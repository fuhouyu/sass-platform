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
import com.fuhouyu.framework.common.utils.HexUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.sass.platform.system.dto.wechat.WechatAccessTokenDTO;
import com.fuhouyu.sass.platform.system.properties.WechatPlatformProperties;
import com.fuhouyu.sass.platform.system.service.WechatPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 微信平台实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/21 20:28
 */
@Slf4j
@Service
public class WechatPlatformServiceImpl implements WechatPlatformService {

    public static final String ACCESS_TOKEN = "wechat:access_token";

    public final String tokenUrl;

    private final WechatPlatformProperties wechatPlatformProperties;

    private final MessageDigest sha1;

    private final CacheService<String, Object> cacheService;

    public WechatPlatformServiceImpl(WechatPlatformProperties wechatPlatformProperties,
                                     CacheService<String, Object> cacheService) {
        this.wechatPlatformProperties = wechatPlatformProperties;
        this.cacheService = cacheService;
        this.sha1 = this.initSha1();
        this.tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                wechatPlatformProperties.getAppId(), wechatPlatformProperties.getAppSecret());
    }

    private MessageDigest initSha1() {
        try {
            return MessageDigest.getInstance("sha1");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "sha 1 加密算法不支持");
        }
    }


    @Override
    public void checkSignature(String signature, String timestamp, String nonce) {
        String[] array = new String[]{wechatPlatformProperties.getToken(), timestamp, nonce};
        Arrays.sort(array);
        String shaResult = HexUtil.encodeToHexString(this.sha1.digest(String.join("", array).getBytes(StandardCharsets.UTF_8)));
        if (!Objects.equals(shaResult, signature)) {
            LoggerUtil.error(log, "微信签名校验失败，signature:{}, timestamp:{}, nonce:{}", signature, timestamp, nonce);
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "微信签名校验失败");
        }
    }

    @Override
    public String getAccessToken() {
        String value = (String) this.cacheService.get(ACCESS_TOKEN);
        if (Objects.nonNull(value)) {
            return value;
        }
        RestClient restClient = RestClient.builder().baseUrl(this.tokenUrl).build();
        ResponseEntity<WechatAccessTokenDTO> responseEntity = RestClient.create(this.tokenUrl)
                .get()
                .retrieve()
                .toEntity(WechatAccessTokenDTO.class);
        WechatAccessTokenDTO tokenDTO = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful() || Objects.isNull(tokenDTO)) {
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                    "获取微信token失败，请检查配置是否正确");
        }
        this.cacheService.set(ACCESS_TOKEN, tokenDTO.getAccessToken(), tokenDTO.getExpiresIn(), TimeUnit.SECONDS);
        return tokenDTO.getAccessToken();
    }
}
