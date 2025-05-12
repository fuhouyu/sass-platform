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
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationDTO;
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationTreeDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Organizations;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 组织接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 22:27
 */
public interface OrganizationService extends IService<Organizations> {


    /**
     * 保存组织信息
     *
     * @param dto 组织信息
     * @return 组织id
     */
    long save(OrganizationDTO dto);

    /**
     * 修改组织信息
     *
     * @param dto 组织信息
     */
    void edit(OrganizationDTO dto);

    /**
     * 批量删除组织信息
     * @param ids ids
     * 批量删除组织信息
     */
    Integer deleteByIds(Collection<Long> ids);


    /**
     * 通过id获取组织信息
     *
     * @param id 组织id
     * @return 组织信息
     */
    OrganizationDTO findById(Long id);

    /**
     * 分页查询组织信息
     *
     * @param pageQuery 分页查询条件
     * @return 组织分页信息
     */
    PageResultDTO<OrganizationDTO> pageList(OrganizationPageQueryDTO pageQuery);

    /**
     * 检查组织编码是否存在
     *
     * @param organizationCode 组织编码
     * @return 是否存在 true 存在 false 不存在
     */
    Boolean checkOrganizationCodeExists(String organizationCode);

    /**
     * 通过组织的父级id获取组织列表
     *
     * @param parentId 父级id
     * @return 组织列表
     */
    List<OrganizationDTO> getOrganizationList(Long parentId);

    /**
     * 获取组织树集合
     *
     * @return 组织树dto列表
     */
    List<OrganizationTreeDTO> getTreeList();

    /**
     * 创建默认的组织
     *
     * @param tenantInfoDTO 租户dto对象
     * @return 租户id
     */
    Long createTenantDefaultOrganization(TenantInfoDTO tenantInfoDTO);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void removeOrganizationByTenantIds(Collection<Long> tenantIds);
}
