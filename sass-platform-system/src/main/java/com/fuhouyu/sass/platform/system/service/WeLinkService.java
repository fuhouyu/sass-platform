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

import com.fuhouyu.sass.platform.system.domain.dto.welink.WeLinkLoginUserDTO;

/**
 * <p>
 * weLink
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/22 18:13
 */
public interface WeLinkService {

    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    String getAccessToken();

    /**
     * 通过code登录
     *
     * @param code code
     * @return 登录
     */
    WeLinkLoginUserDTO login(String code);
}
