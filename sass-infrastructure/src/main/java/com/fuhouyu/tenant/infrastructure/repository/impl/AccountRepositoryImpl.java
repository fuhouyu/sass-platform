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
package com.fuhouyu.tenant.infrastructure.repository.impl;

import com.fuhouyu.tenant.common.PageQuery;
import com.fuhouyu.tenant.common.PageResult;
import com.fuhouyu.tenant.domain.model.account.AccountEntity;
import com.fuhouyu.tenant.domain.model.account.AccountIdEntity;
import com.fuhouyu.tenant.domain.repository.AccountRepository;
import com.fuhouyu.tenant.infrastructure.repository.assembler.AccountAssembler;
import com.fuhouyu.tenant.infrastructure.repository.mapper.AccountMapper;
import com.fuhouyu.tenant.infrastructure.repository.orm.AccountDO;
import com.fuhouyu.tenant.infrastructure.repository.orm.AccountId;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountMapper accountMapper;

    private final AccountAssembler accountAssembler;

    public AccountRepositoryImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.accountAssembler = AccountAssembler.INSTANCE;
    }

    @Override
    public List<AccountEntity> save(List<AccountEntity> accounts) {
        this.accountMapper.insertBatch(this.accountAssembler.toDO(accounts));
        return accounts;
    }

    @Override
    public AccountEntity findById(AccountIdEntity accountIdEntity) {
        AccountId accountId = new AccountId(accountIdEntity.account(), accountIdEntity.accountType());
        return this.accountAssembler.toEntity(this.accountMapper.queryById(accountId));
    }

    @Override
    public int removeById(AccountIdEntity accountIdEntity) {
        AccountId accountId = new AccountId(accountIdEntity.account(), accountIdEntity.accountType());
        return this.accountMapper.deleteById(accountId);
    }

    @Override
    public AccountEntity save(AccountEntity entity) {
        AccountDO accountDO = this.accountAssembler.toDO(entity);
        this.accountMapper.insert(accountDO);
        return entity;
    }

    @Override
    public AccountEntity edit(AccountEntity entity) {
        AccountDO accountDO = this.accountAssembler.toDO(entity);
        this.accountMapper.update(accountDO);
        return entity;
    }

    @Override
    public <P extends PageQuery> PageResult<AccountEntity> pageList(P pageable) {
        try (Page<Object> result = PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<AccountDO> accountDOList = this.accountMapper.queryList();
            List<AccountEntity> entityList = this.accountAssembler.toEntity(accountDOList);
            return new PageResult<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), entityList);
        }
    }
}
