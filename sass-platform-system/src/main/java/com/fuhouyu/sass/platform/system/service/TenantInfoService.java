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
import com.fuhouyu.sass.platform.system.domain.dto.tenant.*;
import com.fuhouyu.sass.platform.system.domain.entity.TenantInfo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户域的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:23
 */
public interface TenantInfoService extends IService<TenantInfo> {


    /**
     * 保存租户信息
     *
     * @param tenantInfoDTO 租户信息dto
     * @return id
     */
    long save(TenantInfoDTO tenantInfoDTO);

    /**
     * 编辑租户信息
     * @param tenantInfoDTO 租户信息dto
     */
    void edit(TenantInfoDTO tenantInfoDTO);

    /**
     * 通过租户编码获取租户
     *
     * @param tenantCode 租户编码
     * @return 租户dto对象
     */
    TenantInfoDTO findByTenantCode(String tenantCode);

    /**
     * 通过id查询租户信息
     *
     * @param id 主键id
     * @return 租户信息
     */
    TenantInfoDTO findById(Long id);

    /**
     * 通过id查询出租户的详情
     *
     * @param id 主键id
     * @return 租户详情
     */
    TenantInfoDetailDTO findDetailById(Long id);

    /**
     * 保存租户空间详情
     *
     * @param tenantInfoDTO 租户信息
     * @return id
     */
    long saveTenantDetail(SaveOrEditTenantInfoDTO tenantInfoDTO);

    /**
     * 修改租户空间详情
     *
     * @param tenantInfoDTO 租户详情dto
     */
    void editDetail(SaveOrEditTenantInfoDTO tenantInfoDTO);

    /**
     * 查询租户的列表
     *
     * @return 租户列表
     */
    List<BasicTenantDTO> findTenantList();

    /**
     * 重置密码
     *
     * @param id 主键id
     */
    void resetPassword(Long id);

    /**
     * 分页查询租户列表
     *
     * @param pageQueryDTO 分页查询参数
     * @return 分页结果
     */
    PageResultDTO<TenantInfoDTO> pageList(TenantPageQueryDTO pageQueryDTO);
}
