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
import com.fuhouyu.sass.platform.common.utils.TreeConvertUtil;
import com.fuhouyu.sass.platform.system.dto.permission.PermissionTreeDTO;
import com.fuhouyu.sass.platform.system.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "权限 web接口服务器")
@Validated
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取用户当前权限列表
     *
     * @return 用户当前的权限列表
     */
    @GetMapping("/me")
    @Operation(summary = "获取用户当前权限列表")
    public BaseResponse<List<PermissionTreeDTO>> getPermissionByMe() {
        return ResponseHelper.success(TreeConvertUtil.buildTree(permissionService.findPermissionListByMe()));
    }


}
