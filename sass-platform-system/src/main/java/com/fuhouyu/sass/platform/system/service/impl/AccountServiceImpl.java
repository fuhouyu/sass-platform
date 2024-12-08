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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.sass.platform.system.assembler.AccountsAssembler;
import com.fuhouyu.sass.platform.system.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.AccountId;
import com.fuhouyu.sass.platform.system.entity.AccountIdDTO;
import com.fuhouyu.sass.platform.system.entity.Accounts;
import com.fuhouyu.sass.platform.system.mapper.AccountMapper;
import com.fuhouyu.sass.platform.system.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 账号实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 22:28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final AccountsAssembler ACCOUNT_ASSEMBLER = AccountsAssembler.INSTANCE;

    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountIdDTO save(AccountDTO accountDTO) {
        accountDTO.setCredentials(passwordEncoder.encode(accountDTO.getCredentials()));
        accountDTO.setIsEnabled(true);
        this.accountMapper.insert(ACCOUNT_ASSEMBLER.toEntity(accountDTO));
        return new AccountIdDTO(accountDTO.getAccount(), accountDTO.getAccountType());
    }

    @Override
    public void saveBatch(List<AccountDTO> saveList) {
        this.accountMapper.insertBatch(ACCOUNT_ASSEMBLER.toEntity(saveList));
    }

    @Override
    public void edit(AccountDTO accountDTO) {
        this.accountMapper.update(ACCOUNT_ASSEMBLER.toEntity(accountDTO));
    }

    @Override
    public int removeById(AccountIdDTO accountIdDTO) {
        return this.accountMapper.deleteById(new AccountId(accountIdDTO.account(), accountIdDTO.accountType()));
    }

    @Override
    public int removeByIds(Collection<AccountIdDTO> accountIdList) {
        List<AccountId> ids = accountIdList.stream().map(account -> new AccountId(account.account(), account.accountType()))
                .toList();
        return this.accountMapper.deleteByIds(ids);
    }

    @Override
    public AccountDTO findById(AccountIdDTO accountIdDTO) {
        Accounts accounts = this.accountMapper.queryById(new AccountId(accountIdDTO.account(), accountIdDTO.accountType()));
        if (Objects.isNull(accounts)) {
            return null;
        }
        AccountDTO dto = ACCOUNT_ASSEMBLER.toDTO(accounts);
        dto.setUserId(accounts.getUserId());
        return dto;
    }

    @Override
    public List<AccountDTO> findByUserId(Long userId) {
        return ACCOUNT_ASSEMBLER.toDTO(this.accountMapper.queryByUserId(userId));
    }

    @Override
    public void removeByUserIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        this.accountMapper.deleteByUserIds(userIds);
    }

    @Override
    public Function<PageQueryDTO, List<AccountDTO>> getPageResult() {
        return p -> ACCOUNT_ASSEMBLER.toDTO(this.accountMapper.queryList(p));
    }
}
