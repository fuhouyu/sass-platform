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
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.account.ThirdPartyBindPlatformDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserLoginDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserTokenDTO;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 认证前端控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/12 20:44
 */
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "认证 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule("认证模块")
public class AuthenticationController {


    private final UserAccountService userAccountService;


    /**
     * 后台管理员用户登录
     *
     * @param userLoginDTO 用户登录的dto对象
     * @return 用户登录的dto对象
     */
    @PostMapping("/admin-login")
    @Operation(summary = "后台用户登录接口")
    @NoAuth
    @LogRecord(operationType = OperationTypeEnum.LOGIN,
            operationUser = "#{#userLoginDTO.account}")
    public BaseResponse<UserTokenDTO> adminLogin(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return ResponseHelper.success(this.userAccountService.adminLogin(userLoginDTO));
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录的dto对象
     * @return 响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    @NoAuth
    @LogRecord(operationType = OperationTypeEnum.LOGIN,
            operationUser = "#{#userLoginDTO.account}")
    public BaseResponse<UserTokenDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        UserTokenDTO userTokenDTO = this.userAccountService.login(userLoginDTO);
        return ResponseHelper.success(userTokenDTO);
    }


    /**
     * 通过刷新令牌，更新token
     *
     * @param refreshToken 刷新令牌
     * @return 用户信息
     */
    @PutMapping("/refresh-token")
    @Operation(summary = "通过刷新令牌更新token")
    @Parameter(name = "refreshToken", description = "刷新令牌")
    @NoAuth
    public BaseResponse<UserTokenDTO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseHelper.success(this.userAccountService.refreshToken(refreshToken));
    }

    /**
     * 用户登录时绑定第三方平台账号
     *
     * @param thirdPartyBindPlatformDTO 账号绑定的vo对象
     * @return token
     */
    @PostMapping("/login-bind")
    @Operation(summary = "用户登录时绑定第三方平台账号")
    @NoAuth
    public BaseResponse<UserTokenDTO> bindThirdParty(@RequestBody @Valid ThirdPartyBindPlatformDTO thirdPartyBindPlatformDTO) {
        UserTokenDTO userTokenDTO = this.userAccountService.loginBindThirdParty(thirdPartyBindPlatformDTO);
        return ResponseHelper.success(userTokenDTO);
    }

    /**
     * 退出登录
     *
     * @return 响应
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        this.userAccountService.logout();
        return ResponseHelper.success();
    }

}
