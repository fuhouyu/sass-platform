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
package com.fuhouyu.sass.interfaces.controller.filter;

import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.web.exception.WebServiceException;
import com.fuhouyu.framework.web.handler.UserParseHandler;
import com.fuhouyu.sass.interfaces.controller.constants.WebConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * 认证过滤器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 23:19
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class AuthFilter implements UserParseHandler {

    private static final String BEARER_TOKEN_HEADER = OAuth2AccessToken.TokenType.BEARER.getValue();

    private final TokenStore tokenStore;

    @Override
    public <T extends User> T parseUser(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Class<T> userSubClass) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (request.getRequestURI().equals(WebConstant.USER_CONTROLLER_PATH + "/login")
                || request.getRequestURI().startsWith("/v3/api-docs")) {
            return null;
        }
        if (Objects.isNull(bearerToken)) {
            throw new WebServiceException(ResponseCodeEnum.NOT_AUTH,
                    "用户登录状态已失效");
        }
        String token = bearerToken.substring(BEARER_TOKEN_HEADER.length()).trim();
        Authentication authentication = this.tokenStore.readAuthentication(token);
        if (Objects.isNull(authentication)) {
            throw new WebServiceException(ResponseCodeEnum.NOT_AUTH,
                    "用户登录状态已失效");
        }
        return JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(authentication.getPrincipal(), userSubClass));
    }

}
