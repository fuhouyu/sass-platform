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
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.ParamConfigService;
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

/**
 * <p>
 * 参数配置控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/11 21:45
 */
@RestController
@RequestMapping("/v1/param-config")
@Tag(name = "参数配置 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule("参数配置")
public class ParamConfigController {

    private final ParamConfigService paramConfigService;

    /**
     * 保存参数配置
     *
     * @param paramConfigDTO 参数配置的dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存参数配置")
    @PreAuthorize("@auth.hasAnyPermission('system:param-config:add')")
    @LogRecord(operationType = OperationTypeEnum.CREATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Long> saveTenant(@RequestBody @Valid ParamConfigDTO paramConfigDTO) {
        return ResponseHelper.success(paramConfigService.save(paramConfigDTO));
    }

    /**
     * 修改参数配置
     *
     * @param paramConfigDTO 参数配置的dto对象
     * @return 主键id
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改参数配置")
    @PreAuthorize("@auth.hasAnyPermission('system:param-config:edit')")
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
    public BaseResponse<Long> editTenant(
            @PathVariable("id") Long id,
            @RequestBody @Valid ParamConfigDTO paramConfigDTO) {
        paramConfigDTO.setId(id);
        this.paramConfigService.edit(paramConfigDTO);
        return ResponseHelper.success();
    }

    /**
     * 参数配置列表
     *
     * @param pageQueryDTO 分页查询的dto对象
     * @return 参数配置列表集合
     */
    @GetMapping("/page")
    @Operation(summary = "参数配置列表")
    @PreAuthorize("@auth.hasAnyPermission('system:param-config:list')")
    public BaseResponse<PageResultDTO<ParamConfigDTO>> pageList(ParamConfigPageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(paramConfigService.pageList(pageQueryDTO));
    }

    /**
     * 通过参数配置id获取参数配置详情
     *
     * @param id 参数配置id
     * @return 参数配置详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "通过参数配置id获取参数配置详情")
    @PreAuthorize("@auth.hasAnyPermission('system:param-config:query')")
    public BaseResponse<ParamConfigDTO> getTenantInfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(paramConfigService.findById(id));
    }

    /**
     * 通过id删除参数配置
     *
     * @param ids 参数配置ids
     * @return true删除成功
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除参数配置")
    @PreAuthorize("@auth.hasAnyPermission('system:param-config:delete')")
    @LogRecord(operationType = OperationTypeEnum.DELETE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Boolean> deleteTenantInfo(@RequestBody @NotEmpty(message = "未选择需要删除的参数配置")
                                                  List<Long> ids) {
        int count = this.paramConfigService.removeByIds(ids);
        return ResponseHelper.success(count > 0);
    }


    /**
     * 检查参数配置编码是否存在
     *
     * @param configKey 参数配置编码
     * @return true 已存在 false 不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查参数配置编码是否已存在，true 已存在")
    @Parameter(name = "configKey", description = "参数配置编码")
    public BaseResponse<Boolean> checkTenantCodeExists(@RequestParam("configKey") String configKey) {
        return ResponseHelper.success(this.paramConfigService.checkConfigKeyExists(configKey));
    }
}
