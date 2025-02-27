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
import java.util.Map;

/**
 * <p>
 * s3 sts 临时token
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 20:03
 */
@Data
@Schema(name = "StsTemporaryTokenResponseDTO", description = "s3 sts 临时token响应")
@Builder
public class StsTemporaryTokenResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 154123678635141238L;

    @Schema(name = "accessKeyId", description = "ak")
    private String accessKeyId;

    @Schema(name = "secretAccessKey", description = "sk")
    private String secretAccessKey;

    @Schema(name = "stsToken", description = "临时token")
    private String stsToken;

    @Schema(name = "bucketName", description = "桶名")
    private String bucketName;

    @Schema(name = "region", description = "region")
    private String region;

    @Schema(name = "endpoint", description = "endpoint")
    private String endpoint;

    @Schema(name = "enabledPathStyle", description = "是否使用path style")
    private Boolean enabledPathStyle;

    @Schema(name = "objectsMap", description = "对象map")
    private Map<String, String> objectsMap;
}
