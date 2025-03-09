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
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.system.constants.CacheConstant;
import com.fuhouyu.sass.platform.system.constants.HttpRequestHeaderConstant;
import com.fuhouyu.sass.platform.system.dto.welink.WeLinkLoginUserDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.WebResponseStatusEnum;
import com.fuhouyu.sass.platform.system.service.WeLinkService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
@ConditionalOnBean(WeLinkService.class)
public class WeLinkAuthenticationProvider implements AuthenticationProvider {

    private final WeLinkService weLinkService;

    private final ExtensionUserDetailsService userDetailsService;

    private final CacheService<String, Object> cacheService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        WeLinkLoginUserDTO weLinkLoginUserDTO = this.weLinkService.login(code);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(weLinkLoginUserDTO.getUserId(), AccountTypeEnum.WELINK.name());
        // 检查用户是否已绑定
        this.checkUserIsBind(userDetails, weLinkLoginUserDTO);
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeLinkAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 检查用户是否绑定
     *
     * @param userDetails        用户详情
     * @param weLinkLoginUserDTO weLink用户信息
     */
    private void checkUserIsBind(UserDetails userDetails, WeLinkLoginUserDTO weLinkLoginUserDTO) {
        String temporaryToken = UUID.randomUUID().toString().replace("-", "").substring(16).toUpperCase(Locale.ROOT);

        if (Objects.isNull(userDetails)) {
            HttpServletResponse response = ContextHolderStrategy.getContext().getRequest().getAdditionalInformation(HttpServletResponse.class);
            response.addHeader(HttpRequestHeaderConstant.USER_BIND_TOKEN, temporaryToken);
            response.addHeader(HttpRequestHeaderConstant.USER_BIND, "true");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, String.format("%s", HttpRequestHeaderConstant.USER_BIND_TOKEN));
            cacheService.set(CacheConstant.USER_BIND_TOKEN + temporaryToken, weLinkLoginUserDTO.getUserId(),
                    3, TimeUnit.MINUTES);
            throw new ServiceException(WebResponseStatusEnum.USER_NOT_BIND,
                    "当前WeLink用户未绑定账号，请先进行绑定账号后操作");
        }
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
