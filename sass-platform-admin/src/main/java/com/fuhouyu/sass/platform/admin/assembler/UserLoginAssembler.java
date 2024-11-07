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
package com.fuhouyu.sass.platform.admin.assembler;

import com.fuhouyu.framework.security.entity.TokenEntity;
import com.fuhouyu.sass.platform.admin.vo.user.UserLoginVO;
import com.fuhouyu.sass.platform.admin.vo.user.UserTokenVO;
import com.fuhouyu.sass.platform.system.dto.LoginAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 用户登录
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 21:49
 */
@Mapper
public interface UserLoginAssembler {

    UserLoginAssembler INSTANCE = Mappers.getMapper(UserLoginAssembler.class);

    /**
     * 转换为账号dto对象
     *
     * @param userLoginVO 用户登录vo对象
     * @return 账号dto对象
     */
    @Mapping(source = "loginType", target = "accountType")
    @Mapping(source = "username", target = "account")
    @Mapping(source = "password", target = "password")
    LoginAccountDTO toLoginAccountDTO(UserLoginVO userLoginVO);

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
    UserTokenVO toUserTokenVO(TokenEntity tokenEntity);
}
