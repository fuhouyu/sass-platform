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
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionTreeDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Permissions;
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
public interface PermissionService extends IService<Permissions> {


    /**
     * 新增权限
     * @param permissionDTO 权限dto对象
     * @return 主键id
     */
    long save(PermissionDTO permissionDTO);

    /**
     * 编辑权限
     * @param permissionDTO 权限dto对象
     */
    void edit(PermissionDTO permissionDTO);


    /**
     * 删除权限
     *
     * @param ids 主键ids
     * @return 删除数量
     */
    int deleteByIds(Collection<Long> ids);


    /**
     * 通过id查询
     *
     * @param id id
     * @return 权限dto对象
     */
    PermissionDTO findById(Long id);


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

    /**
     * 通过租户id删除
     *
     * @param tenantIds 租户ids
     */
    void removeByTenantIds(Collection<Long> tenantIds);

    /**
     * 复制原始权限到新的租户id
     *
     * @param sourcePermissionList 原始的租户权限集合
     * @param tenantId             租户id
     * @return 新增的权限id集合
     */
    List<Long> copyPermissionToTenant(List<PermissionDTO> sourcePermissionList, Long tenantId);

    /**
     * 通过原始的权限id删除租户下的权限
     *
     * @param ids      ids
     * @param tenantId 租户id
     */
    void removePermissionForTenant(Collection<Long> ids, Long tenantId);

    /**
     * 分页查询权限列表
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResultDTO<PermissionDTO> pageList(PermissionPageQueryDTO queryDTO);
}
