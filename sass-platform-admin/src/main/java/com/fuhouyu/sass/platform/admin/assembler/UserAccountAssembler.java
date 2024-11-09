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

import com.fuhouyu.sass.platform.admin.vo.user.SaveUserinfoVO;
import com.fuhouyu.sass.platform.system.dto.UserinfoAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 用户账号转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 21:49
 */
@Mapper
public interface UserAccountAssembler {

    UserAccountAssembler INSTANCE = Mappers.getMapper(UserAccountAssembler.class);

    /**
     * 转换用户账号dto对象
     *
     * @param userinfoVO 用户账号vo对象
     * @return 账号dto对象
     */
    UserinfoAccountDTO toUserinfoAccountDTO(SaveUserinfoVO userinfoVO);

}

