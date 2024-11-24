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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.system.assembler.TokenAssembler;
import com.fuhouyu.sass.platform.system.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.dto.account.UserDetailsDTO;
import com.fuhouyu.sass.platform.system.dto.user.SaveUserDTO;
import com.fuhouyu.sass.platform.system.dto.user.UserLoginDTO;
import com.fuhouyu.sass.platform.system.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import com.fuhouyu.sass.platform.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final TokenAssembler TOKEN_ASSEMBLER = TokenAssembler.INSTANCE;

    private final AccountService accountService;

    private final UserService userService;

    private final TokenStore tokenStore;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(SaveUserDTO userDTO) {
        Long id = this.userService.save(userDTO);
        userDTO.setId(id);
        this.saveAccounts(userDTO);
    }

    @Override
    public UserTokenDTO login(UserLoginDTO userLoginDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(userLoginDTO.getAccountType().getAuthenticationToken(userLoginDTO));
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 {} 方式登录失败: {} ",
                    userLoginDTO.getIdentify(), userLoginDTO.getAccountType(), e.getMessage());
            throw new ServiceException(
                    ResponseStatusEnum.INVALID_PARAM,
                    "用户名或密码错误");
        }
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
        if (Objects.isNull(authentication.getDetails())) {
            ((UsernamePasswordAuthenticationToken) authentication)
                    .setDetails(this.userService.findById(userDetailsDTO.getUserId()));
        }
        UserTokenDTO userTokenDTO = TOKEN_ASSEMBLER.toUserTokenDTO(tokenStore.createToken(authentication));
        this.userService.recordLoginSuccess(userDetailsDTO.getUserId());
        return userTokenDTO;
    }

    @Override
    public void logout() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        String authorization = request.getAuthorization();
        String token = authorization.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
        this.tokenStore.removeAuth2Token(token);
    }

    /**
     * 保存账号列表
     *
     * @param saveUserDTO 用户详情dto
     */
    private void saveAccounts(SaveUserDTO saveUserDTO) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccount(saveUserDTO.getUsername());
        accountDTO.setAccountType(AccountTypeEnum.PASSWORD.name());
        accountDTO.setUserId(saveUserDTO.getId());
        accountDTO.setCredentials(passwordEncoder.encode(saveUserDTO.getPassword()));
        accountDTO.setIsEnabled(true);
        try {
            this.accountService.save(accountDTO);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户账号注册失败: {}", accountDTO, e);
            throw new IllegalArgumentException("用户注册失败");
        }
    }

}
