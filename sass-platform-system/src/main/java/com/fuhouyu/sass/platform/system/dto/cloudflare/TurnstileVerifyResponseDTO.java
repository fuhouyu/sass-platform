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
package com.fuhouyu.sass.platform.system.dto.cloudflare;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Turnstile 验证的dto 响应对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/2 21:31
 */
@Data
@Schema(name = "TurnstileVerifyResponseDTO", description = "cloudflare 验证dto响应的对象")
public class TurnstileVerifyResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1541239871235162423L;

    @Schema(name = "success", description = "验证结果", defaultValue = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean success;

    @Schema(name = "challengeTs", description = "验证时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String challengeTs;

    @Schema(name = "hostname", description = "域名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String hostname;

    @Schema(name = "errorCodes", description = "错误码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String errorCodes;

    @Schema(name = "action", description = "验证行为", requiredMode = Schema.RequiredMode.REQUIRED)
    private String action;

    @Schema(name = "cdata", description = "验证数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cdata;

    @Schema(name = "metadata", description = "验证数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Object> metadata;
}
