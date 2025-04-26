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
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.LoginUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.OnlineUserPageQueryDTO;
import com.fuhouyu.sass.platform.system.service.OnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 在线用户
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/25 21:50
 */
@RestController
@RequestMapping("/v1/online/user")
@Tag(name = "在线用户 web接口")
@Validated
@RequiredArgsConstructor
@LogModule("在线用户")
public class OnlineUserController {

    private final OnlineUserService onlineUserService;

    /**
     * 在线用户列表
     *
     * @param onlineUserPageQueryDTO 在线用户分页查询的dto对象
     * @return 在线用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "在线用户列表")
    @PreAuthorize("@auth.hasAnyPermission('monitor:online-user:list')")
    public BaseResponse<PageResultDTO<LoginUserDetailDTO>> onlineUserList(OnlineUserPageQueryDTO onlineUserPageQueryDTO) {
        return ResponseHelper.success(this.onlineUserService.onlineUserList(onlineUserPageQueryDTO));
    }


    /**
     * 强制踢出在线用户
     *
     * @param sessionIds 用户id
     * @return void
     */
    @DeleteMapping
    @Operation(summary = "强制踢出在线用户")
    @PreAuthorize("@auth.hasAnyPermission('monitor:online-user:logout')")
    @LogRecord(operationType = OperationTypeEnum.DELETE,
            riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> logout(@RequestBody List<String> sessionIds) {
        this.onlineUserService.forceLogout(sessionIds);
        return ResponseHelper.success();
    }
}
