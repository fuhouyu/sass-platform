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

import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.sass.platform.system.service.SiteSettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网站设置 前端控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:32
 */
@RestController
@RequestMapping("/v1/site-setting")
@Tag(name = "网站设置 web接口")
@Slf4j
@Validated
@RequiredArgsConstructor
@LogModule("网站设置模块")
public class SiteSettingController {

    private final SiteSettingService siteSettingService;

}
