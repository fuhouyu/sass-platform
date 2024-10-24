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
package com.fuhouyu.sass.interfaces.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.web.response.ResponseHelper;
import com.fuhouyu.sass.common.utils.TreeConvertUtil;
import com.fuhouyu.sass.domain.model.permission.PermissionEntity;
import com.fuhouyu.sass.domain.service.PermissionService;
import com.fuhouyu.sass.interfaces.controller.assembler.PermissionAssembler;
import com.fuhouyu.sass.interfaces.controller.constants.WebConstant;
import com.fuhouyu.sass.interfaces.controller.dto.permission.PermissionInfoTreeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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
@RequestMapping(WebConstant.PERMISSION_CONTROLLER_PATH)
@Validated
@Tag(name = "权限前端控制层")
@RequiredArgsConstructor
public class PermissionController {

    private static final PermissionAssembler PERMISSION_ASSEMBLER = PermissionAssembler.INSTANCE;

    private final PermissionService permissionService;

    /**
     * 获取用户当前权限列表
     *
     * @return 用户当前的权限列表
     */
    @GetMapping("/me")
    @Operation(summary = "获取用户当前权限列表",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = PermissionInfoTreeDTO.class))))
    public BaseResponse<List<PermissionInfoTreeDTO>> getPermissionByMe() {
        List<PermissionEntity> permissionList = permissionService.findPermissionListByMe(true);
        List<PermissionInfoTreeDTO> treeList = PERMISSION_ASSEMBLER.toPermissionInfoTreeDTOList(permissionList);
        if (CollectionUtils.isEmpty(treeList)) {
            return ResponseHelper.success(Collections.emptyList());
        }
        return ResponseHelper.success(TreeConvertUtil.buildTree(treeList));
    }


}
