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

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.security.entity.TokenEntity;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.system.dto.AccountDTO;
import com.fuhouyu.sass.platform.system.dto.LoginAccountDTO;
import com.fuhouyu.sass.platform.system.dto.SecurityUserDetailDTO;
import com.fuhouyu.sass.platform.system.dto.UserAccountDTO;
import com.fuhouyu.sass.platform.system.entity.AccountIdDTO;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import com.fuhouyu.sass.platform.system.service.UserService;
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

    private final AccountService accountService;

    private final UserService userService;

    private final TokenStore tokenStore;

    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserAccountDTO userAccountDTO) {
        // TODO 这里的用户名需要后期生成
        this.userService.save(userAccountDTO);
        List<AccountDTO> accounts = userAccountDTO.getAccounts();
        this.saveAccounts(accounts, userAccountDTO.getId());
        userAccountDTO.addAccounts(accounts);
    }

    @Override
    public TokenEntity login(@NonNull LoginAccountDTO loginAccount) {
        AccountIdDTO accountIdDTO = new AccountIdDTO(loginAccount.getAccount(), loginAccount.getAccountType());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(accountIdDTO.getFullAccount(),
                        loginAccount.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // 设置用户上下文
        if (authenticate.getPrincipal() instanceof SecurityUserDetailDTO securityUserDetailEntity) {
            setContext(securityUserDetailEntity);
            this.userService.recordLoginSuccess(securityUserDetailEntity.getId());
        }
        return this.getAccessToken(authenticate);
    }

    @Override
    public void logout() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        String authorization = request.getAuthorization();
        String token = authorization.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
        this.tokenStore.removeTokenEntity(token);
    }

    /**
     * 保存账号列表
     *
     * @param accounts 账号列表
     * @param userId   用户id
     */
    private void saveAccounts(List<AccountDTO> accounts,
                              Long userId) {
        if (CollectionUtils.isEmpty(accounts)) {
            throw new IllegalArgumentException("account is empty");
        }
        accounts.forEach(account -> account.attachUser(userId));
        try {
            this.accountService.saveBatch(accounts);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户账号注册失败: {}", accounts, e);
            throw new IllegalArgumentException("用户注册失败");
        }
    }


    /**
     * 用户登录，获取token.
     *
     * @param authentication 认证详情
     * @return dto对象
     */
    private TokenEntity getAccessToken(Authentication authentication) {
        // 先固定
        int accessTokenValidity = 6000;
        int refreshTokenValidity = 6000;
        return tokenStore.createToken(authentication,
                accessTokenValidity,
                refreshTokenValidity);
    }

    /**
     * 设置上下文
     *
     * @param securityUserDetailDTO 上下dto对象
     */
    private void setContext(SecurityUserDetailDTO securityUserDetailDTO) {
        DefaultListableContextFactory defaultListableFactory = new DefaultListableContextFactory();
        UserEntity contextUserEntity = new UserEntity();
        contextUserEntity.setUsername(securityUserDetailDTO.getAccount().getAccount());
        contextUserEntity.setId(securityUserDetailDTO.getId());
        defaultListableFactory.setUser(contextUserEntity);
        if (Objects.nonNull(ContextHolderStrategy.getContext())) {
            defaultListableFactory.setRequest(ContextHolderStrategy.getContext().getRequest());
        }
        ContextHolderStrategy.setContext(defaultListableFactory);
    }

}
