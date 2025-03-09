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
package com.fuhouyu.sass.platform.system.core.security.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.system.dto.wechat.WechatAppletSessionDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
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

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 微信小程序
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 21:31
 */
@RequiredArgsConstructor
@Component
public class WechatAppletAuthenticationProvider implements AuthenticationProvider {

    private final WechatAppletService wechatAppletService;

    private final ExtensionUserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        WechatAppletSessionDTO wechatAppletSessionDTO = this.wechatAppletService.code2Session(code);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(wechatAppletSessionDTO.getOpenid(), AccountTypeEnum.WECHAT_APPLET.name());
        if (Objects.isNull(userDetails)) {
            // TODO 用户不存在，需要处理保存逻辑
            throw new ServiceException(ResponseStatusEnum.NOT_AUTH, "当前用户不存在");
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAppletAuthenticationProvider.WechatAppletAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @EqualsAndHashCode(callSuper = true)
    public static class WechatAppletAuthenticationToken extends AbstractAuthenticationToken {

        private final String code;

        /**
         * 构造函数
         *
         * @param code 授权码
         */
        @JsonCreator
        public WechatAppletAuthenticationToken(
                @JsonProperty("code") String code) {
            super(List.of());
            this.code = code;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return this.code;
        }
    }
}
