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
import com.fuhouyu.sass.platform.system.domain.entity.UserPositions;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户职位mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/11 18:21
 */
public interface UserPositionMapper extends BaseMapper<UserPositions> {


    /**
     * 批量插入用户职位
     *
     * @param list 用户职位列表
     */
    void insertBatch(@Param("list") List<UserPositions> list);

    /**
     * 通过用户id删除
     *
     * @param userIds 用户id删除
     */
    void deleteByUserIds(@Param("userIds") Collection<Long> userIds);

    /**
     * 通过用户id和组织id批量删除
     *
     * @param organizationId 组织id
     * @param userIds        用户id集合
     * @return 影响行数
     */
    Long deleteByOrganizationIdAndUserIds(@Param("organizationId") Long organizationId,
                                          @Param("userIds") Collection<Long> userIds);

    /**
     * 修改用户为非主职
     *
     * @param userId 用户id
     */
    void updateNotMainByUserId(@Param("userId") Long userId);

    /**
     * 通过租户id进行删除
     *
     * @param tenantIds 租户ids
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);
}
