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
import com.fuhouyu.sass.platform.system.entity.UserHasRole;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户角色mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/13 22:05
 */
public interface UserHasRoleMapper {

    /**
     * 插入用户角色
     *
     * @param userHasRole 用户角色对象
     */
    void insert(UserHasRole userHasRole);

    /**
     * 批量插入用户角色
     *
     * @param list 用户角色列表
     */
    void insertBatch(@Param("list") List<UserHasRole> list);

    /**
     * 通过用户id删除
     *
     * @param userIds 用户id删除
     */
    void deleteByUserIds(@Param("userIds") Collection<Long> userIds);

    /**
     * 通过用户id查询角色id
     *
     * @param userId 用户id
     * @return 角色id
     */
    @TenantQuery(column = "r.owner_tenant_id")
    List<Long> queryRoleIdsByUserId(@Param("userId") Long userId);
}
