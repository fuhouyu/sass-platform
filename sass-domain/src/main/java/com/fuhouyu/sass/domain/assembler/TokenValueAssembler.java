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
package com.fuhouyu.sass.domain.assembler;

import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 11:57
 */
@Mapper
public interface TokenValueAssembler {


    TokenValueAssembler INSTANCE = Mappers.getMapper(TokenValueAssembler.class);

    /**
     * 将token值转换为实体
     *
     * @param token token对象
     * @return 实体对象
     */
    @Mappings({
            @Mapping(source = "tokenExpireSeconds", target = "accessTokenExpireSeconds"),
            @Mapping(expression = """
                    java(java.time.Duration.between(token.getAuth2RefreshToken().getExpiresAt(),
                     token.getAuth2RefreshToken().getIssuedAt()).getSeconds())
                    """,
                    target = "refreshTokenExpireSeconds"),
            @Mapping(source = "tokenType.value", target = "tokenType"),
            @Mapping(source = "tokenValue", target = "accessToken"),
            @Mapping(source = "auth2RefreshToken.tokenValue", target = "refreshToken"),
            @Mapping(source = "issuedAt", target = "accessTokenIssuedAt"),
            @Mapping(source = "auth2RefreshToken.issuedAt", target = "refreshTokenIssuedAt"),
            @Mapping(source = "expiresAt", target = "accessTokenExpireAt"),
            @Mapping(source = "auth2RefreshToken.expiresAt", target = "refreshTokenExpireAt"),
    })
    TokenValueEntity toTokenValueEntity(DefaultOAuth2Token token);

}
