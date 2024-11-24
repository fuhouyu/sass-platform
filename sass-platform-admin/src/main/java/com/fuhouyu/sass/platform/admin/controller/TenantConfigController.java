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
import com.fuhouyu.sass.platform.system.dto.tenant.TenantConfigDTO;
import com.fuhouyu.sass.platform.system.service.TenantConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/11 21:45
 */
@RestController
@RequestMapping("/v1/tenant-config")
@Tag(name = "租户配置 前端控制层")
@RequiredArgsConstructor
@Slf4j
@Valid
public class TenantConfigController {

    private final TenantConfigService tenantConfigService;

    /**
     * 租户配置列表
     *
     * @param pageQueryDTO 分页查询的dto对象
     * @return 租户配置列表集合
     */
    @GetMapping("/list")
    @Operation(summary = "租户配置列表")
    public BaseResponse<PageResultDTO<TenantConfigDTO>> pageList(PageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(tenantConfigService.pageList(pageQueryDTO));
    }

    /**
     * 保存租户配置
     *
     * @param tenantConfigDTO 租户配置的dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存租户配置")
    public BaseResponse<Long> saveTenant(@RequestBody @Validated TenantConfigDTO tenantConfigDTO) {
        return ResponseHelper.success(tenantConfigService.save(tenantConfigDTO));
    }

    /**
     * 修改租户配置
     *
     * @param tenantConfigDTO 租户配置的dto对象
     * @return 主键id
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改租户配置")
    public BaseResponse<Long> editTenant(
            @PathVariable("id") Long id,
            @RequestBody @Validated TenantConfigDTO tenantConfigDTO) {
        tenantConfigDTO.setId(id);
        tenantConfigService.edit(tenantConfigDTO);
        return ResponseHelper.success();
    }

    /**
     * 通过租户id获取租户配置详情
     *
     * @param id 租户id
     * @return 租户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "租户配置详情")
    public BaseResponse<TenantConfigDTO> getTenantConfig(@PathVariable("id") Long id) {
        return ResponseHelper.success(tenantConfigService.findById(id));
    }


    /**
     * 通过id删除租户配置
     *
     * @param ids 租户配置ids
     * @return true删除成功
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除租户配置")
    public BaseResponse<Boolean> deleteTenantConfig(@RequestBody @Size(min = 1, message = "配置未选择")
                                                    @NotNull(message = "配置未选择") List<Long> ids) {
        int count = this.tenantConfigService.removeByIds(ids);
        return ResponseHelper.success(count > 0);
    }
}
