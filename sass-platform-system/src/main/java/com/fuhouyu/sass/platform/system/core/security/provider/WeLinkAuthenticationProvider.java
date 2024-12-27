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
package com.fuhouyu.sass.platform.system.core.security.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.system.dto.welink.WeLinkLoginUserDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.service.WeLinkService;
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
 * weLink 账号提供者
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/22 18:10
 */
@RequiredArgsConstructor
@Component
public class WeLinkAuthenticationProvider implements AuthenticationProvider {

    private final WeLinkService weLinkService;

    private final ExtensionUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        WeLinkLoginUserDTO weLinkLoginUserDTO = this.weLinkService.login(code);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(weLinkLoginUserDTO.getUserId(), AccountTypeEnum.WELINK.name());
        if (Objects.isNull(userDetails)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前WeLink用户未绑定账号，请先进行绑定账号后操作");
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeLinkAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @EqualsAndHashCode(callSuper = true)
    public static class WeLinkAuthenticationToken extends AbstractAuthenticationToken {

        private final String code;

        /**
         * 构造函数
         *
         * @param code 授权码
         */
        @JsonCreator
        public WeLinkAuthenticationToken(
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
