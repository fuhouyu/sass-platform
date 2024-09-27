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
package com.fuhouyu.tenant.domain.service.impl;

import com.fuhouyu.tenant.domain.model.account.AccountEntity;
import com.fuhouyu.tenant.domain.model.account.AccountId;
import com.fuhouyu.tenant.domain.model.user.UserAccountEntity;
import com.fuhouyu.tenant.domain.model.user.UserEntity;
import com.fuhouyu.tenant.domain.repository.AccountRepository;
import com.fuhouyu.tenant.domain.repository.UserRepository;
import com.fuhouyu.tenant.domain.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

/**
 * <p>
 * 账号接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 18:12
 */
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public UserAccountEntity login(AccountId accountId) throws UserPrincipalNotFoundException {
        AccountEntity accountEntity = this.accountRepository.findById(accountId);
        if (Objects.isNull(accountEntity)) {
            throw new UserPrincipalNotFoundException(
                    String.format("%s 用户未找到", accountId.account()));
        }
        UserEntity userEntity = this.userRepository.findById(accountEntity.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new UserPrincipalNotFoundException(String.format("%s 账号关联的用户不存在", accountId.account()));
        }
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        BeanUtils.copyProperties(userEntity, userAccountEntity);
        userAccountEntity.setLoginAccount(accountEntity);
        return userAccountEntity;
    }

}
