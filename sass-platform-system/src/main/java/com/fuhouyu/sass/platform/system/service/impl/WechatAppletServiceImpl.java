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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.sass.platform.system.dto.wechat.WechatAppletSessionDTO;
import com.fuhouyu.sass.platform.system.enums.OpenPlatformTypeEnum;
import com.fuhouyu.sass.platform.system.properties.OpenPlatformProperties;
import com.fuhouyu.sass.platform.system.service.WechatAppletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * <p>
 * 微信小程序实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 21:39
 */
@Service
@Slf4j
public class WechatAppletServiceImpl implements WechatAppletService {

    private static final String SESSION_URL = "/sns/jscode2session";

    private final OpenPlatformProperties.Properties properties;

    public WechatAppletServiceImpl(OpenPlatformProperties openPlatformProperties) {
        this.properties = openPlatformProperties.getOpenPlatform().get(OpenPlatformTypeEnum.WECHAT_APPLET);
    }

    @Override
    public WechatAppletSessionDTO code2Session(String code) {

        String responseStr = RestClient.create(properties.getBaseUrl())
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(SESSION_URL)
                                .queryParam("appid", properties.getAccessKey())
                                .queryParam("secret", properties.getSecretKey())
                                .queryParam("js_code", code)
                                .queryParam("grant_type", "authorization_code").build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        if (Objects.isNull(responseStr)) {
            log.error("通过code: {} 获取微信小程序session失败, 返回结果为空", code);
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "微信小程序登录失败");
        }
        WechatAppletSessionDTO wechatAppletSessionDTO = JacksonUtil.readValue(responseStr, WechatAppletSessionDTO.class);
        if (Objects.isNull(wechatAppletSessionDTO.getErrCode())) {
            return wechatAppletSessionDTO;
        }
        throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, wechatAppletSessionDTO.getErrMsg());
    }
}
