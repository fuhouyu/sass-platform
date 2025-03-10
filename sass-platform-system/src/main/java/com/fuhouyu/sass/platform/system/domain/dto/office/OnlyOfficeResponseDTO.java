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
package com.fuhouyu.sass.platform.system.domain.dto.office;

import com.onlyoffice.model.documenteditor.Config;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * office响应dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 20:47
 */
@Data
@Schema(name = "OnlyOfficeResponseDTO", description = "office 响应的dto对象")
@Builder
public class OnlyOfficeResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1571234587142981273L;

    @Schema(name = "config", description = "office配置", requiredMode = Schema.RequiredMode.REQUIRED)
    private Config config;

    @Schema(name = "documentServerApiUrl", description = "office api url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String documentServerApiUrl;

    @Schema(name = "documentServerUrl", description = "office 服务url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String documentServerUrl;
}
