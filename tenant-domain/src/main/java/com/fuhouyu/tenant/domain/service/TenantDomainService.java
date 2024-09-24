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

import com.fuhouyu.tenant.domain.model.TenantEntity;

/**
 * <p>
 * 租户域的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:23
 */
public interface TenantDomainService {


    /**
     * 通过租户模型，创建一个租户
     *
     * @param tenantModel 租户模型对象
     * @return 租户模型对象
     */
    TenantEntity create(TenantEntity tenantModel);

    /**
     * 修改租户模型对象
     *
     * @param tenantModel 租户模型对象
     * @return 租户模型对象
     */
    TenantEntity update(TenantEntity tenantModel);
}
