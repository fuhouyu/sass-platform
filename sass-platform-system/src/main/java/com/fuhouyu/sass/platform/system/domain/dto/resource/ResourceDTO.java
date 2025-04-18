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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.common.annotations.ParamErrorResponse;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.enums.response.ResourceResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 资源dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ResourceDTO", description = "资源dto对象")
public class ResourceDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 167123549812397819L;

    @Schema(name = "id", description = "资源id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(name = "parentId", description = "父资源id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentId;

    @Schema(name = "name", description = "资源名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "名称未输入")
    @ParamErrorResponse(using = ResourceResponseStatusEnum.class,
            value = "RESOURCE_NAME_NOT_NULL")
    private String name;

    @Schema(name = "size", description = "资源大小", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long size;

    @Schema(name = "eTag", description = "资源etag", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("eTag")
    @JsonAlias({
            "eTag",
            "etag"
    })
    @NotEmpty(message = "etag未输入")
    @ParamErrorResponse(using = ResourceResponseStatusEnum.class,
            value = "RESOURCE_ETAG_NOT_NULL")
    private String etag;

    @Schema(name = "mimeType", description = "资源类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String mimeType;

    @Schema(name = "objectKey", description = "对象key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String objectKey;

    @Schema(name = "version", description = "版本号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer version;

    @Schema(name = "isPublic", description = "是否公开", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否公开未选择")
    @ParamErrorResponse(using = ResourceResponseStatusEnum.class,
            value = "RESOURCE_PUBLIC_NOT_NULL")
    private Boolean isPublic;

    @Schema(name = "category", description = "资源分类,根据mimeType获取分类名称")
    private String category;

    @Schema(name = "isDirectory", description = "是否是目录", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否目录未选择")
    @ParamErrorResponse(using = ResourceResponseStatusEnum.class,
            value = "RESOURCE_DIRECTORY_NOT_NULL")
    private Boolean isDirectory;

    @Schema(name = "ownerTenantId", description = "租户id，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ownerTenantId;

}
