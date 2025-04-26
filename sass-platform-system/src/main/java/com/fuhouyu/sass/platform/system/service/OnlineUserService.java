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

import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.LoginUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.OnlineUserPageQueryDTO;

import java.util.List;

/**
 * <p>
 * 在线用户的相关接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/23 20:29
 */
public interface OnlineUserService {


    /**
     * 分页查询在线用户对象
     *
     * @param onlineUserPageQueryDTO 在线用户查询的dto对象
     * @return 在线用户
     */
    PageResultDTO<LoginUserDetailDTO> onlineUserList(OnlineUserPageQueryDTO onlineUserPageQueryDTO);

    /**
     * 强制下线用户
     *
     * @param sessionIds 会话id
     */
    void forceLogout(List<String> sessionIds);
}
