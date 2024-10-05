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

import com.fuhouyu.sass.domain.model.user.SecurityUserDetailEntity;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 10:41
 */
@Mapper
public interface SecurityUserDetailAssembler {

    SecurityUserDetailAssembler INSTANCE = Mappers.getMapper(SecurityUserDetailAssembler.class);


    SecurityUserDetailEntity toAuthUserDetailEntity(UserEntity userEntity);

}