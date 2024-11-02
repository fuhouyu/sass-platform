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
package com.fuhouyu.sass.platform.system.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 账号id实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 19:05
 */
@Getter
@ToString
@RequiredArgsConstructor
public class AccountIdDTO {

    private static final String CONCAT_SEPARATOR = ":";

    private final String account;

    private final String accountType;

    /**
     * account:accountType
     */
    private final String fullAccount;

    public AccountIdDTO(String account, String accountType) {
        this.account = account;
        this.accountType = accountType;
        this.fullAccount = account + CONCAT_SEPARATOR + accountType;
    }

    /**
     * 解析账号信息
     *
     * @param fullAccount 账号信息 account:accountType
     * @return 账号dto对象
     */
    public static AccountIdDTO parseFullAccount(@NonNull String fullAccount) {
        String[] accountAndType = fullAccount.split(CONCAT_SEPARATOR);
        if (accountAndType.length != 2) {
            throw new IllegalArgumentException(String.format("账号不正确: %s", fullAccount));
        }
        return new AccountIdDTO(accountAndType[0], accountAndType[1]);
    }
}
