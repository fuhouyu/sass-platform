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


import com.fuhouyu.sass.platform.system.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.dto.permission.PermissionTreeDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 权限接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:12
 */
public interface PermissionService extends BaseService<Long, PermissionDTO> {


    /**
     * 通过权限编码查询
     *
     * @param permissionCode 权限编码
     * @return 权限dto对象
     */
    PermissionDTO findByPermissionCode(String permissionCode);

    /**
     * 获取当前用户的权限信息
     *
     * @return 权限树集合
     */
    List<PermissionTreeDTO> findPermissionListByMe();

    /**
     * 通过父级id查询子级，为空时查询一级
     *
     * @param parentId 父级id
     * @return 权限列表
     */
    List<PermissionDTO> getPermissionList(Long parentId);

    /**
     * 获取权限树集合
     *
     * @return 权限树dto对象
     */
    List<PermissionTreeDTO> getTreeList();

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @return true 已存在
     */
    Boolean checkPermissionCodeExists(String permissionCode);

    /**
     * 通过ids查询权限集合
     *
     * @param permissionIds 权限id集合
     * @return 权限集合
     */
    List<PermissionDTO> findByIds(Collection<Long> permissionIds);

    /**
     * 通过租户id和用户id查询出权限集合
     *
     * @param tenantId 租户id
     * @param userId   用户id
     * @return 权限集合
     */
    Collection<? extends GrantedAuthority> findUserSimpleGrantedAuthorities(Long tenantId, Long userId);

}
