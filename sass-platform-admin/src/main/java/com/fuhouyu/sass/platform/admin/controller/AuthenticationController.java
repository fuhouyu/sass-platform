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

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.sass.platform.admin.constants.WebConstant;
import com.fuhouyu.sass.platform.system.dto.user.UserLoginDTO;
import com.fuhouyu.sass.platform.system.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 认证前端控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/12 20:44
 */
@RestController
@RequestMapping(WebConstant.AUTH_CONTROLLER_PATH)
@Tag(name = "认证前端控制层")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthenticationController {


    private final UserAccountService userAccountService;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录的dto对象
     * @return 响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    public BaseResponse<UserTokenDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        UserTokenDTO userTokenDTO = this.userAccountService.login(userLoginDTO);
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
