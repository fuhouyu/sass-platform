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
package com.fuhouyu.sass.domain.service.impl;

import com.fuhouyu.framework.security.service.UserAuthService;
import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import com.fuhouyu.framework.utils.LoggerUtil;
import com.fuhouyu.sass.domain.assembler.TokenValueAssembler;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.domain.model.user.UserAccountEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import com.fuhouyu.sass.domain.repository.UserRepository;
import com.fuhouyu.sass.domain.service.UserAccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    private static final TokenValueAssembler TOKEN_VALUE_ASSEMBLER = TokenValueAssembler.INSTANCE;
    private final UserAuthService userAuthService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserAccountEntity userAccountEntity) {
        // TODO 这里的用户名需要后期生成
        this.userRepository.save(userAccountEntity);
        List<AccountEntity> accounts = userAccountEntity.getAccounts();
        this.saveAccounts(accounts, userAccountEntity.getId());
        userAccountEntity.addAccounts(accounts);
    }

    @Override
    public TokenValueEntity login(@NonNull AccountEntity account) throws Exception {
        AccountIdEntity accountIdEntity = account.getIdentifierId();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(accountIdEntity.getFullAccount(), account.getCredentials());
        DefaultOAuth2Token defaultOAuth2Token = userAuthService.generatorToken(usernamePasswordAuthenticationToken);
        return TOKEN_VALUE_ASSEMBLER.toTokenValueEntity(defaultOAuth2Token);
    }


    /**
     * 保存账号列表
     *
     * @param accounts 账号列表
     * @param userId   用户id
     */
    private void saveAccounts(List<AccountEntity> accounts,
                              Long userId) {
        if (CollectionUtils.isEmpty(accounts)) {
            throw new IllegalArgumentException("account is empty");
        }
        accounts.forEach(accountEntity -> accountEntity.attachUser(userId));
        try {
            this.accountRepository.save(accounts);
        } catch (Exception e) {
            LoggerUtil.error(logger, "用户账号注册失败: {}", accounts, e);
            throw new IllegalArgumentException("user register failed");
        }
    }

}
