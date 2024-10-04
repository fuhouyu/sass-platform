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
package com.fuhouyu.sass.infrastructure.repository;

import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户存储层测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/28 13:13
 */
class TestAccountRepository extends TestBaseRepository {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testInsertAccount() {
        AccountEntity accountEntity = this.getAccountEntity();
        this.accountRepository.save(accountEntity);
        Assertions.assertNotNull(accountEntity);
    }

    @Test
    void testInsertBatchAccount() {
        List<AccountEntity> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(this.getAccountEntity());
        }
        this.accountRepository.save(list);
    }

    @Test
    void testUpdateAccount() {
        AccountEntity accountEntity = this.getAccountEntity();
        this.accountRepository.save(accountEntity);

        accountEntity.setRefAccountId("testUpdate");
        accountEntity.setCredentials("testUpdate");
        this.accountRepository.edit(accountEntity);
        Assertions.assertEquals("testUpdate", accountEntity.getRefAccountId(), "参数修改的返回结果有误");
    }


    @Test
    void testQueryAccount() {
        AccountEntity accountEntity = this.getAccountEntity();
        this.accountRepository.save(accountEntity);

        AccountEntity queryById = this.accountRepository.findById(accountEntity.getAccountIdEntity());
        Assertions.assertEquals(accountEntity.getAccountIdEntity(), queryById.getAccountIdEntity(), "查询出的结果不一致");

        AccountEntity isNull = this.accountRepository.findById(new AccountIdEntity("noExist", "noExist"));
        Assertions.assertNull(isNull, "查询到错误的结果");

        // 单次查询
        List<AccountEntity> queryByUserId = this.accountRepository.findByUserId(accountEntity.getUserId());
        Assertions.assertNotNull(queryByUserId, "通过用户id查询出的结果不一致");
    }


    private AccountEntity getAccountEntity() {
        AccountIdEntity accountIdEntity = new AccountIdEntity(super.getUUIDStr(8), super.getUUIDStr(10));
        AccountEntity accountEntity = new AccountEntity(accountIdEntity);
        accountEntity.attachUser(1L);
        accountEntity.enabled();
        accountEntity.setCredentials("test");
        accountEntity.setCredentialsExpirationTime(LocalDateTime.now().plusDays(99));
        return accountEntity;
    }
}
