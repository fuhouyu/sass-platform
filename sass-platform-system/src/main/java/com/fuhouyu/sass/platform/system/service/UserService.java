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
package com.fuhouyu.sass.platform.system.service;

import com.fuhouyu.sass.platform.system.dto.user.UserDTO;

/**
 * <p>
 * 用户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:17
 */
public interface UserService extends BaseService<UserDTO, Long> {

    /**
     * 通过用户名称查询
     *
     * @param username 用户名
     * @return 用户dto对象
     */
    UserDTO findByUsername(String username);

    /**
     * 登录成功后，记录用户的登录信息
     *
     * @param userId 用户id
     */
    void recordLoginSuccess(Long userId);
}
