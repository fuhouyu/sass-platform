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
package com.fuhouyu.sass.platform.system.domain.dto.organization;

import com.fuhouyu.framework.common.annotations.ParamErrorResponse;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.enums.response.OrganizationResponseStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 组织dto传输对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 21:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "OrganizationDTO", description = "组织dto对象")
public class OrganizationDTO extends BaseDTO {

    @Schema(name = "id", description = "组织ID，仅返回")
    private Long id;

    @Schema(name = "parentId", description = "父组织ID", defaultValue = "-1")
    private Long parentId;

    @Schema(name = "organizationName", description = "组织名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "组织名称未输入")
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "ORGANIZATION_NAME_NOT_NULL")
    private String organizationName;

    @Schema(name = "organizationCode", description = "组织名称")
    @NotEmpty(message = "组织编码未输入")
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "ORGANIZATION_CODE_NOT_NULL")
    private String organizationCode;

    @Schema(name = "organizationType", description = "组织类型")
    @NotEmpty(message = "组织类型未输入")
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "ORGANIZATION_TYPE_NOT_NULL")
    private String organizationType;

    @Schema(name = "isEnabled", description = "是否启用")
    @NotNull(message = "启禁用状态未选择")
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "STATUS_NOT_NULL")
    private Boolean isEnabled;

    @Schema(name = "isLeaf", description = "是否为叶子节点，仅返回")
    private Boolean isLeaf;

    @Schema(name = "remark", description = "备注信息")
    @Length(max = 500)
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "REMARK_LENGTH_TOO_LONG")
    private String remark;

    @Schema(name = "显示顺序", description = "显示顺序")
    @NotNull(message = "显示顺序未输入")
    @ParamErrorResponse(using = OrganizationResponseStatusEnums.class,
            value = "DISPLAY_ORDER_NOT_NULL")
    private Integer displayOrder;


}
