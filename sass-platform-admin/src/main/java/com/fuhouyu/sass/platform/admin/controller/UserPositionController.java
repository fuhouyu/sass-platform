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
import com.fuhouyu.sass.platform.system.dto.user.UserPositionDTO;
import com.fuhouyu.sass.platform.system.service.UserPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * <p>
 * 用户职务 web
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/30 20:39
 */
@RestController
@RequestMapping("/v1/user-position")
@Tag(name = "用户职务 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserPositionController {

    private final UserPositionService userPositionService;

    /**
     * 保存用户职务信息
     *
     * @param userPositionDTO 用户职务dto对象
     * @return void
     */
    @PostMapping
    @Operation(summary = "保存用户职务信息")
    @PreAuthorize("@auth.hasAnyPermission('system:organization:add-member')")
    public BaseResponse<Void> saveUserPosition(@RequestBody UserPositionDTO userPositionDTO) {
        this.userPositionService.saveUserPosition(userPositionDTO.getUserId(), userPositionDTO);
        return ResponseHelper.success();
    }

    /**
     * 通过组织id和用户id删除成员
     *
     * @param organizationId 组织id
     * @param userIds        用户id
     * @return void
     */
    @DeleteMapping("/{organizationId}")
    @Operation(summary = "通过组织id和用户id删除成员")
    @PreAuthorize("@auth.hasAnyPermission('system:organization:delete-member')")
    public BaseResponse<Long> deleteUserPosition(@PathVariable("organizationId") Long organizationId,
                                                 @NotEmpty(message = "用户未选择") @RequestBody Collection<Long> userIds) {
        return ResponseHelper.success(this.userPositionService.removeByOrganizationIdAndUserIds(organizationId, userIds));
    }
}
