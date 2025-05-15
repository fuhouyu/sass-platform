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
package com.fuhouyu.sass.platform.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Users;

/**
 * <p>
 * 普通用户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 17:45
 */
public interface UserService extends IService<Users> {

    /**
     * 保存用户
     *
     * @param dto 用户信息
     * @return 用户id
     */
    long save(UserDTO dto);

    /**
     * 编辑用户
     *
     * @param dto 用户信息
     */
    void edit(UserDTO dto);

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserDTO findById(Long id);

    /**
     * 生成用户名称
     *
     * @return 生成用户名
     */
    String generateUsername();

    /**
     * 记录登录信息
     *
     * @param userId 用户id
     */
    void recordLoginSuccess(Long userId);
}
