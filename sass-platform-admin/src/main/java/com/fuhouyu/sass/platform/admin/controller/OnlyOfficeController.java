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
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.dto.office.OnlyOfficeResponseDTO;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.documenteditor.Config;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * <p>
 * office web接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 16:27
 */
@RestController
@RequestMapping("/v1/office")
@Tag(name = "office web接口")
@Validated
@RequiredArgsConstructor
public class OnlyOfficeController {

    private final ConfigService configService;

    private final UrlManager urlManager;


    /**
     * 在线预览
     *
     * @return void
     */
    @GetMapping("/{id}")
    @Operation(summary = "在线预览")
    @NoAuth
    public BaseResponse<OnlyOfficeResponseDTO> view(@PathVariable("id") Long id,
                                                    @RequestParam(required = false, defaultValue = "view") String mode) {
        Config config = configService.createConfig(String.valueOf(id), Mode.valueOf(mode.toUpperCase(Locale.ROOT)), Type.DESKTOP);

        return ResponseHelper.success(
                OnlyOfficeResponseDTO.builder()
                        .config(config)
                        .documentServerApiUrl(urlManager.getDocumentServerApiUrl())
                        .documentServerUrl(urlManager.getDocumentServerUrl())
                        .build());
    }

}
