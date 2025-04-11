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
package com.fuhouyu.sass.platform.system.domain.dto.application;

import com.fuhouyu.framework.common.annotations.ParamErrorResponse;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.enums.response.ApplicationResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * <p>
 * 应用dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 19:42
 */
@Data
@Schema(name = "ApplicationDTO", description = "应用dto对象")
@EqualsAndHashCode(callSuper = true)
public class ApplicationDTO extends BaseDTO {

    @Schema(name = "clientId", description = "客户端id")
    @NotEmpty
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_ID_NOT_NULL")
    private String clientId;

    @Schema(name = "clientSecret", description = "客户端密钥")
    private String clientSecret;

    @Schema(name = "clientName", description = "客户端名称")
    @NotEmpty
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_NAME_NOT_NULL")
    private String clientName;

    @Schema(name = "icon", description = "icon")
    private Long icon;

    @Schema(name = "remark", description = "remark")
    private String remark;

    @Schema(name = "redirectUris", description = "回调地址（多个以逗号分隔）")
    @NotEmpty
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_REDIRECT_URL_NOT_NULL")
    private Set<String> redirectUris;

    @Schema(name = "scopes", description = "授权范围（多个以逗号分隔）")
    private Set<String> scopes;

    @Schema(name = "grantTypes", description = "支持的授权类型（多个以逗号分隔）")
    @NotEmpty
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_GRANT_TYPE_NOT_NULL")
    private Set<String> grantTypes;

    @Schema(name = "accessTokenValidity", description = "访问令牌有效期（单位：秒）")
    @NotNull
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_ACCESS_TOKEN_VALIDITY_NOT_NULL")
    private Integer accessTokenValidity;

    @Schema(name = "refreshTokenValidity", description = "刷新令牌有效期（单位：秒）")
    @NotNull
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_REFRESH_TOKEN_VALIDITY_NOT_NULL")
    private Integer refreshTokenValidity;

    @Schema(name = "published", description = "是否上架：TRUE=已上架，FALSE=未上架")
    private Boolean published;

    @Schema(name = "ipWhitelist", description = "白名单设置")
    private Set<String> ipWhitelist;

    @Schema(name = "isEnabled", description = "是否启用：true 启用，false 禁用")
    @NotNull
    @ParamErrorResponse(using = ApplicationResponseStatusEnum.class,
            value = "CLIENT_STATUS_NOT_NULL")
    private Boolean isEnabled;


    public ApplicationDTO() {
        this.scopes = Set.of("ALL");
    }
}
