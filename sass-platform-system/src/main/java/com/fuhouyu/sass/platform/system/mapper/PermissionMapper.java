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

import com.fuhouyu.sass.platform.system.entity.Permissions;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:00
 */
public interface PermissionMapper extends BaseMapper<Long, Permissions> {

    /**
     * 通过权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限do对象
     */
    Permissions queryByPermissionCode(String permissionCode);

    /**
     * 通过角色id集合查询出权限集合
     *
     * @param roleIdList 角色id集合
     * @return 权限集合
     */
    List<Permissions> queryListByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 查询当前租户下的用户权限
     *
     * @param tenantId 租户id
     * @param userId   用户id
     * @return 权限列表
     */
    List<Permissions> queryUserPermissonList(@Param("tenantId") Long tenantId,
                                             @Param("userId") Long userId);

    /**
     * 通过父级id查询子级
     *
     * @param parentId 父级id
     * @return 权限集合
     */
    List<Permissions> queryListByParentId(@Param("parentId") Long parentId);

    /**
     * 修改叶子节点
     *
     * @param isLeaf 是否为叶子节点
     * @param id     id
     */
    void updateLeafById(@Param("isLeaf") boolean isLeaf, @Param("id") Long id);

    /**
     * 根据父级id设置叶子节点
     *
     * @param parentIdList 父级id集合
     */
    void setLeafByIdList(@Param("parentIdList") Collection<Long> parentIdList);

    /**
     * 查询租户下当前用户的权限编码
     *
     * @param tenantId 租户id
     * @param userId   用户id
     * @return 权限编码集合
     */
    Set<String> queryUserPermissionCodeList(@Param("tenantId") Long tenantId, @Param("userId") Long userId);
}
