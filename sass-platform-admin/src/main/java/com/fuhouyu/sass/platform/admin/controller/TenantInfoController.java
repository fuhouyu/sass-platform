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
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.service.TenantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 租户控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/11 21:45
 */
@RestController
@RequestMapping("/v1/tenant")
@Tag(name = "租户配置 web接口层")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TenantInfoController {

    private final TenantInfoService tenantInfoService;

    /**
     * 租户列表
     *
     * @param pageQueryDTO 分页查询的dto对象
     * @return 租户列表集合
     */
    @GetMapping("/list")
    @Operation(summary = "租户列表")
    public BaseResponse<PageResultDTO<TenantInfoDTO>> pageList(PageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(tenantInfoService.pageList(pageQueryDTO));
    }

    /**
     * 通过租户id获取租户详情
     *
     * @param id 租户id
     * @return 租户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "通过租户id获取租户详情")
    public BaseResponse<TenantInfoDTO> getTenantInfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(tenantInfoService.findById(id));
    }
}
