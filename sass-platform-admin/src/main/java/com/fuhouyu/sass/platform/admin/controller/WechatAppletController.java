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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.wechat.WechatAppletPhoneInfoDTO;
import com.fuhouyu.sass.platform.system.service.UserService;
import com.fuhouyu.sass.platform.system.service.WechatAppletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 微信前端控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/31 09:33
 */
@RestController
@RequestMapping("/v1/wechat-applet")
@Tag(name = "微信小程序 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule("微信小程序模块")
public class WechatAppletController {

    private final UserService userService;

    private final WechatAppletService wechatAppletService;

    /**
     * 保存手机号
     *
     * @param code 手机号临时的编码，用于获取手机号
     * @return void
     */
    @PostMapping("/phone")
    @Operation(summary = "保存手机号")
    @Parameter(name = "code", description = "用于获取手机号的临时编码")
    @LogRecord(operationType = OperationTypeEnum.CREATE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> savePhoneNum(@RequestParam("code") String code) {
        WechatAppletPhoneInfoDTO wechatAppletPhoneInfoDTO = this.wechatAppletService.getPhoneNum(code);
        UserDTO user = this.userService.findById(ContextHolderStrategy.getContext().getUser().getId());
        user.setPhone(wechatAppletPhoneInfoDTO.getPhoneNumber());
        this.userService.edit(user);
        return ResponseHelper.success();
    }
}
