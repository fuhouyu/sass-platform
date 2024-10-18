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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.web.entity.RequestEntity;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.domain.model.user.UserAccountEntity;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import com.fuhouyu.sass.domain.repository.UserRepository;
import com.fuhouyu.sass.domain.service.impl.SecurityApplicationImpl;
import com.fuhouyu.sass.domain.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static org.mockito.Mockito.*;


/**
 * <p>
 * 用户账号测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 22:02
 */
@SpringBootTest(classes = SecurityApplicationImpl.class)
class TestUserAccountService extends TestBaseService {


    @MockBean
    protected UserRepository userRepository;

    @Autowired
    private UserAccountServiceImpl userAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    protected AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        DefaultListableContextFactory context = new DefaultListableContextFactory();
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestIp("127.0.0.1");
        context.setRequest(requestEntity);
        ContextHolderStrategy.setContext(context);
    }

    @Test
    void testRegister() {
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setUsername("username");
        userAccountEntity.setEmail("email");

        AccountIdEntity accountIdEntity = new AccountIdEntity("admin", "password");
        AccountEntity accountEntity = new AccountEntity(accountIdEntity);

        userAccountEntity.addAccount(accountEntity);
        accountEntity.enabled();

        doAnswer((res) -> {
            userAccountEntity.setId(1L);
            return userAccountEntity;
        }).when(this.userRepository).save(userAccountEntity);

        this.userAccountService.register(userAccountEntity);
        Assertions.assertEquals(1L, (long) userAccountEntity.getId(), "Register failed");
        // 使用空账号注册
        userAccountEntity.getAccounts().clear();
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            this.userAccountService.register(userAccountEntity);
        });
        // 使注册账号时抛出异常
        userAccountEntity.addAccount(accountEntity);
        doThrow(IllegalArgumentException.class).when(this.accountRepository).save(userAccountEntity.getAccounts());
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> this.userAccountService.register(userAccountEntity));
    }

    @Test
    void testLogin() throws Exception {
        AccountIdEntity accountIdEntity = new AccountIdEntity("admin", "password");

        AccountEntity accountEntity = new AccountEntity(accountIdEntity);
        accountEntity.setCredentials("admin");
        accountEntity.enabled();
        accountEntity.attachUser(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("admin");
        userEntity.setGender("male");

        AccountEntity returnAccountEntity = new AccountEntity(accountIdEntity);
        BeanUtils.copyProperties(accountEntity, returnAccountEntity);
        returnAccountEntity.setCredentials(passwordEncoder.encode("admin"));
        returnAccountEntity.attachUser(1L);

        when(this.accountRepository.findById(argThat(argument -> Objects.equals(argument.getAccount(), accountIdEntity.getAccount())
                && Objects.equals(argument.getAccountType(), accountIdEntity.getAccountType()))))
                .thenReturn(returnAccountEntity);

        when(this.userRepository.findById(1L))
                .thenReturn(userEntity);

        TokenValueEntity tokenValueEntity = this.userAccountService.login(accountEntity);
        Assertions.assertNotNull(tokenValueEntity, "Login failed");

    }

}
