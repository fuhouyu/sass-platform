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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.sass.platform.system.assembler.TenantSpaceAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantSpaceDetailDTO;
import com.fuhouyu.sass.platform.system.domain.entity.TenantSpace;
import com.fuhouyu.sass.platform.system.mapper.TenantSpaceMapper;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户空间实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:58
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TenantSpaceSpaceServiceImpl implements TenantSpaceService {

    private static final TenantSpaceAssembler TENANT_SPACE_ASSEMBLER = TenantSpaceAssembler.INSTANCE;

    private final TenantSpaceMapper tenantSpaceMapper;

    private final S3Client s3Client;


    @Override
    public void saveTenantSpace(TenantSpaceDTO tenantSpaceDTO) {
        if (Objects.isNull(tenantSpaceDTO.getTenantId())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "未选择租户");
        }
        TenantSpace tenantSpace = this.tenantSpaceMapper.queryById(tenantSpaceDTO.getTenantId());
        if (Objects.nonNull(tenantSpace)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前租户空间已存在");
        }
        TenantSpace entity = TENANT_SPACE_ASSEMBLER.toEntity(tenantSpaceDTO);
        this.tenantSpaceMapper.insert(entity);
        try {

            this.s3Client.createBucket(builder -> {
                builder.bucket(entity.getBucketName());
                builder.acl(tenantSpaceDTO.getAcl());
            });
        } catch (BucketAlreadyOwnedByYouException e) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前租户空间 [%s] 已存在，请修改后重试", tenantSpaceDTO.getBucketName());
        }

    }

    @Override
    public void editTenantSpace(TenantSpaceDTO tenantSpaceDTO) {
        TenantSpaceDTO currentTenantSpace = this.checkExists(tenantSpaceDTO.getTenantId());
        if (!Objects.equals(currentTenantSpace.getAcl(), tenantSpaceDTO.getAcl())) {
            this.s3Client.putBucketAcl(builder -> builder.acl(tenantSpaceDTO.getAcl()));
        }
        this.tenantSpaceMapper.update(TENANT_SPACE_ASSEMBLER.toEntity(tenantSpaceDTO));

    }

    @Override
    public TenantSpaceDTO findByTenantId(Long tenantId) {
        return TENANT_SPACE_ASSEMBLER.toDTO(this.tenantSpaceMapper.queryById(tenantId));
    }

    @Override
    public TenantSpaceDTO checkExists(Long tenantId) {
        TenantSpace tenantSpace = this.tenantSpaceMapper.queryById(tenantId);
        if (Objects.isNull(tenantSpace)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前租户空间不存在");
        }
        return TENANT_SPACE_ASSEMBLER.toDTO(tenantSpace);
    }

    @Override
    public Boolean checkNameExists(String spaceName) {
        return this.tenantSpaceMapper.countTenantSpaceByName(spaceName) > 0;
    }

    @Override
    public void removeSpaceByTenantIds(Collection<Long> tenantIds) {
        List<TenantSpace> tenantSpaces = this.tenantSpaceMapper.queryByIds(tenantIds);
        if (CollectionUtils.isEmpty(tenantSpaces)) {
            LoggerUtil.warn(log, "当前租户空间不存在，无需删除，租户id:{}", tenantIds);
        }

        this.tenantSpaceMapper.deleteByIds(tenantIds);
        tenantSpaces.forEach(tenantSpace -> {
            this.s3Client.deleteBucket(builder -> builder.bucket(tenantSpace.getBucketName()));
        });
    }

    @Override
    public TenantSpaceDetailDTO findDetailByTenantId(Long tenantId) {
        return this.tenantSpaceMapper.queryDetailById(tenantId);
    }
}
