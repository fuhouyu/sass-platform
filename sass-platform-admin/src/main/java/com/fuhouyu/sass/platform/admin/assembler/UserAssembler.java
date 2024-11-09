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
import com.fuhouyu.sass.platform.admin.vo.user.UserinfoVO;
import com.fuhouyu.sass.platform.system.dto.SaveUserinfoDTO;
import com.fuhouyu.sass.platform.system.dto.UserinfoDTO;
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
     * @param userinfoDTO 用户dto对象
     * @return 用户详情
     */
    UserinfoVO toUserVO(UserinfoDTO userinfoDTO);

    /**
     * 用户实体集合转换为用户详情集合
     *
     * @param userEntityList 用户dto集合
     * @return 用户详情集合
     */
    List<UserinfoVO> toUserInfoList(List<UserinfoDTO> userEntityList);

    /**
     * 转换为用户实体
     *
     * @param userinfoVO 用户vo对象
     * @return 用户实体对象
     */
    UserinfoDTO toUserDTO(UserinfoVO userinfoVO);


    /**
     * 转换vo对dto对象
     *
     * @param saveUserinfoVO 保存的用户灵对象
     * @return dto对象
     */
    SaveUserinfoDTO toUserDTO(SaveUserinfoVO saveUserinfoVO);

}
