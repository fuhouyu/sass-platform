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

import com.fuhouyu.framework.exception.WebServiceException;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.framework.utils.LoggerUtil;
import com.fuhouyu.tenant.domain.model.account.AccountEntity;
import com.fuhouyu.tenant.domain.model.account.AccountIdEntity;
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
import org.springframework.util.CollectionUtils;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
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
    public UserAccountEntity register(UserAccountEntity userAccountEntity) {
        // TODO 这里的用户名需要后期生成
        UserEntity registeredUserEntity = this.userRepository.save(userAccountEntity);
        List<AccountEntity> registeredAccountEntity =
                this.saveAccounts(userAccountEntity.getAccounts(), registeredUserEntity.getId());
        BeanUtils.copyProperties(registeredUserEntity, userAccountEntity);
        userAccountEntity.addAccounts(registeredAccountEntity);
        return userAccountEntity;
    }

    @Override
    public UserAccountEntity login(AccountIdEntity accountIdEntity) throws UserPrincipalNotFoundException {
        AccountEntity accountEntity = this.accountRepository.findById(accountIdEntity);
        if (Objects.isNull(accountEntity)) {
            throw new UserPrincipalNotFoundException(
                    String.format("%s 用户未找到", accountIdEntity.account()));
        }
        UserEntity userEntity = this.userRepository.findById(accountEntity.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new UserPrincipalNotFoundException(String.format("%s 账号关联的用户不存在", accountIdEntity.account()));
        }
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        BeanUtils.copyProperties(userEntity, userAccountEntity);
        userAccountEntity.setLoginAccount(accountEntity);
        return userAccountEntity;
    }


    /**
     * 保存账号列表
     *
     * @param accounts 账号列表
     * @param userId   用户id
     * @return 账号列表集合
     */
    private List<AccountEntity> saveAccounts(List<AccountEntity> accounts,
                                             Long userId) {
        if (CollectionUtils.isEmpty(accounts)) {
            throw new WebServiceException(ResponseCodeEnum.INVALID_PARAM,
                    "用户关联的账号为空，无法注册");
        }
        accounts.forEach(accountEntity -> accountEntity.attachUser(userId));
        try {
            return this.accountRepository.save(accounts);
        } catch (Exception e) {
            LoggerUtil.error(logger, "用户账号注册失败: {}", accounts, e);
            throw new WebServiceException(ResponseCodeEnum.SERVER_ERROR,
                    "用户注册失败");
        }
    }
}
