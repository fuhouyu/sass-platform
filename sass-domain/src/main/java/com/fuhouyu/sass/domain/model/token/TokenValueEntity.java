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
package com.fuhouyu.sass.domain.model.token;

import com.fuhouyu.sass.common.ValueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * <p>
 * token实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 11:43
 */
@Getter
@Setter
@ToString
public class TokenValueEntity implements ValueObject<TokenValueEntity> {

    private long accessTokenExpireSeconds;

    private long refreshTokenExpireSeconds;

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenIssuedAt;

    private Instant refreshTokenIssuedAt;

    private Instant accessTokenExpireAt;

    private Instant refreshTokenExpireAt;

    @Override
    public boolean sameValueAs(TokenValueEntity other) {
        return other.equals(this);
    }
}
