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
package com.fuhouyu.tenant.infrastructure.repository.impl;

import com.fuhouyu.framework.exception.WebServiceException;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.tenant.domain.model.BasePageQueryModel;
import com.fuhouyu.tenant.domain.model.PageResultModel;
import com.fuhouyu.tenant.domain.model.TenantModel;
import com.fuhouyu.tenant.domain.repository.TenantRepository;
import com.fuhouyu.tenant.infrastructure.repository.convert.TenantAssembler;
import com.fuhouyu.tenant.infrastructure.repository.mapper.TenantMapper;
import com.fuhouyu.tenant.infrastructure.repository.orm.TenantDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantMapper tenantMapper;

    private final TenantAssembler tenantAssembler;

    public TenantRepositoryImpl(TenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
        this.tenantAssembler = TenantAssembler.INSTANCE;
    }

    @Override
    public TenantModel queryByTenantCode(String tenantCode) {
        return tenantAssembler.toModel(tenantMapper.queryByTenantCode(tenantCode));
    }

    @Override
    public TenantModel queryById(Long id) {
        return tenantAssembler.toModel(tenantMapper.queryById(id));
    }

    @Override
    public int deleteById(Long id) {
        return tenantMapper.deleteById(id);
    }

    @Override
    public TenantModel insert(TenantModel model) {
        TenantDO tenantDO = tenantAssembler.toEntity(model);
        LocalDateTime nowTime = LocalDateTime.now();
        tenantDO.setCrateAt(nowTime);
        tenantDO.setUpdateAt(nowTime);
        tenantDO.setCrateBy("admin");
        tenantDO.setUpdateBy("admin");
        // TODO 这里后面需要做其它的处理
        tenantMapper.insert(tenantDO);
        return tenantAssembler.toModel(tenantDO);
    }

    @Override
    public TenantModel update(TenantModel model) {
        TenantDO tenantDO = tenantAssembler.toEntity(model);
        // TODO 这里后面需要做其它的处理
        int count = tenantMapper.update(tenantDO);
        if (count > 0) {
            return model;
        }
        throw new WebServiceException(ResponseCodeEnum.SERVER_ERROR,
                "租户: %s 修改失败", model.getTenantCode());
    }

    @Override
    public <P extends BasePageQueryModel> PageResultModel<TenantModel> pageList(P pageable) {
        try (Page<Object> result = PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<TenantDO> tenantList = this.tenantMapper.queryList();
            List<TenantModel> modelList = this.tenantAssembler.toModel(tenantList);
            return new PageResultModel<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), modelList);
        }
    }
}