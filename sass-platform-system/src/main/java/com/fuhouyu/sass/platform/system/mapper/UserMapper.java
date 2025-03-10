/*
 * Copyright 2024-2025 fuhouyu.
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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.sass.platform.system.entity.Users;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 17:34
 */
public interface UserMapper extends BaseMapper<Long, Users> {


    /**
     * 记录用户登录信息
     *
     * @param userId    用户id
     * @param loginIp   登录ip
     * @param loginTime 登录时间
     */
    void recordLoginSuccess(@Param("userId") Long userId,
                            @Param("loginIp") String loginIp,
                            @Param("loginTime") LocalDateTime loginTime);

}
