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
package com.fuhouyu.sass.platform.system;

import com.fuhouyu.sass.platform.system.dto.AccountDTO;
import com.fuhouyu.sass.platform.system.entity.AccountIdDTO;
import com.fuhouyu.sass.platform.system.service.AccountService;
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
class TestAccountService extends TestBaseService {

    @Autowired
    private AccountService accountService;

    @Test
    void testInsertAccount() {
        AccountDTO accountDTO = this.getAccountDTO();
        accountService.save(accountDTO);
        Assertions.assertNotNull(accountDTO);
    }

    @Test
    void testInsertBatchAccount() {
        List<AccountDTO> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(this.getAccountDTO());
        }
        this.accountService.saveBatch(list);
        Assertions.assertNotNull(list);
    }

    @Test
    void testUpdateAccount() {
        AccountDTO accountDTO = this.getAccountDTO();
        this.accountService.save(accountDTO);

        accountDTO.setRefAccountId("testUpdate");
        accountDTO.setCredentials("testUpdate");
        this.accountService.edit(accountDTO);
        Assertions.assertEquals("testUpdate", accountDTO.getRefAccountId(), "参数修改的返回结果有误");

        accountDTO.setRefAccountId("testUpdate");
        accountDTO.setCredentials("testUpdate");
        this.accountService.edit(accountDTO);
        Assertions.assertEquals("testUpdate", accountDTO.getRefAccountId(), "参数修改的返回结果有误");
    }


    @Test
    void testQueryAccount() {
        AccountDTO accountDTO = this.getAccountDTO();
        this.accountService.save(accountDTO);

        AccountDTO queryById = this.accountService.findById(new AccountIdDTO(accountDTO.getAccount(), accountDTO.getAccountType()));
        Assertions.assertEquals(accountDTO.getAccount(), queryById.getAccount(), "查询出的结果不一致");

        AccountDTO isNull = this.accountService.findById(new AccountIdDTO("noExist", "noExist"));
        Assertions.assertNull(isNull, "查询到错误的结果");

        // 单次查询
        List<AccountDTO> queryByUserId = this.accountService.findByUserId(accountDTO.getUserId());
        Assertions.assertNotNull(queryByUserId, "通过用户id查询出的结果不一致");
    }


    private AccountDTO getAccountDTO() {
        AccountDTO accountEntity = new AccountDTO();
        accountEntity.setAccount(super.getUUIDStr(8));
        accountEntity.setAccountType(super.getUUIDStr(10));
        accountEntity.attachUser(1L);
        accountEntity.enabled();
        accountEntity.setCredentials("test");
        accountEntity.setCredentialsExpirationTime(LocalDateTime.now().plusDays(99));
        return accountEntity;
    }
}
