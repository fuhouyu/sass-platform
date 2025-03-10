/*
 * Copyright 2024-2025 fuhouyu.
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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.system.assembler.AccountsAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.UpdatePasswordDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.welink.WeLinkLoginUserDTO;
import com.fuhouyu.sass.platform.system.domain.entity.AccountId;
import com.fuhouyu.sass.platform.system.domain.entity.Accounts;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.mapper.AccountMapper;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.WeLinkService;
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

    private final WeLinkService weLinkService;

    @Override
    public AccountIdDTO save(AccountDTO accountDTO) {
        if (Objects.nonNull(accountDTO.getCredentials())) {
            accountDTO.setCredentials(passwordEncoder.encode(accountDTO.getCredentials()));
        }
        accountDTO.setIsEnabled(true);
        this.accountMapper.insert(ACCOUNT_ASSEMBLER.toEntity(accountDTO));
        return new AccountIdDTO(accountDTO.getAccount(), AccountTypeEnum.valueOf(accountDTO.getAccountType()));
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
        return this.accountMapper.deleteById(new AccountId(accountIdDTO.getAccount(), accountIdDTO.getAccountType().name()));
    }

    @Override
    public int removeByIds(Collection<AccountIdDTO> accountIdList) {
        List<AccountId> ids = accountIdList.stream().map(account -> new AccountId(account.getAccount(), account.getAccountType().name()))
                .toList();
        return this.accountMapper.deleteByIds(ids);
    }

    @Override
    public AccountDTO findById(AccountIdDTO accountIdDTO) {
        Accounts accounts = this.accountMapper.queryById(new AccountId(accountIdDTO.getAccount(), accountIdDTO.getAccountType().name()));
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
    public List<AccountDTO> findAccountListForMe(Long userId) {
        List<Accounts> results = this.accountMapper.queryAccountListForMe(userId);
        return ACCOUNT_ASSEMBLER.toDTO(results);
    }

    @Override
    public Function<PageQueryDTO, List<AccountDTO>> getPageResult() {
        return p -> ACCOUNT_ASSEMBLER.toDTO(this.accountMapper.queryList(p));
    }

    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        if (!Objects.equals(updatePasswordDTO.getNewPassword(), updatePasswordDTO.getConfirmPassword())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "两次输入的密码不一致");
        }
        Accounts account = this.accountMapper.queryAccountByUserIdAndType(ContextHolderStrategy.getContext().getUser().getId(),
                AccountTypeEnum.PASSWORD.name());
        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), account.getCredentials())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "原始密码不正确");
        }
        account.setCredentials(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        this.accountMapper.update(account);
    }

    @Override
    public AccountDTO findAccountByUserIdAndType(Long userId, AccountTypeEnum accountTypeEnum) {
        return ACCOUNT_ASSEMBLER.toDTO(this.accountMapper.queryAccountByUserIdAndType(userId, accountTypeEnum.name()));
    }

    @Override
    public void saveThirdPartyAccount(AccountIdDTO accountIdDTO) {
        // TODO 目前这里只会有weLink，先临时处理，后面需要抽到accountTypeEnum中
        WeLinkLoginUserDTO weLinkLoginUserDTO = this.weLinkService.login(accountIdDTO.getAccount());
        Accounts accounts = new Accounts();
        accounts.setAccount(weLinkLoginUserDTO.getUserId());
        accounts.setAccountType(accountIdDTO.getAccountType().name());
        accounts.setUserId(ContextHolderStrategy.getContext().getUser().getId());
        accounts.setRefAccountId(weLinkLoginUserDTO.getUserId());
        accounts.setIsEnabled(true);
        this.accountMapper.insert(accounts);

    }
}
