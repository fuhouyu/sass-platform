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

import com.fuhouyu.framework.security.entity.TokenEntity;
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
     * @param tokenEntity token实体对象
     * @return 实体对象
     */
    @Mappings({
            @Mapping(expression = """
                    java(java.time.Duration.between(tokenEntity.getAccessToken().getIssuedAt(),
                     tokenEntity.getAccessToken().getExpiresAt()).getSeconds())
                    """, target = "accessTokenExpireSeconds"),
            @Mapping(expression = """
                    java(java.time.Duration.between(tokenEntity.getRefreshToken().getIssuedAt(),
                     tokenEntity.getRefreshToken().getExpiresAt()).getSeconds())
                    """,
                    target = "refreshTokenExpireSeconds"),
            @Mapping(source = "tokenEntity.accessToken.tokenType.value", target = "tokenType"),
            @Mapping(source = "tokenEntity.accessToken.tokenValue", target = "accessToken"),
            @Mapping(source = "tokenEntity.refreshToken.tokenValue", target = "refreshToken"),
            @Mapping(source = "tokenEntity.accessToken.issuedAt", target = "accessTokenIssuedAt"),
            @Mapping(source = "tokenEntity.refreshToken.issuedAt", target = "refreshTokenIssuedAt"),
            @Mapping(source = "tokenEntity.accessToken.expiresAt", target = "accessTokenExpireAt"),
            @Mapping(source = "tokenEntity.refreshToken.expiresAt", target = "refreshTokenExpireAt"),
    })
    TokenValueEntity toTokenValueEntity(TokenEntity tokenEntity);

}
