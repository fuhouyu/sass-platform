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
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.system.domain.dto.ValidGroups;
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationDTO;
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 应用web控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 21:05
 */
@RestController
@RequestMapping("/v1/application")
@Tag(name = "应用 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule(value = "应用模块")
public class ApplicationController {


    private final ApplicationService applicationService;


    /**
     * 通过id查询应用详情
     *
     * @return 应用详情dto对象
     */
    @Operation(summary = "通过id查询应用详情")
    @GetMapping
    @PreAuthorize("@auth.hasAnyPermission('application:query')")
    @Parameter(name = "clientId", description = "客户端id")
    public BaseResponse<ApplicationDTO> getById(@RequestParam("clientId") String clientId) {
        return ResponseHelper.success(this.applicationService.findById(clientId));
    }

    /**
     * 修改当前应用的详情
     *
     * @param applicationDTO 应用dto对象
     * @return restResult
     */
    @PutMapping
    @Operation(summary = "修改应用详情")
    @PreAuthorize("@auth.hasAnyPermission('application:edit')")
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> editApplicationInfo(@Valid @RequestBody ApplicationDTO applicationDTO) {
        this.applicationService.edit(applicationDTO);
        return ResponseHelper.success();
    }

    /**
     * 保存应用信息
     *
     * @param applicationDTO 应用dto对象
     * @return 主键id
     */
    @Operation(summary = "保存应用信息")
    @PostMapping
    @PreAuthorize("@auth.hasAnyPermission('application:add')")
    @LogRecord(operationType = OperationTypeEnum.CREATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Void> saveApplication(@RequestBody @Validated({ValidGroups.SaveGroup.class}) ApplicationDTO applicationDTO) {
        this.applicationService.save(applicationDTO);
        return ResponseHelper.success();
    }

    /**
     * 分页查询应用列表
     *
     * @param applicationPageQueryDTO 应用分页查询对象
     * @return 应用列表集合
     */
    @GetMapping("/page")
    @Operation(summary = "获取应用列表")
    @PreAuthorize("@auth.hasAnyPermission('application:list')")
    public BaseResponse<PageResultDTO<ApplicationDTO>> pageList(ApplicationPageQueryDTO applicationPageQueryDTO) {
        return ResponseHelper.success(this.applicationService.pageList(applicationPageQueryDTO));
    }

    /**
     * 通过应用id删除应用
     *
     * @param clientIds 应用id集合
     * @return 成功响应
     */
    @Operation(summary = "通过应用id删除应用")
    @DeleteMapping
    @PreAuthorize("@auth.hasAnyPermission('application:delete')")
    @LogRecord(operationType = OperationTypeEnum.DELETE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> removeApplicationList(
            @RequestBody
            @Size(min = 1, message = "需要删除的应用不能为空")
            @NotNull(message = "需要删除的应用不能为空") Set<String> clientIds) {
        this.applicationService.removeByIds(clientIds);
        return ResponseHelper.success();
    }


    /**
     * 校验应用名是否存在
     *
     * @param clientId 应用名
     * @return true 已存在，false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "校验应用名是否存在，如果存在，则返回true")
    @Parameter(name = "clientId", description = "客户端id")
    public BaseResponse<Boolean> validApplicationNameExists(@RequestParam("clientId") String clientId) {
        return ResponseHelper.success(Objects.nonNull(this.applicationService.findById(clientId)));
    }

    /**
     * 修改状态
     *
     * @param clientId 客户端id
     * @param enabled  true / false
     * @return void
     */
    @PutMapping("/status")
    @Operation(summary = "修改状态")
    @Parameter(name = "enabled", description = "true 启用 false 禁用")
    @Parameter(name = "clientId", description = "客户端id")
    @PreAuthorize("@auth.hasAnyPermission('application:edit')")
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Void> editStatus(@RequestParam("clientId") String clientId,
                                         @RequestParam("enabled") Boolean enabled) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setClientId(clientId);
        applicationDTO.setIsEnabled(enabled);
        this.applicationService.edit(applicationDTO);
        return ResponseHelper.success();
    }


}
