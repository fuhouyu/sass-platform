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
package com.fuhouyu.tenant.domain.service;

import com.fuhouyu.framework.exception.WebServiceException;
import com.fuhouyu.tenant.domain.model.account.AccountEntity;
import com.fuhouyu.tenant.domain.model.account.AccountId;
import com.fuhouyu.tenant.domain.model.user.UserAccountEntity;
import com.fuhouyu.tenant.domain.model.user.UserEntity;
import com.fuhouyu.tenant.domain.repository.AccountRepository;
import com.fuhouyu.tenant.domain.repository.UserRepository;
import com.fuhouyu.tenant.domain.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import static org.mockito.Mockito.when;

/**
 * <p>
 * 用户账号测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 22:02
 */
class TestUserAccountService extends TestBaseService {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

    @InjectMocks
    private UserAccountServiceImpl userAccountService;


    @Test
    void testRegister() {
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setUsername("username");
        userAccountEntity.setEmail("email");

        AccountId accountId = new AccountId("admin", "password");
        AccountEntity accountEntity = new AccountEntity(accountId);

        userAccountEntity.addAccount(accountEntity);
        accountEntity.enabled();

        when(this.userRepository.save(userAccountEntity))
                .then((res) -> {
                    userAccountEntity.setId(1L);
                    return userAccountEntity;
                });
        UserAccountEntity register = this.userAccountService.register(userAccountEntity);
        Assertions.assertEquals(1L, (long) register.getId(), "Register failed");
        // 使用空账号注册
        userAccountEntity.getAccounts().clear();
        Assertions.assertThrowsExactly(WebServiceException.class, () -> {
            this.userAccountService.register(userAccountEntity);
        });
        // 使注册账号时抛出异常
        userAccountEntity.addAccount(accountEntity);
        when(this.accountRepository.save(userAccountEntity.getAccounts()))
                .thenThrow(WebServiceException.class);
        Assertions.assertThrowsExactly(WebServiceException.class, () -> {
            this.userAccountService.register(userAccountEntity);
        });
    }

    @Test
    void testLogin() throws UserPrincipalNotFoundException {
        AccountId accountId = new AccountId("admin", "password");

        AccountEntity accountEntity = new AccountEntity(accountId);
        accountEntity.enabled();
        accountEntity.attachUser(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("admin");
        userEntity.setGender("male");

        when(this.accountRepository.findById(accountId))
                .thenReturn(accountEntity);

        when(this.userRepository.findById(1L))
                .thenReturn(userEntity);
        UserAccountEntity userAccountEntity = this.userAccountService.login(accountId);
        Assertions.assertNotNull(userAccountEntity);

    }

}
