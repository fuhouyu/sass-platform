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
import com.fuhouyu.sass.platform.system.service.UserHasRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户和角色的web层
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/19 20:34
 */
@RestController
@RequestMapping("/v1/user-role")
@Tag(name = "用户和角色 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserHasRoleController {

    private final UserHasRoleService userHasRoleService;

    /**
     * 通过用户id查询出角色id集合
     *
     * @param userId 用户id
     * @return 角色id集合
     */
    @GetMapping("/{userId}")
    @Operation(summary = "通过用户id查询出角色id集合")
    @PreAuthorize("@auth.hasAnyPermission('system:role:list')")
    public BaseResponse<List<Long>> findByRoleListByUserId(@PathVariable("userId") Long userId) {
        return ResponseHelper.success(this.userHasRoleService.findRoleIdsByUserId(userId));
    }


    /**
     * 保存用户和角色的关系
     *
     * @param userId     用户id
     * @param roleIdList 角色id集合
     * @return void
     */
    @PostMapping("/{userId}")
    @Operation(summary = "保存用户和角色的关系")
    @PreAuthorize("@auth.hasAnyPermission('system:user:add', 'system:user:edit')")
    public BaseResponse<Void> saveUserRole(@PathVariable("userId") Long userId,
                                           @RequestBody List<Long> roleIdList) {
        this.userHasRoleService.saveOrUpdateUserRole(userId, roleIdList);
        return ResponseHelper.success();
    }
}
