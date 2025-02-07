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
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.Organizations;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 组织mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 22:06
 */
public interface OrganizationMapper extends BaseMapper<Long, Organizations> {

    @Override
    @TenantQuery
    Organizations queryById(Long id);

    @Override
    @TenantQuery
    List<Organizations> queryByIds(@Param("list") Collection<Long> list);

    /**
     * 根据组织编码查询组织
     *
     * @param organizationCode 组织编码
     * @return 组织
     */
    @TenantQuery
    Organizations queryByOrganizationCode(@Param("organizationCode") String organizationCode);

    @Override
    @TenantQuery
    <P extends PageQueryDTO> List<Organizations> queryList(@Param("pageQuery") P pageQuery);

    /**
     * 根据组织编码查询当前组织是否存在
     *
     * @param organizationCode 组织编码
     * @return 1存在，0不存在
     */
    @TenantQuery
    Integer existsOrganizationCode(@Param("organizationCode") String organizationCode);

    /**
     * 更新父节点的叶子节点状态
     *
     * @param isLeaf   是否是叶子节点
     * @param parentId 父节点id
     */
    void updateLeafById(@Param("isLeaf") boolean isLeaf, @Param("parentId") Long parentId);

    /**
     * 根据父节点id集合更新叶子节点状态
     *
     * @param parentIdList 父节点id集合
     */
    void setLeafByIdList(@Param("parentIdList") Collection<Long> parentIdList);

    /**
     * 通过父级id获取组织列表
     *
     * @param parentId 父级id
     * @return 组织集合
     */
    @TenantQuery
    List<Organizations> queryListByParentId(@Param("parentId") Long parentId);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);
}
