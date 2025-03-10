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

import com.fuhouyu.sass.platform.system.enums.PresignedUrlTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 资源预签名dto请求
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/16 20:38
 */
@Data
@Schema(name = "PresignedUrlRequestDTO", description = "资源预签名dto请求")
public class PresignedUrlRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1941236546871923564L;

    @Schema(name = "businessName", description = "业务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessName;


    @Schema(name = "presignedUrlType", description = "预签名url类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private PresignedUrlTypeEnum presignedUrlType;

    @Schema(name = "partNumber", description = "分片号")
    private Integer partNumber;

    @Schema(name = "objectKey", description = "对象key")
    private String objectKey;

    @Schema(name = "uploadId", description = "分片上传的id，如果是分片上传，该值不能为空")
    private String uploadId;
}
