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
package com.fuhouyu.sass.platform.system.components.security.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.system.assembler.SecurityUserDetailAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.AuthenticationPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.wechat.WechatAppletSessionDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.UserTypeEnum;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.UserPasskeysService;
import com.fuhouyu.sass.platform.system.service.UserService;
import com.fuhouyu.sass.platform.system.service.WechatAppletService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 通行证密钥
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/20 21:31
 */
@RequiredArgsConstructor
@Component
public class PasskeyAuthenticationProvider implements AuthenticationProvider {

    private final UserPasskeysService userPasskeysService;

    private final ExtensionUserDetailsService userDetailsService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String authenticationJson = (String)authentication.getCredentials();
        AuthenticationPasskeyDTO authenticationPasskeyDTO = new AuthenticationPasskeyDTO();
        authenticationPasskeyDTO.setUsername(username);
        authenticationPasskeyDTO.setAuthentication(authenticationJson);
        this.userPasskeysService.verifyAuthentication(authenticationPasskeyDTO);
        // 获取登录信息
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username, AccountTypeEnum.PASSWORD.name());
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), List.of());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return PasskeyAuthenticationProvider.PasskeyAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @EqualsAndHashCode(callSuper = true)
    public static class PasskeyAuthenticationToken extends AbstractAuthenticationToken {

        private final String username;

        private final String authenticationResponseJson;

        /**
         * 构造函数
         *
         * @param username 用户名
         * @param authenticationResponseJson responseJson
         */
        @JsonCreator
        public PasskeyAuthenticationToken(
                @JsonProperty("username") String username,
                @JsonProperty("authenticationResponseJson") String authenticationResponseJson) {
            super(List.of());
            this.username = username;
            this.authenticationResponseJson = authenticationResponseJson;
        }

        @Override
        public Object getCredentials() {
            return this.authenticationResponseJson;
        }

        @Override
        public Object getPrincipal() {
            return this.username;
        }
    }
}
