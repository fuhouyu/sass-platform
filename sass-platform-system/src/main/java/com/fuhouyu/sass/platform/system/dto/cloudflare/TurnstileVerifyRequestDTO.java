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

/**
 * <p>
 * 验证的请求dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/2 21:36
 */
@Data
@Schema(name = "TurnstileVerifyRequestDTO", description = "cloudflare turnstile请求的dto对象")
public class TurnstileVerifyRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7812346541232215612L;

    @Schema(name = "secret", description = "小组件的密钥。密钥可以在 Cloudflare 仪表板的 Turnstile 下的小部件设置下找到", requiredMode = Schema.RequiredMode.REQUIRED)
    private String secret;

    @Schema(name = "response", description = "Turnstile 客户端渲染在您的网站上提供的响应", requiredMode = Schema.RequiredMode.REQUIRED)
    private String response;

    @Schema(name = "remoteIp", description = "访客的 IP 地址。", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remoteIp;

    @Schema(name = "idempotencyKey", description = "要与响应关联的 UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String idempotencyKey;
}
