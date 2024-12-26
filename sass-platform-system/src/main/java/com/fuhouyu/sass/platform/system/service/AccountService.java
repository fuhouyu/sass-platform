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
package com.fuhouyu.sass.platform.system.service;

import com.fuhouyu.sass.platform.system.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.dto.account.AccountIdDTO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 账号接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 22:25
 */
public interface AccountService extends BaseService<AccountIdDTO, AccountDTO> {

    /**
     * 通过用户id查询账号
     *
     * @param userId 用户id
     * @return 账号dto对象列表
     */
    List<AccountDTO> findByUserId(Long userId);

    /**
     * 通过用户id进行删除
     *
     * @param userIds 用户id
     */
    void removeByUserIds(Collection<Long> userIds);

    /**
     * 通过用户id查询关联的第三方账号信息
     *
     * @param userId 用户id
     * @return 第三方账号信息
     */
    List<AccountDTO> getAccountListForMe(Long userId);

    /**
     * 绑定第三方账号
     *
     * @param accountIdDTO 账号dto对象
     */
    void saveThirdPartyAccount(AccountIdDTO accountIdDTO);
}
