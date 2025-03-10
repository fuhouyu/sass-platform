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

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.domain.dto.welink.WeLinkAccessTokenDTO;
import com.fuhouyu.sass.platform.system.domain.dto.welink.WeLinkLoginUserDTO;
import com.fuhouyu.sass.platform.system.enums.OpenPlatformTypeEnum;
import com.fuhouyu.sass.platform.system.properties.OpenPlatformProperties;
import com.fuhouyu.sass.platform.system.service.WeLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * weLink实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/22 18:14
 */
@Service
@Slf4j
public class WeLinkServiceImpl implements WeLinkService {

    private static final String WE_LINK_ACCESS_TOKEN = "welink:accessToken";

    private static final String TICKET_URL = "/api/auth/v2/tickets/";

    private static final String LOGIN_URL = "/api/auth/v2/userid?code=";

    private final CacheService<String, Object> cacheService;

    private final OpenPlatformProperties.Properties properties;

    public WeLinkServiceImpl(CacheService<String, Object> cacheService,
                             OpenPlatformProperties openPlatformProperties) {
        this.cacheService = cacheService;
        this.properties = openPlatformProperties.getOpenPlatform().get(OpenPlatformTypeEnum.WELINK);
    }

    @Override
    public String getAccessToken() {
        String accessToken = (String) this.cacheService.get(WE_LINK_ACCESS_TOKEN);
        if (Objects.nonNull(accessToken)) {
            return accessToken;
        }
        Map<String, String> body = new HashMap<>();
        body.put("client_id", properties.getAccessKey());
        body.put("client_secret", properties.getSecretKey());
        ResponseEntity<WeLinkAccessTokenDTO> responseEntity = RestClient.create(properties.getBaseUrl() + TICKET_URL)
                .post()
                .body(body)
                .retrieve()
                .toEntity(WeLinkAccessTokenDTO.class);
        WeLinkAccessTokenDTO weLinkAccessTokenDTO = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful() || Objects.isNull(weLinkAccessTokenDTO)) {
            log.error("获取accessToken失败，错误码：{}，错误信息：{}", responseEntity.getStatusCode(), weLinkAccessTokenDTO);
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "获取accessToken失败");
        }
        this.checkCodeMessage(weLinkAccessTokenDTO.getCode(), weLinkAccessTokenDTO.getMessage());
        cacheService.set(WE_LINK_ACCESS_TOKEN, weLinkAccessTokenDTO.getAccessToken(), weLinkAccessTokenDTO.getExpiresIn(), TimeUnit.SECONDS);
        return weLinkAccessTokenDTO.getAccessToken();
    }

    @Override
    public WeLinkLoginUserDTO login(String code) {
        String accessToken = this.getAccessToken();
        ResponseEntity<WeLinkLoginUserDTO> responseEntity = RestClient.create(properties.getBaseUrl()
                        + LOGIN_URL + code)
                .get()
                .header("x-wlk-Authorization", accessToken)
                .retrieve()
                .toEntity(WeLinkLoginUserDTO.class);
        WeLinkLoginUserDTO weLinkLoginUserDTO = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful() || Objects.isNull(weLinkLoginUserDTO)) {
            log.error("通过code登录失败，错误码：{}", responseEntity.getStatusCode());
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "weLink登录失败");
        }
        this.checkCodeMessage(weLinkLoginUserDTO.getCode(), weLinkLoginUserDTO.getMessage());
        return weLinkLoginUserDTO;
    }

    /**
     * 检查codeMessage
     *
     * @param code    code
     * @param message 信息
     */
    private void checkCodeMessage(String code, String message) {
        if (Objects.equals("0", code)) {
            return;
        }
        throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                String.format("weLink登录失败，错误码：%s 错误信息：%s", code, message));
    }

}
