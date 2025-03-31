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
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogDTO;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 操作日志 web控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/31 19:26
 */
@RestController
@RequestMapping("/v1/log")
@Tag(name = "log web接口")
@Validated
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    /**
     * 获取日志
     *
     * @param operationLogPageQueryDTO 操作日志查询的dto对象
     * @return 日志分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "查询操作日志")
    public BaseResponse<PageResultDTO<OperationLogDTO>> page(OperationLogPageQueryDTO operationLogPageQueryDTO) {
        return ResponseHelper.success(this.operationLogService.page(operationLogPageQueryDTO));
    }
}
