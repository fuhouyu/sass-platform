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
package com.fuhouyu.tenant.domain.service;

import com.fuhouyu.tenant.domain.model.tenant.TenantEntity;
import com.fuhouyu.tenant.domain.repository.TenantRepository;
import com.fuhouyu.tenant.domain.service.impl.TenantServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import static org.mockito.Mockito.doAnswer;

/**
 * <p>
 * 测试租户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 19:50
 */
class TestTenantService extends TestBaseService {

    private final TenantRepository tenantRepository = Mockito.mock(TenantRepository.class);

    @InjectMocks
    private TenantServiceImpl tenantService;


    @Test
    void testSaveTenant() {
        TenantEntity tenantEntity = TenantEntity.builder()
                .tenantName("test")
                .tenantCode("test")
                .contactNumber("test")
                .contactPerson("test")
                .build();

        doAnswer((res) -> {
            tenantEntity.setId(1L);
            return tenantEntity;
        }).when(this.tenantRepository).save(tenantEntity);
        tenantService.createTenant(tenantEntity);
        Assert.notNull(tenantEntity.getId(), "id is null");
    }

    @Test
    void testUpdateTenant() {
        TenantEntity tenantEntity = TenantEntity.builder()
                .id(1L)
                .tenantName("test")
                .tenantCode("test")
                .contactNumber("test")
                .contactPerson("test")
                .build();
        tenantEntity.deleted();
        this.tenantService.updateTenant(tenantEntity);
    }
}
