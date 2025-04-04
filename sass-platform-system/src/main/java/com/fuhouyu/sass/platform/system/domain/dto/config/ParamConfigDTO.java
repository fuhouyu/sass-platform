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
package com.fuhouyu.sass.platform.system.domain.dto.config;

import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * <p>
 * 参数配置的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/2 21:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ParamConfigDTO", description = "参数配置dto对象")
public class ParamConfigDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 89612357615237182L;

    @Schema(name = "id", description = "主键id")
    private Long id;

    @Schema(name = "configName", description = "配置名称")
    @NotEmpty(message = "配置名称未输入")
    private String configName;

    @Schema(name = "configKey", description = "配置key")
    @NotEmpty(message = "配置键未输入")
    private String configKey;

    @Schema(name = "configValue", description = "配置value")
    @NotEmpty(message = "配置值未输入")
    private String configValue;

    @Schema(name = "groupKey", description = "分组标识")
    @NotEmpty(message = "分组标识未输入")
    private String groupKey;

    @Schema(name = "remark", description = "备注")
    @Length(max = 500, message = "超出最大字符限制")
    private String remark;

    @Schema(name = "isAllowModified", description = "是否允许修改")
    private Boolean isAllowModified;

    public ParamConfigDTO() {
        this.isAllowModified = true;
    }
}
