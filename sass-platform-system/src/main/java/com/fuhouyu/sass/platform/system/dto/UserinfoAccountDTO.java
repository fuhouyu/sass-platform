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
package com.fuhouyu.sass.platform.system.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户和账号的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 21:37
 */
@Setter
@Getter
@ToString(callSuper = true)
public class UserinfoAccountDTO extends UserinfoDTO {

    /**
     * 用户关联的账号信息
     */
    @Setter(AccessLevel.PRIVATE)
    private List<AccountDTO> accounts;


    /**
     * 添加用户的账号
     *
     * @param account 用户账号
     */
    public void addAccount(AccountDTO account) {
        if (Objects.isNull(account)) {
            return;
        }
        if (Objects.isNull(accounts)) {
            this.accounts = new ArrayList<>();
        }
        this.accounts.add(account);
    }

    /**
     * 添加所有的账号
     *
     * @param accounts 账号信息
     */
    public void addAccounts(List<AccountDTO> accounts) {
        if (CollectionUtils.isEmpty(accounts)) {
            return;
        }
        if (Objects.isNull(this.accounts)) {
            this.accounts = new ArrayList<>(accounts.size());
        }
        this.accounts.addAll(accounts);
    }
}