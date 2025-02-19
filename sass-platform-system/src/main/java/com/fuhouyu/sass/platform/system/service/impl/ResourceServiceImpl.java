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
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.ResourcesAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourcePresignedUrlRequestDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourcePresignedUrlResponseDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.entity.Resources;
import com.fuhouyu.sass.platform.system.mapper.ResourceMapper;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SnowflakeIdWorker snowflake;
    private final ResourceMapper resourceMapper;
    private final TenantSpaceService tenantSpaceService;

    @Override
    public ResourcePresignedUrlResponseDTO generateResourcePresignedUrl(ResourcePresignedUrlRequestDTO resourcePresignedUrlRequestDTO) {
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.findByTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        if (Objects.isNull(tenantSpaceDTO)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前租户空间不存在");
        }
        String objectKey = this.generateKey(resourcePresignedUrlRequestDTO.getBusinessName());
        PresignedPutObjectRequest presignedPutObjectRequest = this.s3Presigner.presignPutObject(builder -> builder.putObjectRequest(r ->
                r.bucket(tenantSpaceDTO.getBucketName()).key(objectKey)).signatureDuration(Duration.ofDays(1))
        );
        return ResourcePresignedUrlResponseDTO.builder()
                .objectKey(objectKey)
                .presignedUrl(presignedPutObjectRequest.url().toExternalForm())
                .build();
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
    public Long save(ResourceDTO dto) {
        Resources entity = RESOURCES_ASSEMBLER.toEntity(dto);
        long id = snowflake.nextId();
        entity.setId(id);
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
        return this.resourceMapper.deleteByIds(ids);
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
     * @param businessName 业务名称
     * @return objectKey
     */
    private String generateKey(String businessName) {
        String datetime = LocalDate.now().format(DATETIME_FORMAT);
        String randomString = RandomUtil.randomString(5);
        return String.format("%s/%s-%s", businessName, datetime, randomString);
    }


    /**
     * 设置http响应头
     * @param objectResponse object响应
     * @param response 响应
     * @param resourceName 资源名称
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

}
