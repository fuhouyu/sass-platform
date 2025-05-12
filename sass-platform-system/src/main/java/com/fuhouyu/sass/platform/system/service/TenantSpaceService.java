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
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantSpaceDetailDTO;
import com.fuhouyu.sass.platform.system.domain.entity.TenantSpace;

import java.util.Collection;

/**
 * <p>
 * 租户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:57
 */
public interface TenantSpaceService extends IService<TenantSpace> {


    /**
     * 保存租户空间
     *
     * @param tenantSpaceDTO 租户空间dto对象
     */
    void saveTenantSpace(TenantSpaceDTO tenantSpaceDTO);


    /**
     * 更新租户空间
     *
     * @param tenantSpaceDTO 租户空间dto对象
     */
    void editTenantSpace(TenantSpaceDTO tenantSpaceDTO);


    /**
     * 通过租户id查询租户空间dto元旦
     *
     * @param tenantId 租户id
     * @return 租户空间dto对象
     */
    TenantSpaceDTO findByTenantId(Long tenantId);


    /**
     * 检查租户空间是否存在，存在则返回，否则抛出异常
     *
     * @param tenantId 租户id
     * @return 租户空间dto对象
     */
    TenantSpaceDTO checkExists(Long tenantId);

    /**
     * 检查当前租户空间是否存在
     *
     * @param spaceName 桶名称
     * @return true 已存在 false不存在
     */
    Boolean checkNameExists(String spaceName);

    /**
     * 删除租户空间
     *
     * @param tenantIds 租户ids
     */
    void removeSpaceByTenantIds(Collection<Long> tenantIds);

    /**
     * 通过租户id查询详情dto对象
     *
     * @param tenantId 租户id
     * @return 租户空间详情dto对象
     */
    TenantSpaceDetailDTO findDetailByTenantId(Long tenantId);
}
