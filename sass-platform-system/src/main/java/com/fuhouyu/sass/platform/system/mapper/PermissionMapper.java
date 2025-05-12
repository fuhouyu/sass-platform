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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Permissions;
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
public interface PermissionMapper extends BaseMapper<Permissions> {

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
     * @param userId   用户id
     * @return 权限列表
     */
    List<Permissions> queryUserPermissonList(@Param("userId") Long userId);

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

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 通过id进行查询
     *
     * @param id 主键id
     * @return 实体对象
     */
    Permissions queryById(Long id);

    /**
     * 批量通过id进行查询
     *
     * @param list id集合
     * @return 查询到的实体对象
     */
    List<Permissions> queryByIds(@Param("list") Collection<Long> list);

    /**
     * 查询当前租户下所有的租权限
     *
     * @return 权限集合
     */
    List<Permissions> queryAll();

    /**
     * 通过原始的权限ids查询出指定租户下的权限
     *
     * @param ids      ids
     * @param tenantId 租户id
     * @return 权限集合
     */
    List<Permissions> queryBySourceIdsAndTenant(@Param("ids") Collection<Long> ids, @Param("tenantId") Long tenantId);

    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @param <P>       范围查询的类型
     * @return 批量查询
     */
    <P extends PageQueryDTO> List<Permissions> queryList(@Param("pageQuery") P pageQuery);

}
