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
package com.fuhouyu.sass.infrastructure.repository.impl;

import com.fuhouyu.sass.common.PageQuery;
import com.fuhouyu.sass.common.PageResult;
import com.fuhouyu.sass.domain.model.tenant.TenantEntity;
import com.fuhouyu.sass.domain.repository.TenantRepository;
import com.fuhouyu.sass.infrastructure.repository.assembler.TenantAssembler;
import com.fuhouyu.sass.infrastructure.repository.mapper.TenantMapper;
import com.fuhouyu.sass.infrastructure.repository.orm.TenantDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 租户存储库实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 22:52
 */
@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantMapper tenantMapper;

    private static final TenantAssembler TENANT_ASSEMBLER = TenantAssembler.INSTANCE;

    @Override
    public TenantEntity findByTenantCode(String tenantCode) {
        return TENANT_ASSEMBLER.toEntity(tenantMapper.queryByTenantCode(tenantCode));
    }

    @Override
    public TenantEntity findById(Long id) {
        return TENANT_ASSEMBLER.toEntity(tenantMapper.queryById(id));
    }

    @Override
    public int removeById(Long id) {
        return tenantMapper.deleteById(id);
    }

    @Override
    public void save(TenantEntity entity) {
        TenantDO tenantDO = TENANT_ASSEMBLER.toDO(entity);
        // TODO 这里后面需要做其它的处理
        tenantMapper.insert(tenantDO);
    }

    @Override
    public void edit(TenantEntity entity) {
        TenantDO tenantDO = TENANT_ASSEMBLER.toDO(entity);
        // TODO 这里后面需要做其它的处理
        int count = tenantMapper.update(tenantDO);
        if (count < 1) {
            throw new IllegalArgumentException("edit tenant error");
        }

    }

    @Override
    public <P extends PageQuery> PageResult<TenantEntity> pageList(P pageable) {
        try (Page<Object> result = PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<TenantDO> tenantList = this.tenantMapper.queryList(pageable);
            List<TenantEntity> modelList = TENANT_ASSEMBLER.toEntity(tenantList);
            return new PageResult<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), modelList);
        }
    }
}
