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
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.dto.permission.PermissionPageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.permission.PermissionTreeDTO;
import com.fuhouyu.sass.platform.system.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:08
 */
@RestController
@RequestMapping("/v1/permission")
@Tag(name = "权限 web接口")
@Validated
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 保存权限dto对象
     *
     * @param permissionDTO 权限dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存权限dto对象")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:add')")
    public BaseResponse<Long> savePermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseHelper.success(this.permissionService.save(permissionDTO));
    }

    /**
     * 修改权限
     *
     * @param id            主键id
     * @param permissionDTO dto对象
     * @return void
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改权限")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:edit')")
    public BaseResponse<Void> updatePermission(@PathVariable("id") Long id,
                                               @RequestBody PermissionDTO permissionDTO) {
        permissionDTO.setId(id);
        this.permissionService.edit(permissionDTO);
        return ResponseHelper.success();
    }

    /**
     * 获取用户当前权限列表
     *
     * @return 用户当前的权限列表
     */
    @GetMapping("/me")
    @Operation(summary = "获取用户当前权限列表")
    public BaseResponse<List<PermissionTreeDTO>> getPermissionByMe() {
        return ResponseHelper.success(permissionService.findPermissionListByMe());
    }

    /**
     * 通过父级id查询查询
     * 当父级id不存在时，查询一级菜单
     * 当前接口懒加载时使用
     *
     * @param parentId 父级id
     * @return 菜单列表
     */
    @Operation(summary = "通过父级id查询子级菜单，")
    @GetMapping({"/list/{parentId}", "/list"})
    @PreAuthorize("@auth.hasAnyPermission('system:permission:list')")
    public BaseResponse<List<PermissionDTO>> getPermissionList(@PathVariable(value = "parentId", required = false) Long parentId) {
        return ResponseHelper.success(this.permissionService.getPermissionList(parentId));
    }

    /**
     * 权限分页查询dto对象
     *
     * @param pageQuery 分页查询
     * @return 分页列表
     */
    @Operation(summary = "权限分页查询dto对象")
    @GetMapping("/page")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:list')")
    public BaseResponse<PageResultDTO<PermissionDTO>> pageList(@ParameterObject PermissionPageQueryDTO pageQuery) {
        return ResponseHelper.success(this.permissionService.pageList(pageQuery));
    }


    /**
     * deletePermission
     *
     * @param idList id 集合
     * @return void
     */
    @DeleteMapping
    @Operation(summary = "根据权限id删除权限")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:delete')")
    public BaseResponse<Void> deletePermission(@RequestBody @NotEmpty(message = "未选择要删除的权限") List<Long> idList) {
        this.permissionService.removeByIds(idList);
        return ResponseHelper.success();
    }

    /**
     * 权限树集合，需要有菜单权限
     *
     * @return 权限树集合
     */
    @GetMapping("/tree")
    @Operation(summary = "权限树集合,需要有菜单权限")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:list')")
    public BaseResponse<List<PermissionTreeDTO>> treeList() {
        return ResponseHelper.success(this.permissionService.getTreeList());
    }

    /**
     * 权限详情
     *
     * @param id 主键id
     * @return 权限详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "权限详情")
    @PreAuthorize("@auth.hasAnyPermission('system:permission:query')")
    public BaseResponse<PermissionDTO> permissionInfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.permissionService.findById(id));
    }

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @return true 已存在， false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查权限编码是否存在， true已存在")
    @Parameter(name = "permissionCode", description = "权限编码已存在")
    public BaseResponse<Boolean> checkPermissionCodeExists(@RequestParam("permissionCode") String permissionCode) {
        return ResponseHelper.success(this.permissionService.checkPermissionCodeExists(permissionCode));
    }
}
