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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.common.utils.HexUtil;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * <p>
 * 第三方平台 web 接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/21 17:25
 */
@RestController
@RequestMapping("/v1/third-platform")
@Tag(name = "第三方平台 web 接口")
@Slf4j
@Validated
public class ThirdPlatformController {

    private final String wechatCheckToken;

    public ThirdPlatformController(@Value("${sass.third-platform.wechat.check-token:}") String wechatCheckToken) {
        this.wechatCheckToken = wechatCheckToken;
    }


    /**
     * 校验微信签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return void
     */
    @GetMapping("/wechat-check")
    @NoAuth
    public BaseResponse<Void> wechatCheck(@RequestParam("signature") String signature,
                                          @RequestParam("timestamp") String timestamp,
                                          @RequestParam("nonce") String nonce) {
        String param = String.format("%s%s%s", timestamp, nonce, wechatCheckToken);
        try {
            String shaResult = HexUtil.encodeToHexString(MessageDigest.getInstance("sha1").digest(param.getBytes()));
            if (Objects.equals(shaResult, signature)) {
                return ResponseHelper.success();
            }
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "微信签名校验失败");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "sha 1 加密算法不支持");
        }
    }
}
