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
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoCountryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitSummaryDTO;
import com.fuhouyu.sass.platform.system.service.MatomoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * matomo 数据监控
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 19:29
 */
@RestController
@RequestMapping("/v1/matomo")
@Tag(name = "matomo web接口")
@Validated
@RequiredArgsConstructor
@ConditionalOnBean(MatomoService.class)
public class MatomoController {


    private final MatomoService matomoService;


    /**
     * 获取访问概要
     *
     * @param queryDTO 查询参数
     * @return 访问概要
     */
    @Operation(summary = "获取访问概要")
    @GetMapping("/visit-summary")
    public BaseResponse<List<MatomoVisitSummaryDTO>> getVisitSummary(MatomoVisitQueryDTO queryDTO) {
        return ResponseHelper.success(this.matomoService.getVisitSummary(queryDTO));
    }

    /**
     * 获取国家访问统计
     *
     * @param queryDTO 查询参数
     * @return 国家访问统计
     */
    @Operation(summary = "获取国家访问统计")
    @GetMapping("/country-visit")
    public BaseResponse<List<MatomoCountryDTO>> getCountryVisit(MatomoVisitQueryDTO queryDTO) {
        return ResponseHelper.success(this.matomoService.getCountryVisit(queryDTO));
    }
}
