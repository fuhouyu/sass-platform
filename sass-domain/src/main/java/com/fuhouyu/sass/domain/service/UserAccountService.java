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
package com.fuhouyu.sass.domain.service;

import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.domain.model.user.UserAccountEntity;
import lombok.NonNull;

/**
 * <p>
 * 账号接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 18:10
 */
public interface UserAccountService {

    /**
     * 注册用户
     *
     * @param userAccountEntity 用户账号实体
     */
    void register(UserAccountEntity userAccountEntity);

    /**
     * 通过账号id进行登录
     *
     * @param account 账号实体
     * @return token实体
     */
    TokenValueEntity login(@NonNull AccountEntity account) throws Exception;
}
