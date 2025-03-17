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
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.entity.AdminUsers;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户mapper接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/24 20:26
 */
public interface AdminUserMapper extends BaseMapper<Long, AdminUsers> {


    @Override
    @TenantQuery
    int update(AdminUsers adminUsers);

    /**
     * 通过用户名称查询
     *
     * @param username 用户名称
     * @return userEntity对象
     */
    AdminUsers queryByUsername(String username);

    /**
     * 记录用户登录信息
     *
     * @param userId    用户id
     * @param loginIp   登录ip
     * @param loginTime 登录时间
     */
    @TenantQuery
    void recordLoginSuccess(@Param("userId") Long userId,
                            @Param("loginIp") String loginIp,
                            @Param("loginTime") LocalDateTime loginTime);


    @Override
    @TenantQuery
    <P extends PageQueryDTO> List<AdminUsers> queryList(P pageQuery);

    /**
     * 查询用户详情列表
     *
     * @param pageQuery 分页查询对象
     * @return 用户详情列表
     */
    @TenantQuery
    <P extends PageQueryDTO> List<AdminUserDTO> queryDetailList(@Param("pageQuery") P pageQuery);

    /**
     * 查询用户详情
     *
     * @param id 用户id
     * @return 用户详情
     */
    @TenantQuery
    AdminUserDetailDTO queryDetailById(Long id);
}
