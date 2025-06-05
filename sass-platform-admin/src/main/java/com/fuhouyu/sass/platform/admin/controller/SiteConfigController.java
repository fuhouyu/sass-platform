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
import com.fuhouyu.sass.platform.system.domain.dto.site.SiteConfigDTO;
import com.fuhouyu.sass.platform.system.service.SiteConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站设置 前端控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:32
 */
@RestController
@RequestMapping("/v1/site-config")
@Tag(name = "网站设置 web接口")
@Slf4j
@Validated
@RequiredArgsConstructor
@LogModule("网站设置模块")
public class SiteConfigController {

    private final SiteConfigService siteConfigService;


    /**
     * 获取网站设置
     *
     * @return 网站设置
     */
    @GetMapping
    @Operation(summary = "获取网站设置")
    public BaseResponse<SiteConfigDTO> getSiteSetting() {
        return ResponseHelper.success(siteConfigService.getSiteConfig());
    }


    /**
     * 更新网站设置
     *
     * @param siteConfig 网站设置DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新网站设置")
    public BaseResponse<Boolean> updateSiteConfig(@PathVariable("id") Long id,
                                                  SiteConfigDTO siteConfig) {
        return ResponseHelper.success(siteConfigService.updateSiteConfig(siteConfig));
    }

}
