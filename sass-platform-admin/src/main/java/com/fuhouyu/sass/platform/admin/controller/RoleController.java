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
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.dto.role.RolePageQueryDTO;
import com.fuhouyu.sass.platform.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色 api
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/11 21:01
 */
@RestController
@RequestMapping("/v1/role")
@Tag(name = "角色 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RoleController {

    private final RoleService roleService;

    /**
     * 保存角色
     *
     * @param roleDTO 角色dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存角色")
    public BaseResponse<Long> save(@Validated @RequestBody RoleDTO roleDTO) {
        return ResponseHelper.success(this.roleService.save(roleDTO));
    }

    /**
     * 修改角色
     *
     * @param roleDTO 角色dto
     * @return void
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改角色")
    public BaseResponse<Void> update(@PathVariable("id") Long id,
                                     @Validated @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        this.roleService.edit(roleDTO);
        return ResponseHelper.success();
    }

    /**
     * 通过主键id获取详情
     *
     * @param id 主键id
     * @return 详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "通过主键id获取详情")
    public BaseResponse<RoleDTO> getById(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.roleService.findById(id));
    }

    /**
     * 通过id集合删除数据
     *
     * @param ids ids
     * @return 影响行数
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除数据")
    public BaseResponse<Integer> deleteByIds(@NotEmpty(message = "删除的数据未选择") @RequestBody List<Long> ids) {
        return ResponseHelper.success(this.roleService.removeByIds(ids));
    }


    /**
     * 角色分页查询列表
     *
     * @param pageQueryDTO 分页查询dto对象
     * @return 分页查询的dto对象
     */
    @GetMapping("/page")
    @Operation(summary = "角色分页查询列表")
    public BaseResponse<PageResultDTO<RoleDTO>> pageList(@ParameterObject RolePageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(this.roleService.pageList(pageQueryDTO));
    }


    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @return true 已存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查角色编码是否存在")
    @Parameter(name = "roleCode", description = "角色编码")
    public BaseResponse<Boolean> checkCodeExists(@RequestParam("roleCode") String roleCode) {
        return ResponseHelper.success(Objects.nonNull(this.roleService.findByRoleCode(roleCode)));
    }
}
