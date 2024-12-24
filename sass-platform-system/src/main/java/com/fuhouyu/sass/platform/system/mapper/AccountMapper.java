/*
 * Copyright 2024-2024 the original author or authors.
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


import com.fuhouyu.sass.platform.system.entity.AccountId;
import com.fuhouyu.sass.platform.system.entity.Accounts;
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
public interface AccountMapper extends BaseMapper<AccountId, Accounts> {


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
     * 通过用户id查询第三方账号
     *
     * @param userId 用户id
     * @return 第三方账号信息
     */
    List<Accounts> getThirdPartyAccountByUserId(@Param("userId") Long userId);
}
