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

import com.fuhouyu.sass.platform.system.dto.user.admin.UserPositionDTO;

import java.util.Collection;

/**
 * <p>
 * 用户职位接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/11 12:24
 */
public interface UserPositionService {


    /**
     * 保存用户职位信息
     * @param userId 用户id
     * @param userPositionDTO 用户职位dto对象
     */
    void saveUserPosition(Long userId, UserPositionDTO userPositionDTO);

    /**
     * 通过用户id批量删除
     *
     * @param userIds 用户id
     */
    void removeByUserIds(Collection<Long> userIds);

    /**
     * 通过用户id和组织id批量删除
     *
     * @param organizationId 组织id
     * @param userIds        用户id集合
     * @return 影响行数
     */
    Long removeByOrganizationIdAndUserIds(Long organizationId,
                                          Collection<Long> userIds);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void removeByTenantIds(Collection<Long> tenantIds);
}
