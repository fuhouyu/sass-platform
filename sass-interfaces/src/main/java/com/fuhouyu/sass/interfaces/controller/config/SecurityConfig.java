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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 * 配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 13:07
 */
@Configuration
public class SecurityConfig {

//     private final ClientAuthenticationManager clientAuthenticationManager;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**")
                                .permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilter(this.initBasicAuthenticationFilter())
                .build();
    }


//
//    /**
//     * 初始化基本认证过滤器
//     * Basic Base64(clientId:clientSecret).
//     *
//     * @return 基本认证过滤器
//     */
//    private BasicAuthenticationFilter initBasicAuthenticationFilter() {
//        return new BasicAuthenticationFilter(clientAuthenticationManager, (request, response, authException) -> {
//            ErrorResponse error = ResponseHelper.error(ResponseCodeEnum.NO_AUTH_CODE, authException.getMessage());
//            String body = ObjectMapperUtil.writeValueAsString(error);
//            try (ServletOutputStream outputStream = response.getOutputStream()) {
//                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
//                outputStream.flush();
//            }
//        });
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactory.createDelegatingPasswordEncoder("sm3");
//    }
}
