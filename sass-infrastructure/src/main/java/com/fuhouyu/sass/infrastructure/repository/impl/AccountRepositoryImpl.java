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
package com.fuhouyu.sass.infrastructure.repository.impl;

import com.fuhouyu.sass.common.PageQuery;
import com.fuhouyu.sass.common.PageResult;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import com.fuhouyu.sass.infrastructure.repository.assembler.AccountAssembler;
import com.fuhouyu.sass.infrastructure.repository.mapper.AccountMapper;
import com.fuhouyu.sass.infrastructure.repository.orm.AccountDO;
import com.fuhouyu.sass.infrastructure.repository.orm.AccountId;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 账号存储层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 23:45
 */
@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountMapper accountMapper;

    private static final AccountAssembler ACCOUNT_ASSEMBLER = AccountAssembler.INSTANCE;
    

    @Override
    public void save(List<AccountEntity> accounts) {
        this.accountMapper.insertBatch(ACCOUNT_ASSEMBLER.toDO(accounts));
    }

    @Override
    public List<AccountEntity> findByUserId(Long userId) {
        List<AccountDO> accountDOList = this.accountMapper.queryByUserId(userId);
        return ACCOUNT_ASSEMBLER.toEntity(accountDOList);
    }

    @Override
    public AccountEntity findById(AccountIdEntity accountIdEntity) {
        AccountId accountId = new AccountId(accountIdEntity.getAccount(), accountIdEntity.getAccountType());
        return ACCOUNT_ASSEMBLER.toEntity(this.accountMapper.queryById(accountId));
    }

    @Override
    public int removeById(AccountIdEntity accountIdEntity) {
        AccountId accountId = new AccountId(accountIdEntity.getAccount(), accountIdEntity.getAccountType());
        return this.accountMapper.deleteById(accountId);
    }

    @Override
    public void save(AccountEntity entity) {
        AccountDO accountDO = ACCOUNT_ASSEMBLER.toDO(entity);
        this.accountMapper.insert(accountDO);
    }

    @Override
    public void edit(AccountEntity entity) {
        AccountDO accountDO = ACCOUNT_ASSEMBLER.toDO(entity);
        this.accountMapper.update(accountDO);
    }

    @Override
    public <P extends PageQuery> PageResult<AccountEntity> pageList(P pageable) {
        try (Page<Object> result = PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<AccountDO> accountDOList = this.accountMapper.queryList(pageable);
            List<AccountEntity> entityList = ACCOUNT_ASSEMBLER.toEntity(accountDOList);
            return new PageResult<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), entityList);
        }
    }
}
