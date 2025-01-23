/*
 * Copyright 2024-2025 fuhouyu.
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

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户权限接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:46
 */
public interface TenantHasPermissionService {

    /**
     * 保存或更新关联关系
     *
     * @param tenantId      租户id
     * @param permissionIds 权限id集合
     */
    void saveOrUpdateTenantPermission(Long tenantId,
                                      Collection<Long> permissionIds);

    /**
     * 删除关联关系
     *
     * @param tenantId      租户id
     * @param permissionIds 权限id集合
     */
    void removeTenantPermission(Long tenantId, Collection<Long> permissionIds);

    /**
     * 通过租户id批量删除
     *
     * @param tenantIds 租户id
     */
    void removeTenantPermissions(Collection<Long> tenantIds);

    /**
     * 通过租户id查询权限id
     *
     * @param tenantId 租户id
     * @return 权限id集合
     */
    List<Long> findPermissionIdByTenantId(Long tenantId);
}
