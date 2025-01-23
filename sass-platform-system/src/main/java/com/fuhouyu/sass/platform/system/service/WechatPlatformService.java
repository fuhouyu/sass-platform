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

/**
 * <p>
 * 微信开放平台接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/21 20:27
 */
public interface WechatPlatformService {

    /**
     * 检查签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    void checkSignature(String signature, String timestamp, String nonce);

    /**
     * 获取开放平台的token
     *
     * @return token
     */
    String getAccessToken();
}
