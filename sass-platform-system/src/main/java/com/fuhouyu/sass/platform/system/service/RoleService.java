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

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RolePageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Roles;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 20:47
 */
public interface RoleService extends IService<Roles> {

    /**
     * 创建角色
     *
     * @param roleDTO 角色dto对象
     * @return 角色id
     */
    long save(RoleDTO roleDTO);


    /**
     * 修改角色
     *
     * @param roleDTO 角色dto对象
     */
    void edit(RoleDTO roleDTO);

    /**
     * 通过id获取角色
     *
     * @param id 主键id
     * @return 角色dto对象
     */
    RoleDTO findById(Long id);

    /**
     * 通过角色编码获取角色
     *
     * @param roleCode 角色编码
     * @return 角色dto对象
     */
    RoleDTO findByRoleCode(String roleCode);

    /**
     * 创建默认的租户角色
     *
     * @param tenantId      租户id
     * @param permissionIds 权限id集合
     * @return 主键id
     */
    Long createTenantDefaultRole(Long tenantId, List<Long> permissionIds);

    /**
     * 角色列表
     *
     * @return 角色列表
     */
    List<RoleDTO> getRoleList();

    /**
     * 删除租户下所有的权限
     *
     * @param tenantIds 租户ids
     */
    void removeByTenantIds(Collection<Long> tenantIds);

    /**
     * 通过角色编码和租户id获取角色
     *
     * @param roleCode 角色编码
     * @param tenantId 租户id
     * @return 角色dto对象
     */
    RoleDTO findByRoleCodeAndTenantId(String roleCode, Long tenantId);

    /**
     * 修改状态信息
     *
     * @param roleDTO 角色dto对象
     */
    void editStatus(RoleDTO roleDTO);

    /**
     * 分页查询
     *
     * @param pageQueryDTO 分页查询对象
     * @return 分页查询集合
     */
    PageResultDTO<RoleDTO> pageList(RolePageQueryDTO pageQueryDTO);
}
