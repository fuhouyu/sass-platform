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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.sass.platform.system.domain.entity.TenantHasPermission;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户权限关系mapper层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:49
 */
public interface TenantHasPermissionMapper {

    /**
     * 批量保存
     *
     * @param tenantHasPermissions 租户和权限的集合
     */
    void insertBatch(@Param("tenantHasPermissions") Collection<TenantHasPermission> tenantHasPermissions);

    /**
     * 通过租户id和权限id删除
     *
     * @param tenantId      租户id
     * @param permissionIds 权限id集合
     */
    void delete(@Param(("tenantId")) Long tenantId,
                @Param("permissionIds") Collection<Long> permissionIds);

    /**
     * 批量删除租户
     *
     * @param tenantIds 租户id集合
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 通过租户id查询出权限id集合
     *
     * @param tenantId 租户id
     * @return 权限id集合
     */
    List<Long> queryPermissionIdByTenantId(@Param("tenantId") Long tenantId);
}
