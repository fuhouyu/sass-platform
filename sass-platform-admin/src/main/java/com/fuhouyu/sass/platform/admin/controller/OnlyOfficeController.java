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
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeCallbackDTO;
import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeCallbackResponseDTO;
import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeResponseDTO;
import com.fuhouyu.sass.platform.system.properties.OnlyOfficeDocumentProperties;
import com.fuhouyu.sass.platform.system.service.OnlyOfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
@ConditionalOnProperty(prefix = OnlyOfficeDocumentProperties.PREFIX,
        value = "enabled", havingValue = "true")
public class OnlyOfficeController {

    private final OnlyOfficeService onlyOfficeService;

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
        return ResponseHelper.success(this.onlyOfficeService.view(id, mode));

    }


    /**
     * 文件保存的回调接口
     *
     * @param id                    文件id
     * @param token                 用户token
     * @param onlyOfficeCallbackDTO 回调的dto对象
     * @return void
     */
    @PostMapping("/{id}/callback")
    @Operation(summary = "文件保存的回调接口")
    @NoAuth
    public OnlyOfficeCallbackResponseDTO saveFile(@PathVariable("id") Long id,
                                                  @RequestParam("token") String token,
                                                  @RequestBody OnlyOfficeCallbackDTO onlyOfficeCallbackDTO) {
        if (onlyOfficeCallbackDTO.getStatus() != 2) {
            return OnlyOfficeCallbackResponseDTO.success();
        }
        LoggerUtil.info(log, "文件保存的回调接口,文件id:{}", onlyOfficeCallbackDTO.getKey());
        this.onlyOfficeService.saveFile(id, token, onlyOfficeCallbackDTO);

        return OnlyOfficeCallbackResponseDTO.success();
    }

}
