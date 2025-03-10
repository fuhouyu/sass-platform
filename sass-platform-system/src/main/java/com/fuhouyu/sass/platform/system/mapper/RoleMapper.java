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


import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Roles;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:00
 */
public interface RoleMapper extends BaseMapper<Long, Roles> {

    /**
     * 通过角色编码查询出角色对象
     *
     * @param roleCode 角色编码
     * @return 角色do对象
     */
    @TenantQuery
    Roles queryByRoleCode(String roleCode);

    /**
     * 通过id进行查询
     *
     * @param id 主键id
     * @return 实体对象
     */
    @TenantQuery
    @Override
    Roles queryById(Long id);

    /**
     * 批量通过id进行查询
     *
     * @param list id集合
     * @return 查询到的实体对象
     */
    @TenantQuery
    @Override
    List<Roles> queryByIds(@Param("list") Collection<Long> list);

    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @param <P>       范围查询的类型
     * @return 批量查询
     */
    @TenantQuery
    @Override
    <P extends PageQueryDTO> List<Roles> queryList(@Param("pageQuery") P pageQuery);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户id集合
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 通过角色编码和租户id查询角色对象
     *
     * @param roleCode 角色编码
     * @param tenantId 租户id
     * @return 角色信息
     */
    Roles queryByRoleCodeByTenantId(@Param("roleCode") String roleCode, @Param("tenantId") Long tenantId);
}
