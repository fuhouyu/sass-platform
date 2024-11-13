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
package com.fuhouyu.sass.platform.system.assembler;

import com.fuhouyu.framework.security.token.OAuth2Token;
import com.fuhouyu.sass.platform.system.dto.user.UserTokenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * token转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/11 20:50
 */
@Mapper
public interface TokenAssembler {

    TokenAssembler INSTANCE = Mappers.getMapper(TokenAssembler.class);

    /**
     * token value转为dto对象
     *
     * @param tokenEntity token实体
     * @return dto对象
     */
    @Mapping(expression = """
            java(java.time.Duration.between(tokenEntity.getAccessToken().getIssuedAt(),
             tokenEntity.getAccessToken().getExpiresAt()).getSeconds())
            """, target = "accessTokenExpireSeconds")
    @Mapping(expression = """
            java(java.time.Duration.between(tokenEntity.getRefreshToken().getIssuedAt(),
             tokenEntity.getRefreshToken().getExpiresAt()).getSeconds())
            """,
            target = "refreshTokenExpireSeconds")
    @Mapping(source = "tokenEntity.accessToken.tokenType.value", target = "tokenType")
    @Mapping(source = "tokenEntity.accessToken.tokenValue", target = "accessToken")
    @Mapping(source = "tokenEntity.refreshToken.tokenValue", target = "refreshToken")
    @Mapping(source = "tokenEntity.accessToken.issuedAt", target = "accessTokenIssuedAt")
    @Mapping(source = "tokenEntity.refreshToken.issuedAt", target = "refreshTokenIssuedAt")
    @Mapping(source = "tokenEntity.accessToken.expiresAt", target = "accessTokenExpireAt")
    @Mapping(source = "tokenEntity.refreshToken.expiresAt", target = "refreshTokenExpireAt")
    UserTokenDTO toUserTokenDTO(OAuth2Token tokenEntity);
}
