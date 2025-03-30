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

import com.fuhouyu.sass.platform.system.domain.dto.wechat.WechatAppletPhoneInfoDTO;
import com.fuhouyu.sass.platform.system.domain.dto.wechat.WechatAppletSessionDTO;

/**
 * <p>
 * 微信小程序相关接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 21:34
 */
public interface WechatAppletService {


    /**
     * 通过临时码获取session
     *
     * @param code 临时码，前端传入
     * @return session信息
     */
    WechatAppletSessionDTO code2Session(String code);

    /**
     * 获取接口调用凭证
     *
     * @return token
     */
    String getAccessToken();

    /**
     * 获取手机号
     *
     * @param code 临时code
     * @return 手机号信息
     */
    WechatAppletPhoneInfoDTO getPhoneNum(String code);

}
