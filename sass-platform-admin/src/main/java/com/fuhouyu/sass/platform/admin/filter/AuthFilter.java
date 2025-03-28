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
package com.fuhouyu.sass.platform.admin.filter;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.web.Ip2RegionTemplate;
import com.fuhouyu.framework.web.handler.ParseHttpRequest;
import com.fuhouyu.framework.web.model.Ip2Region;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

import static com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant.USER_ADDITIONAL_INFORMATION_PERMISSIONS;

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
public class AuthFilter implements ParseHttpRequest {

    private static final String BEARER_TOKEN_HEADER = OAuth2AccessToken.TokenType.BEARER.getValue();

    private final TokenStore tokenStore;

    private final Ip2RegionTemplate ip2RegionTemplate;

    @Override
    public User parseUser(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                          @NonNull Object handler) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (this.checkNoAuth(handler)) {
            return this.parseUser(bearerToken, false);
        }
        if (request.getRequestURI().startsWith("/v3/api-docs")) {
            return null;
        }
        return this.parseUser(bearerToken, true);
    }

    @Override
    public Request parseRequest(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        Request parseRequest = ParseHttpRequest.super.parseRequest(request, response);
        Ip2Region ip2Region = ip2RegionTemplate.searchIp(parseRequest.getRequestIp());
        String location = String.format("%s/%s/%s", ip2Region.getCountry(), ip2Region.getProvince(), ip2Region.getCity());
        parseRequest.putAdditionalInformation(HttpRequestAdditionalConstant.IP_LOCATION_ADDITIONAL_INFORMATION,
                location);
        return parseRequest;
    }

    /**
     * 解析用户信息
     *
     * @param bearerToken token
     * @return 退出
     */
    private UserEntity parseUser(String bearerToken, boolean isThrowException) {
        if (Objects.isNull(bearerToken) || bearerToken.isEmpty()) {
            if (isThrowException) {
                throw new ServiceException(ResponseStatusEnum.TOKEN_EXPIRE);
            }
            return null;
        }
        String token = bearerToken.substring(BEARER_TOKEN_HEADER.length()).trim();
        Authentication authentication = this.tokenStore.readAuthentication(token);
        if (Objects.isNull(authentication)) {
            if (isThrowException) {
                throw new ServiceException(ResponseStatusEnum.TOKEN_EXPIRE);
            }
            return null;
        }
        UserEntity userEntity = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(authentication.getDetails(),
                UserEntity.class));
        userEntity.putAdditionalInformation(USER_ADDITIONAL_INFORMATION_PERMISSIONS, authentication.getAuthorities());
        return userEntity;
    }


    /**
     * 检查是否是noAuth
     *
     * @param handler 处理器
     * @return true 有noAuth注解 false无注解
     */
    private boolean checkNoAuth(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return Objects.nonNull(handlerMethod.getMethodAnnotation(NoAuth.class)) || handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class);
        }
        return false;
    }
}
