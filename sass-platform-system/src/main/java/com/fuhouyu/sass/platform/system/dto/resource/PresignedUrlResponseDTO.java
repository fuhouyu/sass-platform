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
package com.fuhouyu.sass.platform.system.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预签名url响应结果
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/16 22:11
 */
@Schema(name = "ResourcePresignedUrlResponseDTO", description = "预签名url响应结果")
@Data
@Builder
public class PresignedUrlResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1547897123654123879L;

    @Schema(name = "presignedUrl", description = "预签名url")
    private String presignedUrl;

    @Schema(name = "objectKey", description = "对象key")
    private String objectKey;

    @Schema(name = "singedHeaders", description = "签名头信息")
    private Map<String, List<String>> singedHeaders;
}
