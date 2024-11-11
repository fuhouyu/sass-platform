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
package com.fuhouyu.sass.platform.system.service;


import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantDTO;

/**
 * <p>
 * 租户域的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:23
 */
public interface TenantService extends BaseService<PageQueryDTO, TenantDTO, Long> {


    /**
     * 通过租户编码获取租户
     *
     * @param tenantCode 租户编码
     * @return 租户dto对象
     */
    TenantDTO findByTenantCode(String tenantCode);


}
