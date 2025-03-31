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

import com.fuhouyu.sass.platform.system.domain.entity.RoleHasPermission;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色和权限的mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/14 16:43
 */
public interface RoleHasPermissionMapper {

    /**
     * 批量插入
     *
     * @param roleHasPermissions 角色和权限的实体集合
     */
    void insertBatch(@Param("roleHasPermissions") Collection<RoleHasPermission> roleHasPermissions);

    /**
     * 通过角色id删除
     *
     * @param roleId               角色id
     * @param excludePermissionIds 需要排除的权限id集合，为空则删除所有
     */
    void deleteByRoleId(@Param("roleId") Long roleId,
                        @Param("excludePermissionIds") Collection<Long> excludePermissionIds);


    /**
     * 通过角色id查询出权限id集合
     *
     * @param roleId 角色id
     * @return 权限id集合
     */
    List<Long> queryPermissionIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过租户ids进行删除
     *
     * @param tenantIds 租户ids
     */
    void deleteRolePermissionByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 通过权限id进行删除
     *
     * @param permissionIds 权限ids
     */
    void deleteByPermissionIds(@Param("permissionIds") Collection<Long> permissionIds);
}
