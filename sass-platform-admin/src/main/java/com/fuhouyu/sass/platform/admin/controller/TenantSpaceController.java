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
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDetailDTO;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 租户空间web接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/23 16:48
 */
@RestController
@RequestMapping("/v1/tenant-space")
@Tag(name = "租户空间 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TenantSpaceController {

    private final TenantSpaceService tenantSpaceService;


    /**
     * 检查桶名称是否存在
     *
     * @param bucketName 桶名称
     * @return true 已存在 false 不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查租户桶名称是否已存在，true 已存在")
    @Parameter(name = "bucketName", description = "桶名称")
    public BaseResponse<Boolean> checkTenantCodeExists(@RequestParam("bucketName") String bucketName) {
        return ResponseHelper.success(this.tenantSpaceService.checkNameExists(bucketName));
    }


    /**
     * 通过租户id获取租户空间
     *
     * @param tenantId 租户id
     * @return 租户w空间信息
     */
    @GetMapping("/{tenantId}")
    @Operation(summary = "通过租户id获取租户空间")
    public BaseResponse<TenantSpaceDTO> getTenantSpaceByTenantId(@PathVariable("tenantId") Long tenantId) {
        return ResponseHelper.success(this.tenantSpaceService.findByTenantId(tenantId));
    }


    /**
     * 获取当前用户下的租户空间
     *
     * @return 租户空间信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户下的租户空间")
    public BaseResponse<TenantSpaceDetailDTO> getTenantSpaceFormMe() {
        return ResponseHelper.success(this.tenantSpaceService.findDetailByTenantId(ContextHolderStrategy.getContext().getUser().getTenantId()));
    }

}
