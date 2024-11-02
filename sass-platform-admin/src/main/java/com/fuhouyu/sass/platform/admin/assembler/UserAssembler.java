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

import com.fuhouyu.sass.platform.admin.vo.user.UserVO;
import com.fuhouyu.sass.platform.system.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户详情转换接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:09
 */
@Mapper
public interface UserAssembler {

    UserAssembler INSTANCE = Mappers.getMapper(UserAssembler.class);

    /**
     * 用户dto转换为用户vo
     *
     * @param userDTO 用户dto对象
     * @return 用户详情
     */
    UserVO toUserVO(UserDTO userDTO);

    /**
     * 用户实体集合转换为用户详情集合
     *
     * @param userEntityList 用户dto集合
     * @return 用户详情集合
     */
    List<UserVO> toUserInfoList(List<UserDTO> userEntityList);

    /**
     * 转换为用户实体
     *
     * @param userVO 用户vo对象
     * @return 用户实体对象
     */
    UserDTO toUserDTO(UserVO userVO);

}
