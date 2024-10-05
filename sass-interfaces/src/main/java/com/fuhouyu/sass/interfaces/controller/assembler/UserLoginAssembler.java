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
package com.fuhouyu.sass.interfaces.controller.assembler;

import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.interfaces.controller.dto.UserLoginCommand;
import com.fuhouyu.sass.interfaces.controller.dto.UserTokenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
     * 转换为账号实体
     *
     * @param userLoginCommand 用户登录操作
     * @return 账号实体
     */
    @Mappings({
            @Mapping(source = "loginType", target = "accountIdEntity.accountType"),
            @Mapping(source = "username", target = "accountIdEntity.account"),
            @Mapping(source = "password", target = "credentials")
    })
    AccountEntity toAccountEntity(UserLoginCommand userLoginCommand);

    /**
     * token value转为dto对象
     *
     * @param tokenValueEntity 实体
     * @return dto对象
     */
    UserTokenDTO toUserTokenDTO(TokenValueEntity tokenValueEntity);
}
