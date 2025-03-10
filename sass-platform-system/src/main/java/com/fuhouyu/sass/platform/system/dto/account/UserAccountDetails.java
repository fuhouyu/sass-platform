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
package com.fuhouyu.sass.platform.system.dto.account;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * oauth2 用户详情
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 10:13
 */
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class UserAccountDetails implements UserDetails {

    /**
     * 账号
     */
    private final String account;

    /**
     * 凭证
     */
    private transient String credentials;

    /**
     * 是否启用
     */
    private final Boolean isEnabled;

    /**
     * 用户id
     */
    private final Long userId;

    /**
     * 用户类型
     */
    private String userType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.credentials;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * 擦除密码信息
     */
    public void eraseCredentials() {
        this.credentials = null;
    }
}
