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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.Request;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.utils.LoggerUtil;
import com.fuhouyu.sass.domain.assembler.TokenValueAssembler;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.domain.model.user.SecurityUserDetailEntity;
import com.fuhouyu.sass.domain.model.user.UserAccountEntity;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import com.fuhouyu.sass.domain.repository.UserRepository;
import com.fuhouyu.sass.domain.service.UserAccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {


    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private static final TokenValueAssembler TOKEN_VALUE_ASSEMBLER = TokenValueAssembler.INSTANCE;

    private final TokenStore tokenStore;

    private final AuthenticationManager authenticationManager;


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
    public TokenValueEntity login(@NonNull AccountEntity account) {
        AccountIdEntity accountIdEntity = account.getIdentifierId();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(accountIdEntity.getFullAccount(), account.getCredentials());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // 设置用户上下文
        if (authenticate.getPrincipal() instanceof SecurityUserDetailEntity securityUserDetailEntity) {
            setContext(securityUserDetailEntity);
        }
        UserEntity userEntity = UserEntity.generateLoginUserEntity(account.getUserId());
        this.userRepository.edit(userEntity);
        return this.getAccessToken(authenticate);
    }

    @Override
    public void logout() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        String authorization = request.getAuthorization();
        String token = authorization.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
        this.tokenStore.readTokenEntity(token);
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
            LoggerUtil.error(log, "用户账号注册失败: {}", accounts, e);
            throw new IllegalArgumentException("user register failed");
        }
    }


    /**
     * 用户登录，获取token.
     *
     * @param authentication 认证详情
     * @return dto对象
     */
    private TokenValueEntity getAccessToken(Authentication authentication) {
        // 先固定
        int accessTokenValidity = 6000;
        int refreshTokenValidity = 6000;
        return TOKEN_VALUE_ASSEMBLER.toTokenValueEntity(tokenStore.createToken(authentication,
                accessTokenValidity,
                refreshTokenValidity));
    }

    /**
     * 设置上下文
     *
     * @param securityUserDetailEntity 上下文entity
     */
    private void setContext(SecurityUserDetailEntity securityUserDetailEntity) {
        DefaultListableContextFactory defaultListableFactory = new DefaultListableContextFactory();
        com.fuhouyu.framework.web.entity.UserEntity contextUserEntity = new com.fuhouyu.framework.web.entity.UserEntity();
        contextUserEntity.setUsername(securityUserDetailEntity.getAccount().getIdentifierId().getAccount());
        contextUserEntity.setId(securityUserDetailEntity.getId());
        defaultListableFactory.setUser(contextUserEntity);
        if (Objects.nonNull(ContextHolderStrategy.getContext())) {
            defaultListableFactory.setRequest(ContextHolderStrategy.getContext().getRequest());
        }
        ContextHolderStrategy.setContext(defaultListableFactory);
    }
}
