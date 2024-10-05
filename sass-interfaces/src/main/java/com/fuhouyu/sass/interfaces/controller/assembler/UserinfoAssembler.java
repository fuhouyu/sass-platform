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

import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.interfaces.controller.dto.UserinfoDTO;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 用户详情转换接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:09
 */
public interface UserinfoAssembler {

    UserinfoAssembler INSTANCE = Mappers.getMapper(UserinfoAssembler.class);

    /**
     * 用户实体转换为用户详情
     *
     * @param userEntity 用户实体
     * @return 用户详情
     */
    UserinfoDTO toUserInfo(UserEntity userEntity);

}
