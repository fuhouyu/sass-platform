/*
 * Copyright 2024-2025 fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuhouyu.sass.platform.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.s3.StsOperation;
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.ResourcesAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.dto.resource.SaveResourceDTO;
import com.fuhouyu.sass.platform.system.dto.resource.StsTemporaryTokenDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.entity.Resources;
import com.fuhouyu.sass.platform.system.mapper.ResourceMapper;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.utils.BinaryUtils;
import software.amazon.awssdk.utils.Md5Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * 资源实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/16 13:02
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final ResourcesAssembler RESOURCES_ASSEMBLER = ResourcesAssembler.INSTANCE;

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final String TMP_DIR = "tmp";

    private final S3Client s3Client;

    private final SnowflakeIdWorker snowflake;

    private final ResourceMapper resourceMapper;

    private final TenantSpaceService tenantSpaceService;

    private final StsOperation stsOperation;

    private final S3Properties s3Properties;


    @Override
    public Long save(ResourceDTO dto) {
        Resources entity = RESOURCES_ASSEMBLER.toEntity(dto);
        long id = snowflake.nextId();
        entity.setId(id);
        entity.setParentId(Optional.ofNullable(dto.getParentId()).orElse(-1L));
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        this.resourceMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(ResourceDTO dto) {
        this.resourceMapper.update(RESOURCES_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long aLong) {
        return this.resourceMapper.deleteById(aLong);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(ContextHolderStrategy.getContext().getUser().getTenantId());
        List<Resources> resources = this.resourceMapper.queryByIds(ids);
        if (CollectionUtils.isEmpty(resources)) {
            return 0;
        }
        List<ObjectIdentifier> objectIdentifiers = resources.stream().map(resource ->
                ObjectIdentifier.builder()
                        .key(resource.getObjectKey())
                        .eTag(resource.getEtag())
                        .build()
        ).toList();
        Delete deleteObjects = Delete.builder()
                .objects(objectIdentifiers)
                .quiet(true)
                .build();
        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(tenantSpaceDTO.getBucketName())
                .overrideConfiguration(builder -> builder.putHeader("Content-Md5", this.calculateContentMd5(deleteObjects)))
                .delete(deleteObjects)
                .build();
        this.s3Client.deleteObjects(deleteObjectsRequest);
        return this.resourceMapper.deleteByIds(resources.stream().map(Resources::getId).toList());
    }

    @SneakyThrows
    private String calculateContentMd5(Delete delete) {
        // 将 Delete 对象转换为 XML 字符串
        String xml = delete.toBuilder().build().toString();

        // 计算 MD5 哈希值
        byte[] md5Hash = Md5Utils.computeMD5Hash(RequestBody.fromString(xml).contentStreamProvider().newStream());

        // 将 MD5 哈希值转换为 Base64 编码的字符串
        return BinaryUtils.toBase64(md5Hash);
    }

    @Override
    public ResourceDTO findById(Long id) {
        Resources resources = this.resourceMapper.queryById(id);
        return RESOURCES_ASSEMBLER.toDTO(resources);
    }

    @Override
    public Function<PageQueryDTO, List<ResourceDTO>> getPageResult() {
        return p -> RESOURCES_ASSEMBLER.toDTO(this.resourceMapper.queryList(p));
    }

    @Override
    public void previewResource(Long id, HttpServletRequest request,
                                HttpServletResponse response) {
        Resources resources = this.checkResourcePermission(id);
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.findByTenantId(resources.getOwnerTenantId());

        try (ServletOutputStream outputStream = response.getOutputStream();
             ResponseInputStream<GetObjectResponse> responseResponseInputStream = this.s3Client.getObject(builder ->
                     builder.bucket(tenantSpaceDTO.getBucketName()).key(resources.getObjectKey())
             )) {
            // 设置必要的 HTTP 头部
            this.setHttpResponseHeader(responseResponseInputStream.response(), response, resources.getName());

            // 8KB 缓冲区
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = responseResponseInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            LoggerUtil.info(log, "资源下载成功: bucket={}, key={}, size={}",
                    tenantSpaceDTO.getBucketName(),
                    resources.getObjectKey(),
                    responseResponseInputStream.response().contentLength());
        } catch (IOException e) {
            // ignore 这里如果是客户端取消下载，会抛出异常，不需要处理
        }
    }

    @Override
    public StsTemporaryTokenDTO generateToken() {
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(ContextHolderStrategy.getContext().getUser().getTenantId());
        // 只允许上传到指定的对象中
        String objectKey = this.generateKey();
        AssumeRoleResponse assumeRoleResponse = this.stsOperation.generateStsToken(tenantSpaceDTO.getBucketName(),
                objectKey,
                StsActionEnum.PutObject);
        Credentials credentials = assumeRoleResponse.credentials();
        return StsTemporaryTokenDTO.builder()
                .accessKeyId(credentials.accessKeyId())
                .secretAccessKey(credentials.secretAccessKey())
                .stsToken(credentials.sessionToken())
                .bucketName(tenantSpaceDTO.getBucketName())
                .objectKey(objectKey)
                .enabledPathStyle(s3Properties.getPathStyleEnabled())
                .endpoint(s3Properties.getEndpoint())
                .region(s3Properties.getRegion().id())
                .build();
    }

    @Override
    public Long saveResource(SaveResourceDTO resourceDTO) {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(tenantId);
        String bucketName = tenantSpaceDTO.getBucketName();
        String businessName = Optional.ofNullable(resourceDTO.getBusinessName()).orElse("");
        businessName = businessName.endsWith("/") ? businessName.substring(0, businessName.length() - 1) : businessName;
        String newObjectKey = resourceDTO.getObjectKey().replace(TMP_DIR, businessName);
        Long parentId = this.createDirectory(businessName, resourceDTO.getIsPublic());
        // 复制资源
        this.s3Client.copyObject(builder -> {
            builder.sourceBucket(bucketName);
            builder.destinationBucket(bucketName);
            builder.sourceKey(resourceDTO.getObjectKey());
            builder.destinationKey(newObjectKey);
        });
        resourceDTO.setParentId(parentId);
        resourceDTO.setObjectKey(newObjectKey);
        return this.save(resourceDTO);
    }

    @Override
    public ResourceDTO findResourceByEtag(String etag) {
        return RESOURCES_ASSEMBLER.toDTO(this.resourceMapper.queryByEtag(etag));
    }

    /**
     * 检查资源权限
     *
     * @param id 资源id
     * @return 资源
     */
    private Resources checkResourcePermission(Long id) {
        Resources resources = this.resourceMapper.queryById(id);
        if (Objects.isNull(resources)) {
            LoggerUtil.warn(log, "资源不存在, id: {}", id);
            throw new ServiceException(ResponseStatusEnum.NOT_FOUND,
                    "资源文件不存在");
        }
        if (!resources.getIsPublic()) {
            if (Objects.isNull(ContextHolderStrategy.getContext().getUser()) ||
                    !Objects.equals(ContextHolderStrategy.getContext().getUser().getTenantId(), resources.getOwnerTenantId())) {
                throw new ServiceException(ResponseStatusEnum.NOT_AUTH, "无权访问该资源");
            }
        }
        return resources;
    }

    /**
     * 生成随机的objectKey
     *
     * @return objectKey
     */
    private String generateKey() {
        String datetime = LocalDate.now().format(DATETIME_FORMAT);
        String randomString = RandomUtil.randomString(5);
        return String.format("%s/%s-%s", TMP_DIR, datetime, randomString);
    }


    /**
     * 设置http响应头
     *
     * @param objectResponse object响应
     * @param response       响应
     * @param resourceName   资源名称
     */
    private void setHttpResponseHeader(GetObjectResponse objectResponse,
                                       HttpServletResponse response,
                                       String resourceName) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, objectResponse.contentType());
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(objectResponse.contentLength()));
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resourceName);
        response.setHeader(HttpHeaders.CACHE_CONTROL, objectResponse.cacheControl());
        response.setHeader(HttpHeaders.EXPIRES, objectResponse.expiresString());
        response.setHeader(HttpHeaders.ETAG, objectResponse.eTag());
        response.setHeader(HttpHeaders.LAST_MODIFIED, objectResponse.lastModified().toString());
        response.setHeader(HttpHeaders.ACCEPT_RANGES, objectResponse.acceptRanges());
        response.setHeader(HttpHeaders.CONTENT_RANGE, objectResponse.contentRange());
        response.setHeader(HttpHeaders.CONTENT_ENCODING, objectResponse.contentEncoding());
        response.setHeader(HttpHeaders.CONTENT_LANGUAGE, objectResponse.contentLanguage());
    }


    /**
     * 创建目录
     *
     * @param businessName 业务名
     * @param isPublic     是否公开
     * @return 父级id
     */
    private Long createDirectory(String businessName, boolean isPublic) {
        if (StringUtils.isAllBlank(businessName)) {
            return -1L;
        }
        String[] split = businessName.split("/");
        Long parentId = -1L;
        for (String name : split) {
            String objectKey = businessName.substring(0, businessName.indexOf(name)) + name;
            parentId = this.doCreateDirectory(name, objectKey, isPublic, parentId);
        }
        return parentId;
    }

    /**
     * 创建目录资源
     *
     * @param name      名称
     * @param objectKey objectKey
     * @param isPublic  是否公开
     * @param parentId  父级id
     * @return 父级id
     */
    private Long doCreateDirectory(String name,
                                   String objectKey,
                                   Boolean isPublic,
                                   Long parentId) {
        Resources resources = this.resourceMapper.queryByObjectKey(objectKey);
        if (Objects.nonNull(resources)) {
            return resources.getId();
        }
        resources = new Resources();
        long id = snowflake.nextId();
        resources.setId(id);
        resources.setParentId(parentId);
        resources.setName(name);
        resources.setSize(0L);
        resources.setEtag("");
        resources.setMimeType("");
        resources.setObjectKey(objectKey);
        resources.setVersion(1);
        resources.setIsPublic(isPublic);
        resources.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        resources.setIsDirectory(true);
        this.resourceMapper.insert(resources);
        return id;

    }
}
