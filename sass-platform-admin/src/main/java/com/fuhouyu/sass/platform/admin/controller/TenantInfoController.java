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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.*;
import com.fuhouyu.sass.platform.system.enums.response.TenantResponseStatusEnum;
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
@LogModule("租户模块")
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
    @LogRecord(operationType = OperationTypeEnum.CREATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Long> saveTenant(@RequestBody @Valid SaveOrEditTenantInfoDTO tenantInfoDTO) {
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
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Long> editTenant(
            @PathVariable("id") Long id,
            @RequestBody @Valid SaveOrEditTenantInfoDTO tenantInfoDTO) {
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
    @LogRecord(operationType = OperationTypeEnum.DELETE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Boolean> deleteTenantInfo(@RequestBody @NotEmpty(message = "未选择需要删除的租户")
                                                  List<Long> ids) {
        if (ids.contains(ContextHolderStrategy.getContext().getUser().getTenantId())) {
            throw new ServiceException(TenantResponseStatusEnum.TENANT_NO_PERMISSION);
        }
        return ResponseHelper.success(this.tenantInfoService.removeByIds(ids));
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
     * 查询出当前用户关联的租户
     *
     * @return 用户关联的租户
     */
    @GetMapping("/me")
    @Operation(summary = "查询出当前用户关联的租户")
    public BaseResponse<TenantInfoDTO> findTenantForMe() {
        return ResponseHelper.success(this.tenantInfoService.findDetailById(ContextHolderStrategy.getContext().getUser().getTenantId()));
    }


    /**
     * 修改状态
     *
     * @param id      主键id
     * @param enabled true / false
     * @return void
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "修改状态")
    @Parameter(name = "enabled", description = "true 启用 false 禁用")
    @PreAuthorize("@auth.hasAnyPermission('tenant:edit')")
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Void> editStatus(@PathVariable("id") Long id,
                                         @RequestParam("enabled") Boolean enabled) {
        if (Objects.equals(id, ContextHolderStrategy.getContext().getUser().getTenantId())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前租户禁止修改");
        }
        TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
        tenantInfoDTO.setId(id);
        tenantInfoDTO.setIsEnabled(enabled);
        this.tenantInfoService.edit(tenantInfoDTO);
        return ResponseHelper.success();
    }


    /**
     * 重置租户管理员密码
     *
     * @param id 租户id
     * @return void
     */
    @GetMapping("/{id}/reset")
    @Operation(summary = "重置租户管理员密码")
    public BaseResponse<Void> resetPassword(@PathVariable("id") Long id) {
        this.tenantInfoService.resetPassword(id);
        return ResponseHelper.success();
    }

}
