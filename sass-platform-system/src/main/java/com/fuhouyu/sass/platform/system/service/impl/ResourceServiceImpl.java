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
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    private final MinioClient minioClient;
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
        try {
            String objectKey = this.generateKey(resourcePresignedUrlRequestDTO.getBusinessName());
            String url = this.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(tenantSpaceDTO.getBucketName())
                    .object(objectKey)
                    .method(resourcePresignedUrlRequestDTO.getMethod()).build());
            return ResourcePresignedUrlResponseDTO.builder()
                    .objectKey(objectKey)
                    .presignedUrl(url)
                    .build();
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            LoggerUtil.error(log, "生成资源上传url失败", e);
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                    "生成资源上传url失败");
        }
    }

    @Override
    public void previewResource(Long id, HttpServletRequest request,
                                HttpServletResponse response) {
        Resources resources = this.checkResourcePermission(id);
        TenantSpaceDTO tenantSpaceDTO = this.tenantSpaceService.findByTenantId(resources.getOwnerTenantId());


        long[] ranges = this.parseRequestRanges(request);
        long start = ranges[0];
        long end = ranges[1];
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(tenantSpaceDTO.getBucketName())
                .object(resources.getObjectKey())
                .offset(start)
                .length(end == -1 ? Long.MAX_VALUE : end - start + 1)
                .build();

        try (ServletOutputStream outputStream = response.getOutputStream();
             GetObjectResponse getObjectResponse = this.minioClient.getObject(getObjectArgs)) {
            Headers headers = getObjectResponse.headers();
            headers.forEach(key -> response.setHeader(key.component1(), key.component2()));
            // 写入数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = getObjectResponse.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 设置部分响应头
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                    String.format("%s, %s", HttpHeaders.CONTENT_RANGE, HttpHeaders.CONTENT_LENGTH));
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            LoggerUtil.error(log, "预览资源失败", e);
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                    "预览资源失败");
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
     * 解析请求范围
     *
     * @param request 请求
     * @return 请求范围
     */
    private long[] parseRequestRanges(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.RANGE);
        if (StringUtils.isAllEmpty(header)) {
            return new long[]{0, -1};
        }
        if (header.startsWith("bytes=")) {
            String range = header.substring(6);
            String[] ranges = range.split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : -1;
            return new long[]{start, end};
        }
        return new long[]{0, -1};
    }
}
