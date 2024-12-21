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
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.service.WechatPlatformService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 微信开放平台 web 接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/21 17:25
 */
@RestController
@RequestMapping("/v1/wechat")
@Tag(name = "微信开放平台 web 接口")
@Slf4j
@Validated
@RequiredArgsConstructor
public class WechatPlatformController {

    private final WechatPlatformService wechatPlatformService;


    /**
     * 校验微信签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    @GetMapping("/check")
    @NoAuth
    public void wechatCheck(@RequestParam("signature") String signature,
                            @RequestParam("timestamp") String timestamp,
                            @RequestParam("nonce") String nonce,
                            @RequestParam("echostr") Long echoString,
                            HttpServletResponse response) {
        this.wechatPlatformService.checkSignature(signature, timestamp, nonce);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/json");
            outputStream.write(echoString.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "响应错误");
        }
    }
}
