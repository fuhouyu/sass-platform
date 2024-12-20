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


import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;

import java.util.List;

/**
 * <p>
 * 租户域的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:23
 */
public interface TenantInfoService extends BaseService<Long, TenantInfoDTO> {


    /**
     * 通过租户编码获取租户
     *
     * @param tenantCode 租户编码
     * @return 租户dto对象
     */
    TenantInfoDTO findByTenantCode(String tenantCode);

    /**
     * 通过用户id查询租户集合
     *
     * @param userId 用户id
     * @return 租户集合
     */
    List<TenantInfoDTO> findTenantByUserId(Long userId);

    /**
     * 租户切换
     *
     * @param id 租户id
     */
    void switchTenant(Long id);
}
