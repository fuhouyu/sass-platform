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

import com.fuhouyu.sass.platform.system.dto.SecurityUserDetailDTO;
import com.fuhouyu.sass.platform.system.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * security 转换类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 21:39
 */
@Mapper(uses = AccountsAssembler.class)
public interface SecurityUserDetailAssembler {

    SecurityUserDetailAssembler INSTANCE = Mappers.getMapper(SecurityUserDetailAssembler.class);

    SecurityUserDetailDTO toSecurityUserDetail(UserDTO userDTO);
}
