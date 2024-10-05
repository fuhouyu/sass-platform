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
package com.fuhouyu.sass.domain.service.impl;

import com.fuhouyu.sass.domain.model.tenant.TenantEntity;
import com.fuhouyu.sass.domain.repository.TenantRepository;
import com.fuhouyu.sass.domain.service.TenantService;
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
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;


    @Override
    public void createTenant(TenantEntity tenantEntity) {
        TenantEntity existsTenantModel = tenantRepository.findByTenantCode(tenantEntity.getTenantCode());
        if (Objects.nonNull(existsTenantModel)) {
            throw new IllegalArgumentException("Tenant already exists!");
        }
        tenantRepository.save(tenantEntity);
    }

    @Override
    public void updateTenant(TenantEntity tenantEntity) {
        TenantEntity exists = tenantRepository.findByTenantCode(tenantEntity.getTenantCode());
        if (Objects.isNull(exists)) {
            throw new IllegalArgumentException("Tenant does not exist!");
        }
        this.tenantRepository.edit(tenantEntity);
    }
}
