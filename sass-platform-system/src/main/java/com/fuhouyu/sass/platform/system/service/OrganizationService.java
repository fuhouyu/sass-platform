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

import com.fuhouyu.sass.platform.system.dto.organization.OrganizationDTO;
import com.fuhouyu.sass.platform.system.dto.organization.OrganizationTreeDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;

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
public interface OrganizationService extends BaseService<Long, OrganizationDTO> {

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
     */
    void createTenantDefaultOrganization(TenantInfoDTO tenantInfoDTO);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void removeOrganizationByTenantIds(Collection<Long> tenantIds);
}
