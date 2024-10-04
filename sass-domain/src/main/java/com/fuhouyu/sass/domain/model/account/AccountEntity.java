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
package com.fuhouyu.sass.domain.model.account;

import com.fuhouyu.sass.domain.model.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.LocalDateTime;

/**
 * <p>
 * 账号实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 17:54
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AccountEntity extends BaseEntity<AccountIdEntity> {

    /**
     * 账号唯一标识
     */
    private final AccountIdEntity accountIdEntity;

    /**
     * 用户id
     */
    @Setter(AccessLevel.PRIVATE)
    private Long userId;

    /**
     * 凭证
     */
    private String credentials;

    /**
     * 凭证过期时间
     */
    private LocalDateTime credentialsExpirationTime;

    /**
     * 第三方所属的账号id
     */
    private String refAccountId;

    /**
     * 是否启用标记
     */
    @Setter(AccessLevel.PRIVATE)
    private Boolean isEnabled;

    @Override
    public AccountIdEntity getIdentifierId() {
        return this.accountIdEntity;
    }

    @Override
    public boolean sameIdentityAs(AccountIdEntity other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    public void disabled() {
        this.isEnabled = false;
    }

    public void enabled() {
        this.isEnabled = true;
    }

    public void attachUser(Long userId) {
        this.userId = userId;
    }

    public void detachUser() {
        this.userId = null;
    }
}
