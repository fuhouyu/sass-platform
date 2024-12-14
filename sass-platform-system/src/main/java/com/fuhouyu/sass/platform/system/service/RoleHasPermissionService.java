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

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色权限接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/14 16:35
 */
public interface RoleHasPermissionService {

    /**
     * 保存角色id和权限id集合
     *
     * @param roleId        角色id
     * @param permissionIds 权限id集合
     */
    void saveRolePermission(Long roleId,
                            Collection<Long> permissionIds);

    /**
     * 删除角色和id的关联关系
     *
     * @param roleId 角色id
     */
    void removeRolePermission(Long roleId);

    /**
     * 删除角色和id的关联关系
     *
     * @param roleId               角色id
     * @param excludePermissionIds 需要排除的权限id，为空则删除所有
     */
    void removeRolePermission(Long roleId,
                              Collection<Long> excludePermissionIds);


    /**
     * 通过角色id查询出权限id集合
     *
     * @param roleId 角色id
     * @return 权限id集合
     */
    List<Long> findPermissionIdsByRoleId(Long roleId);
}
