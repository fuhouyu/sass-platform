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

import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDetailDTO;
import com.fuhouyu.sass.platform.system.domain.entity.TenantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 租户mapper接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 22:29
 */
public interface TenantInfoMapper extends BaseMapper<Long, TenantInfo> {

    /**
     * 通过租户编码进行查询
     *
     * @param tenantCode 租户编码
     * @return 租户do实体对象
     */
    TenantInfo queryByTenantCode(String tenantCode);

    /**
     * 通过用户id查询出租户
     *
     * @param userId 用户id
     * @return 租户集合
     */
    List<TenantInfo> queryByUserId(@Param("userId") Long userId);

    /**
     * count用户下的租户信息，用于判断用户是否存在于租户中
     *
     * @param userId   用户id
     * @param tenantId 租户id
     * @return 记录数
     */
    Integer existsUserTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    /**
     * 通过id查询出租户详情
     *
     * @param id 主键id
     * @return 详情dto对象
     */
    TenantInfoDetailDTO queryDetailById(@Param("id") Long id);

    /**
     * 查询所有
     *
     * @return 所有的租户列表
     */
    List<TenantInfo> queryAllList();
}
