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
package com.fuhouyu.sass.interfaces.controller.config;

import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.framework.response.ResponseHelper;
import com.fuhouyu.framework.response.RestResult;
import com.fuhouyu.framework.security.core.AbstractApplicationManager;
import com.fuhouyu.framework.utils.JacksonUtil;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * <p>
 * 配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 13:07
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AbstractApplicationManager clientAuthenticationManager;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**")
                                .permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(this.basicAuthenticationFilter())
                .build();
    }


    /**
     * 初始化基本认证过滤器
     * Basic Base64(clientId:clientSecret).
     *
     * @return 基本认证过滤器
     */
    private BasicAuthenticationFilter basicAuthenticationFilter() {
        return new BasicAuthenticationFilter(clientAuthenticationManager, (request, response, authException) -> {
            RestResult<Void> restResult = ResponseHelper.failed(ResponseCodeEnum.NOT_AUTH, authException.getMessage());
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(JacksonUtil.writeValueAsBytes(restResult));
                outputStream.flush();
            }
        });
    }
}
