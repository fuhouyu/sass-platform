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
package com.fuhouyu.sass.platform.system.core.security;

import com.fuhouyu.sass.platform.system.domain.dto.user.LoginUserDetailDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户登录认证
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/23 18:26
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserAccountAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private LoginUserDetailDTO loginUserDetails;

    public UserAccountAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserAccountAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * 创建认证对象
     *
     * @param principal   主体
     * @param credentials 凭证
     * @param authorities 权限
     * @return 认证对象
     */
    public static UserAccountAuthenticationToken authenticated(Object principal, Object credentials,
                                                               Object userDetails,
                                                               LoginUserDetailDTO loginUserDetailDTO,
                                                               Collection<? extends GrantedAuthority> authorities) {
        UserAccountAuthenticationToken userAccountAuthenticationToken = new UserAccountAuthenticationToken(principal, credentials, authorities);
        userAccountAuthenticationToken.setDetails(userDetails);
        userAccountAuthenticationToken.setLoginUserDetails(loginUserDetailDTO);
        return userAccountAuthenticationToken;
    }


    /**
     * 创建认证对象
     *
     * @param principal   主体
     * @param credentials 凭证
     * @return 认证对象
     */
    public static UserAccountAuthenticationToken authenticated(Object principal, Object credentials,
                                                               Object userDetails,
                                                               LoginUserDetailDTO loginUserDetailDTO) {
        return authenticated(principal, credentials, userDetails, loginUserDetailDTO, List.of());
    }


}
