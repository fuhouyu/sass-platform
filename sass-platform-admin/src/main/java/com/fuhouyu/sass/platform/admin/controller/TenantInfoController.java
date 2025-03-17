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
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.BasicTenantDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantPageQueryDTO;
import com.fuhouyu.sass.platform.system.service.TenantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
@Tag(name = "租户 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TenantInfoController {

    private final TenantInfoService tenantInfoService;

    /**
     * 保存租户
     *
     * @param tenantInfoDTO 租户的dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存租户")
    @PreAuthorize("@auth.hasAnyPermission('tenant:add')")
    public BaseResponse<Long> saveTenant(@RequestBody @Valid TenantInfoDetailDTO tenantInfoDTO) {
        return ResponseHelper.success(tenantInfoService.saveTenantDetail(tenantInfoDTO));
    }

    /**
     * 修改租户
     *
     * @param tenantInfoDTO 租户的dto对象
     * @return 主键id
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改租户")
    @PreAuthorize("@auth.hasAnyPermission('tenant:edit')")
    public BaseResponse<Long> editTenant(
            @PathVariable("id") Long id,
            @RequestBody @Valid TenantInfoDetailDTO tenantInfoDTO) {
        tenantInfoDTO.setId(id);
        this.tenantInfoService.editDetail(tenantInfoDTO);
        return ResponseHelper.success();
    }
    
    /**
     * 租户列表
     *
     * @param pageQueryDTO 分页查询的dto对象
     * @return 租户列表集合
     */
    @GetMapping("/page")
    @Operation(summary = "租户列表")
    @PreAuthorize("@auth.hasAnyPermission('tenant:list')")
    public BaseResponse<PageResultDTO<TenantInfoDTO>> pageList(TenantPageQueryDTO pageQueryDTO) {
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
    @PreAuthorize("@auth.hasAnyPermission('tenant:query')")
    public BaseResponse<TenantInfoDetailDTO> getTenantInfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(tenantInfoService.findDetailById(id));
    }

    /**
     * 通过id删除租户
     *
     * @param ids 租户ids
     * @return true删除成功
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除租户")
    @PreAuthorize("@auth.hasAnyPermission('tenant:delete')")
    public BaseResponse<Boolean> deleteTenantInfo(@RequestBody @NotEmpty(message = "未选择需要删除的租户")
                                                  List<Long> ids) {
        int count = this.tenantInfoService.removeByIds(ids);
        return ResponseHelper.success(count > 0);
    }


    /**
     * 检查租户编码是否存在
     *
     * @param tenantCode 租户编码
     * @return true 已存在 false 不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查租户编码是否已存在，true 已存在")
    @Parameter(name = "tenantCode", description = "租户编码")
    public BaseResponse<Boolean> checkTenantCodeExists(@RequestParam("tenantCode") String tenantCode) {
        return ResponseHelper.success(Objects.nonNull(this.tenantInfoService.findByTenantCode(tenantCode)));
    }


    /**
     * 查询出当前用户关联的租户
     *
     * @return 用户关联的租户
     */
    @GetMapping("/me")
    @Operation(summary = "查询出当前用户关联的租户")
    public BaseResponse<List<TenantInfoDTO>> findTenantForMe() {
        return ResponseHelper.success(this.tenantInfoService.findTenantByUserId(ContextHolderStrategy.getContext().getUser().getId()));
    }

    /**
     * 获取租户的列表
     *
     * @return 租户列表集合
     */
    @GetMapping("/list")
    @Operation(summary = "租户列表")
    @NoAuth
    public BaseResponse<List<BasicTenantDTO>> tenantList() {
        return ResponseHelper.success(this.tenantInfoService.findTenantList());
    }


    /**
     * 租户切换
     *
     * @param id 租户id
     * @return void
     */
    @GetMapping("/switch/{id}")
    @Operation(summary = "租户切换")
    public BaseResponse<Void> switchTenant(@PathVariable("id") Long id) {
        this.tenantInfoService.switchTenant(id);
        return ResponseHelper.success();
    }
    
}
