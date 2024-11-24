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
 * 租户配置接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:46
 */
public interface TenantConfigPermissionService {

    /**
     * 保存关联关系
     *
     * @param configId      配置id
     * @param permissionIds 权限id集合
     */
    void saveConfigPermission(Long configId,
                              Collection<Long> permissionIds);

    /**
     * 删除关联关系
     *
     * @param configId      配置id
     * @param permissionIds 权限id集合
     */
    void deleteConfigPermission(Long configId, Collection<Long> permissionIds);

    /**
     * 通过配置id批量删除
     *
     * @param configIds 配置id
     */
    void deleteConfigPermissions(Collection<Long> configIds);

    /**
     * 通过配置id查询权限id
     *
     * @param configId 配置id
     * @return 权限id集合
     */
    List<Long> findPermissionIdByConfigId(Long configId);
}
