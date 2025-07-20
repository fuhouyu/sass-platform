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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.AuthenticationPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.UserPasskeyListDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.RegisterPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.domain.entity.UserPasskey;
import com.fuhouyu.sass.platform.system.service.UserPasskeysService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 认证前端控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/12 20:44
 */
@RestController
@RequestMapping("/v1/passkeys")
@Tag(name = "通行密钥 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule("通行密钥")
public class PassKeysController {

    private final UserPasskeysService userPasskeysService;

    /**
     * 获取通行证密钥的选项
     *
     * @return 通行密钥选项
     */
    @GetMapping("/attestation-options/generate")
    @Operation(summary = "获取通行证密钥的选项")
    public BaseResponse<String> passkeyAttestationOptions() {
        return ResponseHelper.success(userPasskeysService.generateRegistrationOptions());
    }

    /**
     * 注册通行证密钥
     *
     * @param registerPasskey 注册通行证密钥的dto对象
     * @return void
     */
    @PostMapping("/register")
    @Operation(summary = "注册通行证密钥")
    public BaseResponse<Void> passkeyRegister(@RequestBody RegisterPasskeyDTO registerPasskey) {
        this.userPasskeysService.registerPasskey(registerPasskey);
        return ResponseHelper.success();
    }

    /**
     * 获取当前用户的通行证密钥
     *
     * @return 当前用户的通行证密钥attestation-options
     */
    @GetMapping("/list/me")
    @Operation(summary = "获取当前用户的通行证密钥")
    public BaseResponse<List<UserPasskeyListDTO>> passkeyList() {
        return ResponseHelper.success(this.userPasskeysService.passkeyList());
    }

    /**
     * 获取当前用户的通行证密钥断言
     * @param username 用户名
     * @return 断言数据
     */
    @GetMapping("/attestation-options")
    @Operation(summary = "获取当前用户的通行证密钥断言")
    @Parameter(name = "username", description = "用户名")
    @NoAuth
    public BaseResponse<String> getAttestationOptions(@RequestParam("username")String username) {
        return ResponseHelper.success(this.userPasskeysService.getAttestationOptionsByUsername(username));
    }

    /**
     * 通过keyId删除当前用户的通行密钥
     * @param passkeyId 通行密钥id
     * @return true false
     */
    @DeleteMapping
    @Operation(summary = "通过keyId删除当前用户的通行密钥")
    public BaseResponse<Boolean> deleteByPasskeyId(@RequestParam String passkeyId) {
        LambdaQueryWrapper<UserPasskey> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserPasskey::getPasskeyId, passkeyId);
        lambdaQueryWrapper.eq(UserPasskey::getUsername, ContextHolderStrategy.getContext().getUser().getUsername());
        return ResponseHelper.success(this.userPasskeysService.remove(lambdaQueryWrapper));
    }
}
