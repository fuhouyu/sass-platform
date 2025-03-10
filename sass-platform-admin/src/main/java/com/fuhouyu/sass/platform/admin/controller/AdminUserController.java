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
import com.fuhouyu.sass.platform.system.domain.dto.ValidGroups;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserPageQueryDTO;
import com.fuhouyu.sass.platform.system.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 21:50
 */
@RestController
@RequestMapping("/v1/admin-user")
@Tag(name = "管理员用户 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 登录用户的用户详情
     *
     * @return 用户详情
     */
    @Operation(summary = "当前用户详情")
    @GetMapping("/me")
    public BaseResponse<AdminUserDetailDTO> userinfo() {
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        AdminUserDetailDTO userDetail = this.adminUserService.findDetailById(userId);
        userDetail.setTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        return ResponseHelper.success(userDetail);
    }


    /**
     * 通过id获取用户详情
     *
     * @return 用户详情dto对象
     */
    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("@auth.hasAnyPermission('system:user:query')")
    public BaseResponse<AdminUserDetailDTO> userDetailById(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.adminUserService.findDetailById(id));
    }

    /**
     * 修改当前用户的详情
     *
     * @param adminUserDTO 用户dto对象
     * @return restResult
     */
    @PutMapping
    @Operation(summary = "修改当前的用户详情")
    public BaseResponse<Void> editUserinfo(@Valid @RequestBody AdminUserDTO adminUserDTO) {
        adminUserDTO.setId(ContextHolderStrategy.getContext().getUser().getId());
        this.adminUserService.edit(adminUserDTO);
        return ResponseHelper.success();
    }

    /**
     * 保存用户信息
     *
     * @param userDTO 用户dto对象
     * @return 主键id
     */
    @Operation(summary = "保存用户信息")
    @PostMapping
    @PreAuthorize("@auth.hasAnyPermission('system:user:add')")
    public BaseResponse<Long> saveUser(@RequestBody @Validated({ValidGroups.SaveGroup.class}) AdminUserDetailDTO userDTO) {
        return ResponseHelper.success(this.adminUserService.saveUser(userDTO));
    }

    /**
     * 修改当前用户的详情
     *
     * @param userDTO 用户dto对象
     * @param id      主键id
     * @return restResult
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改用户详情")
    @PreAuthorize("@auth.hasAnyPermission('system:user:edit')")
    public BaseResponse<Void> editUserinfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody AdminUserDetailDTO userDTO) {
        userDTO.setId(id);
        this.adminUserService.editUser(userDTO);
        return ResponseHelper.success();
    }

    /**
     * 分页查询用户列表
     *
     * @param adminUserPageQueryDTO 用户分页查询对象
     * @return 用户列表集合
     */
    @GetMapping("/page")
    @Operation(summary = "获取用户列表")
    @PreAuthorize("@auth.hasAnyPermission('system:user:list')")
    public BaseResponse<PageResultDTO<AdminUserDTO>> pageList(AdminUserPageQueryDTO adminUserPageQueryDTO) {
        return ResponseHelper.success(this.adminUserService.pageList(adminUserPageQueryDTO));
    }

    /**
     * 通过用户id删除用户
     *
     * @param ids 用户id集合
     * @return 成功响应
     */
    @Operation(summary = "通过用户id删除用户")
    @DeleteMapping
    @PreAuthorize("@auth.hasAnyPermission('system:user:delete')")
    public BaseResponse<Void> removeUserList(
            @RequestBody
            @Size(min = 1, message = "需要删除的用户不能为空")
            @NotNull(message = "需要删除的用户不能为空") List<Long> ids) {
        this.adminUserService.removeByIds(ids);
        return ResponseHelper.success();
    }


    /**
     * 校验用户名是否存在
     *
     * @param username 用户名
     * @return true 已存在，false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "校验用户名是否存在，如果存在，则返回true")
    @Parameter(name = "username", description = "用户名称")
    public BaseResponse<Boolean> validUsernameExists(@RequestParam("username") String username) {
        return ResponseHelper.success(Objects.nonNull(this.adminUserService.findByUsername(username)));
    }

}
