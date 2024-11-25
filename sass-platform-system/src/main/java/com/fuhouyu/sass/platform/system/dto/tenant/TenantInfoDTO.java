/*
 * Copyright 2024-2024 the original author or authors.
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
package com.fuhouyu.sass.platform.system.dto.tenant;

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 租户dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 16:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TenantInfoDTO", description = "租户dto对象")
public class TenantInfoDTO extends BaseDTO {

    @Schema(name = "id", description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(name = "tenantCode", description = "租户编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户编码未输入")
    private String tenantCode;

    @Schema(name = "tenantName", description = "租户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户名称未输入")
    private String tenantName;

    @Schema(name = "tenantType", description = "租户类型", requiredMode = Schema.RequiredMode.REQUIRED)
    // FIXME 先写死默认值
    private String tenantType = "COMPANY";

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "icon", description = "租户图标", requiredMode = Schema.RequiredMode.REQUIRED)
    private String icon;

    @Schema(name = "contactPerson", description = "联系人")
    private String contactPerson;

    @Schema(name = "contactInfo", description = "联系方式")
    private String contactInfo;

    @Schema(name = "isEnabled", description = "状态：true 启用")
    private Boolean isEnabled;

    @Schema(name = "permissionIds", description = "权限id集合")
    @NotEmpty(message = "权限未选择")
    private List<Long> permissionIds;
}
