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
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.service.StsOperation;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.ResourcesAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceSignedUrlDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.StsTemporaryTokenRequestDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.StsTemporaryTokenResponseDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Resources;
import com.fuhouyu.sass.platform.system.enums.response.ResourceResponseStatusEnum;
import com.fuhouyu.sass.platform.system.mapper.ResourceMapper;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import com.fuhouyu.sass.platform.system.utils.SignedUrlUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.BinaryUtils;
import software.amazon.awssdk.utils.Md5Utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    /**
     * 过期时间一个小时
     */
    private static final long EXPIRE_TIME = Duration.ofHours(1).getSeconds();

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final String BASE_DOWNLOAD_API = "%s/v1/resource/download/%s";

    private static final String TMP_DIR = "tmp/";

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final HttpServletRequest httpServletRequest;

    private final HttpServletResponse httpServletResponse;

    private final S3Client s3Client;

    private final SnowflakeIdWorker snowflake;

    private final ResourceMapper resourceMapper;

    private final TenantSpaceService tenantSpaceService;

    private final StsOperation stsOperation;

    private final S3Properties s3Properties;

    private final S3Presigner s3Presigner;

    @Override
    public Long save(ResourceDTO dto) {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(tenantId);
        String bucketName = tenantSpaceDTO.getBucketName();
        String oldObject = dto.getObjectKey();
        String newObjectKey = oldObject.replace(TMP_DIR, "");
        Long parentId = this.createDirectory(this.getParentPath(newObjectKey), dto.getIsPublic());
        // 复制资源
        this.s3Client.copyObject(builder -> {
            builder.sourceBucket(bucketName);
            builder.destinationBucket(bucketName);
            builder.sourceKey(oldObject);
            builder.destinationKey(newObjectKey);
        });
        // 删除临时资源
        this.s3Client.deleteObject(builder -> builder.bucket(bucketName).key(oldObject));
        Resources entity = RESOURCES_ASSEMBLER.toEntity(dto);
        long id = snowflake.nextId();
        entity.setId(id);
        entity.setObjectKey(newObjectKey);
        entity.setParentId(parentId);
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
        List<String> directoryPrefixList = resources.stream().filter(Resources::getIsDirectory)
                .map(Resources::getObjectKey)
                .toList();
        if (!CollectionUtils.isEmpty(directoryPrefixList)) {
            // 查询出所有的关联文件信息
            resources.addAll(this.resourceMapper.queryByPrefixList(directoryPrefixList));
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
        try {
            this.s3Client.deleteObjects(deleteObjectsRequest);
        } catch (S3Exception e) {
            LoggerUtil.error(log, "资源文件: [{}] 删除失败，错误信息: {}", resources, e.getMessage(), e);
        }
        return this.resourceMapper.deleteByIds(resources.stream().map(Resources::getId).toList());
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
    public void downloadFile(Long id, ResourceSignedUrlDTO resourceSignedUrlDTO) {
        Resources resources = this.resourceMapper.queryById(id);
        this.checkSignedUrl(resourceSignedUrlDTO, resources);
        ResponseInputStream<GetObjectResponse> responseResponseInputStream = this.downloadFileByS3(RESOURCES_ASSEMBLER.toDTO(resources));
        // 判断是预览还是下载
        if (Objects.equals(resourceSignedUrlDTO.getPreview(), Boolean.TRUE)) {
            httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, responseResponseInputStream.response().contentType());
        } else {
            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    String.format("attachment; filename=\"%s\"", URLEncoder.encode(resources.getName(), StandardCharsets.UTF_8)));
        }
        try {
            this.doFileDownload(responseResponseInputStream);
        } catch (IOException e) {
            // ignore 这里如果是客户端取消下载，会抛出异常，不需要处理
        }
    }

    @Override
    public StsTemporaryTokenResponseDTO generateToken(StsTemporaryTokenRequestDTO requestDTO) {
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(ContextHolderStrategy.getContext().getUser().getTenantId());
        List<String> fileNames = requestDTO.getFileNames();
        Map<String, String> objectsMap = new HashMap<>(fileNames.size());
        String prefix = requestDTO.getPrefix();
        for (String fileName : fileNames) {
            String parentPath = this.getParentPath(fileName);
            String parentPrefix = Objects.isNull(parentPath) ? prefix : (Objects.isNull(prefix) ? parentPath : prefix + "/" + parentPath);
            objectsMap.put(fileName, this.generateKey(parentPrefix));
        }
        // 只允许上传到指定的对象中
        StsTokenResponse stsTokenResponse = this.stsOperation.generateStsToken(tenantSpaceDTO.getBucketName(),
                objectsMap.values(),
                StsActionEnum.PutObject);
        return StsTemporaryTokenResponseDTO.builder()
                .accessKeyId(stsTokenResponse.getAccessKey())
                .secretAccessKey(stsTokenResponse.getSecretAccessKey())
                .stsToken(stsTokenResponse.getSessionToken())
                .bucketName(tenantSpaceDTO.getBucketName())
                .objectsMap(objectsMap)
                .enabledPathStyle(stsTokenResponse.getEnablePathStyle())
                .endpoint(stsTokenResponse.getEndpoint())
                .region(stsTokenResponse.getRegion())
                .build();
    }

    @Override
    public ResourceDTO findResourceByEtag(String etag) {
        return RESOURCES_ASSEMBLER.toDTO(this.resourceMapper.queryByEtag(etag));
    }


    @Override
    public String generateSignedUrl(Long id,
                                    Boolean preview) {
        ResourceDTO resourceDTO = this.checkResourcePermission(id);
        String baseUrl = String.format(BASE_DOWNLOAD_API, this.getHttpBaseUrl(), id);
        SignedUrlUtil.UrlSignedDTO urlSignedDTO = SignedUrlUtil.UrlSignedDTO
                .builder()
                .expiresSeconds(EXPIRE_TIME)
                .accessKey(resourceDTO.getObjectKey())
                .secretKey(s3Properties.getSecretKey())
                .params(Map.of("preview", preview))
                .build();
        return SignedUrlUtil.generateSignedUrl(baseUrl, urlSignedDTO);
    }

    @Override
    public String generatePresignerDownloadUrl(Long id) {
        ResourceDTO resourceDTO = this.checkResourcePermission(id);
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.checkExists(resourceDTO.getOwnerTenantId());

        PresignedGetObjectRequest presignedGetObjectRequest = this.s3Presigner.presignGetObject(request -> {
            request.signatureDuration(Duration.ofHours(1));
            request.getObjectRequest(getObject -> {
                getObject.key(resourceDTO.getObjectKey());
                getObject.bucket(tenantSpaceDTO.getBucketName());
                getObject.responseContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
                getObject.responseContentDisposition(String.format("attachment; filename=\"%s\"", URLEncoder.encode(resourceDTO.getName(), StandardCharsets.UTF_8)));
            });
        });
        return presignedGetObjectRequest.url().toExternalForm();
    }

    @Override
    public Integer countObjects() {
        return this.resourceMapper.countObjects();
    }

    /**
     * 检查资源权限
     *
     * @param id 资源id
     * @return 资源
     */
    @Override
    public ResourceDTO checkResourcePermission(Long id) {
        Resources resources = this.resourceMapper.queryById(id);
        if (Objects.isNull(resources)) {
            LoggerUtil.warn(log, "资源不存在, id: {}", id);
            throw new ServiceException(ResourceResponseStatusEnum.RESOURCE_NOT_EXISTS);
        }
        if (!resources.getIsPublic()) {
            if (Objects.isNull(ContextHolderStrategy.getContext().getUser()) ||
                    !Objects.equals(ContextHolderStrategy.getContext().getUser().getTenantId(), resources.getOwnerTenantId())) {
                throw new ServiceException(ResponseStatusEnum.NOT_AUTH, "无权访问该资源");
            }
        }
        return RESOURCES_ASSEMBLER.toDTO(resources);
    }


    /**
     * 获取父级路径
     *
     * @param objectKey objectKey
     * @return 父级路径
     */
    private String getParentPath(String objectKey) {
        Path path = Paths.get(objectKey);
        Path parentPath = path.getParent();
        return Objects.isNull(parentPath) ? null : parentPath.toString();
    }

    /**
     * 生成随机的objectKey
     *
     * @param prefix 前缀
     * @return objectKey
     */
    private String generateKey(String prefix) {
        String datetime = LocalDate.now().format(DATETIME_FORMAT);
        String randomString = RandomUtil.randomString(5);
        if (Objects.isNull(prefix)) {
            return String.format("%s%s-%s", TMP_DIR, datetime, randomString);
        }
        return String.format("%s%s/%s-%s", TMP_DIR, prefix, datetime, randomString);
    }


    /**
     * 设置http响应头
     *
     * @param objectResponse object响应
     * @param response       响应
     */
    private void setHttpResponseHeader(GetObjectResponse objectResponse,
                                       HttpServletResponse response) {
        response.setContentLengthLong(objectResponse.contentLength());
        response.setHeader(HttpHeaders.CONTENT_RANGE, objectResponse.contentRange());
        response.setHeader(HttpHeaders.CACHE_CONTROL, objectResponse.cacheControl());
        response.setHeader(HttpHeaders.EXPIRES, objectResponse.expiresString());
        response.setHeader(HttpHeaders.ETAG, objectResponse.eTag());
        response.setHeader(HttpHeaders.LAST_MODIFIED, objectResponse.lastModified().toString());
        response.setHeader(HttpHeaders.ACCEPT_RANGES, objectResponse.acceptRanges());
        response.setHeader(HttpHeaders.CONTENT_ENCODING, objectResponse.contentEncoding());
        response.setHeader(HttpHeaders.CONTENT_LANGUAGE, objectResponse.contentLanguage());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                String.format("%s,%s,%s,%s", HttpHeaders.ACCEPT_RANGES, HttpHeaders.CONTENT_LENGTH, HttpHeaders.CONTENT_TYPE, HttpHeaders.CONTENT_RANGE));
    }


    /**
     * 创建目录
     *
     * @param parentPathName 父级路径名
     * @param isPublic       是否公开
     * @return 父级id
     */
    private Long createDirectory(String parentPathName, boolean isPublic) {
        if (StringUtils.isAllBlank(parentPathName)) {
            return -1L;
        }
        String[] split = parentPathName.split("/");
        Long parentId = -1L;
        for (String name : split) {
            String objectKey = parentPathName.substring(0, parentPathName.indexOf(name)) + name;
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
    private synchronized Long doCreateDirectory(String name,
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

    @SneakyThrows
    private String calculateContentMd5(Delete delete) {
        String xml = delete.toBuilder().build().toString();
        byte[] md5Hash = Md5Utils.computeMD5Hash(RequestBody.fromString(xml).contentStreamProvider().newStream());
        return BinaryUtils.toBase64(md5Hash);
    }


    /**
     * 文件下载
     *
     * @param responseResponseInputStream oss资源流
     * @throws IOException io异常
     */
    private void doFileDownload(ResponseInputStream<GetObjectResponse> responseResponseInputStream) throws IOException {
        setHttpResponseHeader(responseResponseInputStream.response(), httpServletResponse);
        try (ServletOutputStream outputStream = httpServletResponse.getOutputStream();
             responseResponseInputStream) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = responseResponseInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 检查签名url
     *
     * @param resourceSignedUrlDTO 资源签名url dto
     * @param resources            资源文件
     */
    private void checkSignedUrl(ResourceSignedUrlDTO resourceSignedUrlDTO,
                                Resources resources) {
        if (Objects.isNull(resources)) {
            throw new ServiceException(ResponseStatusEnum.NOT_FOUND,
                    "当前资源不存在");
        }
        if (resources.getIsPublic()) {
            return;
        }
        SignedUrlUtil.VerifySignedUrlDTO verifySignedUrlDTO = SignedUrlUtil.VerifySignedUrlDTO
                .builder()
                .expires(resourceSignedUrlDTO.getExpires())
                .nonce(resourceSignedUrlDTO.getNonce())
                .signature(resourceSignedUrlDTO.getSignature())
                .accessKey(resources.getObjectKey())
                .secretKey(s3Properties.getSecretKey())
                .signature(resourceSignedUrlDTO.getSignature())
                .params(Map.of("preview", resourceSignedUrlDTO.getPreview()))
                .build();
        SignedUrlUtil.verifySignedUrl(verifySignedUrlDTO);
    }

    /**
     * 从s3 下载资源
     *
     * @param resourceDTO 资源文件
     * @return 从s3下载的资源
     */
    private ResponseInputStream<GetObjectResponse> downloadFileByS3(ResourceDTO resourceDTO) {
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.findByTenantId(resourceDTO.getOwnerTenantId());
        String rangeHeader = httpServletRequest.getHeader(HttpHeaders.RANGE);
        int status = Objects.isNull(rangeHeader) ?
                HttpServletResponse.SC_OK : HttpServletResponse.SC_PARTIAL_CONTENT;
        httpServletResponse.setStatus(status);
        return this.s3Client.getObject(builder -> {
            builder.bucket(tenantSpaceDTO.getBucketName())
                    .key(resourceDTO.getObjectKey());
            if (Objects.nonNull(rangeHeader)) {
                builder.range(rangeHeader);
            }
        });
    }


    /**
     * 获取url 地址
     *
     * @return 当前url地址
     */
    private String getHttpBaseUrl() {
        String baseUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName();
        if (httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443) {
            baseUrl += ":" + httpServletRequest.getServerPort();
        }
        return baseUrl;
    }
}


