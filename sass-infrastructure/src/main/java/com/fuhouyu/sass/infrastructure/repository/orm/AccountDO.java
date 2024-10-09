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
package com.fuhouyu.sass.infrastructure.repository.orm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * <p>
 * 账号do对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 23:32
 */
@Getter
@Setter
@ToString
public class AccountDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号唯一标识
     */
    private String account;

    /**
     * 账号类型
     */
    private String accountType;

    /**
     * 用户id
     */
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
    private Boolean isEnabled;
}
