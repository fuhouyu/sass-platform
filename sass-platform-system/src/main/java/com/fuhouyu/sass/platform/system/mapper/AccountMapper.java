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
import com.fuhouyu.sass.platform.system.domain.entity.AccountId;
import com.fuhouyu.sass.platform.system.domain.entity.Accounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 账号mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 23:46
 */
public interface AccountMapper extends BaseMapper<Accounts> {


    /**
     * 通过用户id查询出对应的账号
     *
     * @param userId 用户id
     * @return 账号集合
     */
    List<Accounts> queryByUserId(Long userId);

    /**
     * 通过用户id集合删除账号
     *
     * @param userIds 用户id集合
     */
    void deleteByUserIds(@Param("userIds") Collection<Long> userIds);

    /**
     * 通过用户id查询关联的第三方账号信息
     *
     * @param userId 用户id
     * @return 第三方账号信息
     */
    List<Accounts> queryAccountListForMe(@Param("userId") Long userId);

    /**
     * 通过用户id和账号类型查询账号
     *
     * @param userId      用户id
     * @param accountType 账号类型
     * @return 账号
     */
    Accounts queryAccountByUserIdAndType(@Param("userId") Long userId, @Param("accountType") String accountType);

    /**
     * 通过租户ids进行删除
     *
     * @param tenantIds 租户ids
     */
    void deleteByTenantIds(@Param("tenantIds") Collection<Long> tenantIds);

    /**
     * 通过用户id对账号进行启用或者禁用
     *
     * @param userId  用户id
     * @param enabled 启禁用状态
     */
    void updateAccountStatusByUserId(@Param("userId") Long userId, @Param("enabled") Boolean enabled);

    /**
     * 通过账号id查询账号信息
     *
     * @param accountId 账号id
     * @return 账号信息
     */
    Accounts queryById(AccountId accountId);

}
