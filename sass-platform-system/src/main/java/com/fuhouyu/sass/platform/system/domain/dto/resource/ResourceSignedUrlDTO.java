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
package com.fuhouyu.sass.platform.system.domain.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 资源签名参数dto
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/8 23:42
 */
@Schema(name = "ResourceSignedUrlDTO", description = "资源签名参数")
@Data
public class ResourceSignedUrlDTO implements Serializable {


    @Schema(name = "signature", description = "签名参数")
    private String signature;

    @Schema(name = "expires", description = "过期时间")
    private Long expires;

    @Schema(name = "nonce", description = "随机串")
    private String nonce;
}
