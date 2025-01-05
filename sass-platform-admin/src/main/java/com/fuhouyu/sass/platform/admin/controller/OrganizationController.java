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
 * See the License for the specific language governing organizations and
 * limitations under the License.
 */
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.sass.platform.system.dto.organization.OrganizationDTO;
import com.fuhouyu.sass.platform.system.dto.organization.OrganizationPageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.organization.OrganizationTreeDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.OrganizationService;
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
import java.util.Optional;

/**
 * <p>
 * 组织控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:08
 */
@RestController
@RequestMapping("/v1/organization")
@Tag(name = "组织 web接口")
@Validated
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * 保存组织dto对象
     *
     * @param organizationDTO 组织dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存组织dto对象")
    @PreAuthorize("@auth.hasPermission('system:organization:add')")
    public BaseResponse<Long> saveOrganization(@RequestBody OrganizationDTO organizationDTO) {
        return ResponseHelper.success(this.organizationService.save(organizationDTO));
    }

    /**
     * 修改组织
     *
     * @param id              主键id
     * @param organizationDTO dto对象
     * @return void
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改组织")
    @PreAuthorize("@auth.hasPermission('system:organization:edit')")
    public BaseResponse<Void> updateOrganization(@PathVariable("id") Long id,
                                                 @RequestBody OrganizationDTO organizationDTO) {
        organizationDTO.setId(id);
        this.organizationService.edit(organizationDTO);
        return ResponseHelper.success();
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
    @PreAuthorize("@auth.hasPermission('system:organization:list')")
    public BaseResponse<List<OrganizationDTO>> getOrganizationList(@PathVariable(value = "parentId", required = false) Long parentId) {
        return ResponseHelper.success(this.organizationService.getOrganizationList(
                Optional.ofNullable(parentId).orElse(-1L)
        ));
    }

    /**
     * 组织分页查询dto对象
     *
     * @param pageQuery 分页查询
     * @return 分页列表
     */
    @Operation(summary = "组织分页查询dto对象")
    @GetMapping("/page")
    @PreAuthorize("@auth.hasPermission('system:organization:list')")
    public BaseResponse<PageResultDTO<OrganizationDTO>> pageList(@ParameterObject OrganizationPageQueryDTO pageQuery) {
        return ResponseHelper.success(this.organizationService.pageList(pageQuery));
    }


    /**
     * deleteOrganization
     *
     * @param idList id 集合
     * @return void
     */
    @DeleteMapping
    @Operation(summary = "根据组织id删除组织")
    @PreAuthorize("@auth.hasPermission('system:organization:delete')")
    public BaseResponse<Void> deleteOrganization(@RequestBody @NotEmpty(message = "未选择要删除的组织") List<Long> idList) {
        this.organizationService.removeByIds(idList);
        return ResponseHelper.success();
    }

    /**
     * 组织详情
     *
     * @param id 主键id
     * @return 组织详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "组织详情")
    @PreAuthorize("@auth.hasPermission('system:organization:query')")
    public BaseResponse<OrganizationDTO> organizationInfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.organizationService.findById(id));
    }

    /**
     * 查询组织树
     *
     * @return 组织树
     */
    @GetMapping("/tree")
    @Operation(summary = "组织树列表")
    @PreAuthorize("@auth.hasPermission('system:organization:list')")
    public BaseResponse<List<OrganizationTreeDTO>> organizationTree() {
        return ResponseHelper.success(this.organizationService.getTreeList());
    }

    /**
     * 检查组织编码是否存在
     *
     * @param organizationCode 组织编码
     * @return true 已存在， false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查组织编码是否存在， true已存在")
    @Parameter(name = "organizationCode", description = "组织编码已存在")
    public BaseResponse<Boolean> checkOrganizationCodeExists(@RequestParam("organizationCode") String organizationCode) {
        return ResponseHelper.success(this.organizationService.checkOrganizationCodeExists(organizationCode));
    }
}
