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

import com.fuhouyu.sass.platform.system.domain.entity.TenantHasUser;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 * 租户和用户的mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/12 17:48
 */
public interface TenantHasUserMapper {

    /**
     * 批量保存租户和用户的关系
     *
     * @param tenantHasUser 租户和用户的关系
     */
    void insertBatch(@Param("list") Collection<TenantHasUser> tenantHasUser);

    /**
     * 保存租户和用户的关系
     *
     * @param tenantHasUser 租户和用户的关系
     */
    void insert(TenantHasUser tenantHasUser);

    /**
     * 删除租户和用户的关系
     *
     * @param tenantId 租户id
     * @param userIds  用户id集合
     */
    void deleteByTenantIdAndUserIds(@Param("tenantId") Long tenantId, @Param("userIds") Collection<Long> userIds);

    /**
     * 通过租户ids删除关联信息
     *
     * @param tenantIds 租户ids
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);
}
