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
package com.fuhouyu.tenant.domain.service.impl;

import com.fuhouyu.framework.exception.WebServiceException;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.tenant.domain.model.TenantModel;
import com.fuhouyu.tenant.domain.repository.TenantRepository;
import com.fuhouyu.tenant.domain.service.TenantDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
public class TenantDomainServiceImpl implements TenantDomainService {

    private final TenantRepository tenantRepository;


    @Override
    public TenantModel create(TenantModel tenantModel) {
        TenantModel existsTenantModel = tenantRepository.queryByTenantCode(tenantModel.getTenantCode());
        if (Objects.nonNull(existsTenantModel)) {
            throw new WebServiceException(
                    ResponseCodeEnum.INVALID_PARAM,
                    "当前租户编码: %s 已存在.", existsTenantModel.getTenantCode());
        }
        return tenantRepository.insert(tenantModel);
    }

    @Override
    public TenantModel update(TenantModel tenantModel) {
        TenantModel exists = tenantRepository.queryByTenantCode(tenantModel.getTenantCode());
        if (Objects.isNull(exists)) {
            throw new WebServiceException(
                    ResponseCodeEnum.NOT_FOUND,
                    "当前租户不存在: %s.", tenantModel.getTenantCode());
        }
        return this.tenantRepository.update(tenantModel);
    }
}
