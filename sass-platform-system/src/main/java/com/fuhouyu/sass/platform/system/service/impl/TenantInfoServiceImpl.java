/*
 * Copyright 2024-2024 the original author or authors.
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
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.TenantInfoAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.entity.TenantInfo;
import com.fuhouyu.sass.platform.system.mapper.TenantInfoMapper;
import com.fuhouyu.sass.platform.system.service.TenantInfoService;
import com.fuhouyu.sass.platform.system.service.TenantPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 租户领域模型
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:55
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantInfoServiceImpl implements TenantInfoService {

    private static final TenantInfoAssembler TENANTS_ASSEMBLER = TenantInfoAssembler.INSTANCE;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPermissionService tenantPermissionService;

    private final SnowflakeIdWorker snowflakeIdWorker;


    @Override
    public Long save(TenantInfoDTO tenantInfoDTO) {
        TenantInfo existsTenant = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.nonNull(existsTenant)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "租户编码:%s 已存在", existsTenant.getTenantCode());
        }
        long id = snowflakeIdWorker.nextId();
        TenantInfo entity = TENANTS_ASSEMBLER.toEntity(tenantInfoDTO);
        entity.setId(id);
        tenantInfoMapper.insert(entity);
        this.tenantPermissionService.saveTenantPermission(id, tenantInfoDTO.getPermissionIds());
        return id;
    }

    @Override
    public void edit(TenantInfoDTO tenantInfoDTO) {
        TenantInfo tenantInfo = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.isNull(tenantInfo)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "租户: %s 不存在", tenantInfoDTO.getTenantCode());
        }
        this.tenantInfoMapper.update(TENANTS_ASSEMBLER.toEntity(tenantInfoDTO));
        this.tenantPermissionService.saveTenantPermission(tenantInfoDTO.getId(), tenantInfoDTO.getPermissionIds());
    }

    @Override
    public int removeById(Long id) {
        return this.tenantInfoMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        if (ids.contains(tenantId)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前登录的租户不允许删除操作！");
        }
        int count = this.tenantInfoMapper.deleteByIds(ids);
        this.tenantPermissionService.removeTenantPermissions(ids);
        return count;
    }

    @Override
    public TenantInfoDTO findById(Long id) {
        TenantInfo tenantInfo = this.tenantInfoMapper.queryById(id);
        if (Objects.isNull(tenantInfo)) {
            return null;
        }
        TenantInfoDTO result = TENANTS_ASSEMBLER.toDTO(tenantInfo);
        result.setPermissionIds(this.tenantPermissionService.findPermissionIdByTenantId(id));
        return result;
    }

    @Override
    public Function<PageQueryDTO, List<TenantInfoDTO>> getPageResult() {
        return p -> TENANTS_ASSEMBLER.toDTO(this.tenantInfoMapper.queryList(p));
    }

    @Override
    public TenantInfoDTO findByTenantCode(String tenantCode) {
        return TENANTS_ASSEMBLER.toDTO(this.tenantInfoMapper.queryByTenantCode(tenantCode));
    }
}
