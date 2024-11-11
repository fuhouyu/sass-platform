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

import com.fuhouyu.sass.platform.common.enums.ResponseCodeEnum;
import com.fuhouyu.sass.platform.common.exception.ServiceException;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.TenantsAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantDTO;
import com.fuhouyu.sass.platform.system.entity.Tenants;
import com.fuhouyu.sass.platform.system.mapper.TenantMapper;
import com.fuhouyu.sass.platform.system.service.TenantService;
import lombok.RequiredArgsConstructor;
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
public class TenantServiceImpl implements TenantService {

    private static final TenantsAssembler TENANTS_ASSEMBLER = TenantsAssembler.INSTANCE;

    private final TenantMapper tenantMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;


    @Override
    public void save(TenantDTO tenantDTO) {
        Tenants existsTenant = tenantMapper.queryByTenantCode(tenantDTO.getTenantCode());
        if (Objects.nonNull(existsTenant)) {
            throw new ServiceException(ResponseCodeEnum.INVALID_PARAM, "租户编码:%s 已存在", existsTenant.getTenantCode());
        }
        tenantDTO.setId(snowflakeIdWorker.nextId());
        tenantMapper.insert(TENANTS_ASSEMBLER.toEntity(tenantDTO));
    }

    @Override
    public void saveBatch(List<TenantDTO> dtoList) {
        List<Tenants> rolesList = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return TENANTS_ASSEMBLER.toEntity(dto);
        }).toList();
        this.tenantMapper.insertBatch(rolesList);
    }

    @Override
    public void edit(TenantDTO tenantDTO) {
        Tenants tenants = tenantMapper.queryByTenantCode(tenantDTO.getTenantCode());
        if (Objects.isNull(tenants)) {
            throw new ServiceException(ResponseCodeEnum.INVALID_PARAM, "租户: %s 不存在", tenantDTO.getTenantCode());
        }
        this.tenantMapper.update(TENANTS_ASSEMBLER.toEntity(tenantDTO));
    }

    @Override
    public int removeById(Long id) {
        return this.tenantMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.tenantMapper.deleteByIds(ids);
    }

    @Override
    public TenantDTO findById(Long id) {
        return TENANTS_ASSEMBLER.toDTO(this.tenantMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<TenantDTO>> getPageResult() {
        return (p) -> TENANTS_ASSEMBLER.toDTO(this.tenantMapper.queryList(p));
    }

    @Override
    public TenantDTO findByTenantCode(String tenantCode) {
        return TENANTS_ASSEMBLER.toDTO(this.tenantMapper.queryByTenantCode(tenantCode));
    }
}
