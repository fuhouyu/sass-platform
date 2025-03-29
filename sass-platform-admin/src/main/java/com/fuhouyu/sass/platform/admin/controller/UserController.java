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
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 普通用户 web接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 20:05
 */
@RestController
@RequestMapping("/v1/user")
@Tag(name = "普通用户 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;


    /**
     * 当前用户详情接口
     *
     * @return 用户详情
     */
    @GetMapping("/me")
    @Operation(summary = "当前用户详情接口")
    public BaseResponse<UserDTO> userinfo() {
        return ResponseHelper.success(this.userService.findById(ContextHolderStrategy.getContext().getUser().getId()));
    }


    /**
     * 修改用户详情
     *
     * @param userDTO 用户dto对象
     * @return void
     */
    @PutMapping
    @Operation(summary = "修改当前用户详情")
    public BaseResponse<Void> editUser(@Valid @RequestBody UserDTO userDTO) {
        Long id = ContextHolderStrategy.getContext().getUser().getId();
        userDTO.setId(id);
        this.userService.edit(userDTO);
        return ResponseHelper.success();
    }
}
