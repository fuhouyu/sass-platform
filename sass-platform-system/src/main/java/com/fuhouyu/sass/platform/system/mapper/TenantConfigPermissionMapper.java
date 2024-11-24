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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.sass.platform.system.entity.TenantConfigPermission;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户权限配置关系mapper层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:49
 */
public interface TenantConfigPermissionMapper {

    /**
     * 批量保存
     *
     * @param tenantConfigPermissions 配置关系和权限的集合
     */
    void insertBatch(@Param("tenantConfigPermissions") Collection<TenantConfigPermission> tenantConfigPermissions);

    /**
     * 通过配置id和权限id删除
     *
     * @param tenantConfigId 租户配置id
     * @param permissionIds  权限id集合
     */
    void delete(@Param(("tenantConfigId")) Long tenantConfigId,
                @Param("permissionIds") Collection<Long> permissionIds);

    /**
     * 批量删除配置文件
     *
     * @param configIds 配置id集合
     */
    void deleteConfigPermissions(@Param("configIds") Collection<Long> configIds);

    /**
     * 通过配置id查询出权限id集合
     *
     * @param configId 配置id
     * @return 权限id集合
     */
    List<Long> queryPermissionIdByConfigId(@Param("configId") Long configId);
}
