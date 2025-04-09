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
package com.fuhouyu.sass.platform.system.domain.dto.dict;

import com.fuhouyu.framework.common.annotations.ParamErrorResponse;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.enums.response.DictItemResponseStatusEnum;
import com.fuhouyu.sass.platform.system.enums.response.DictTypeResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * <p>
 * 字典项dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 18:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DictItemDTO", description = "字典项dto对象")
public class DictItemDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 6812254385712345112L;

    @Schema(name = "id", description = "主键id，仅返回")
    private Long id;

    @Schema(name = "dictCode", description = "字典编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @ParamErrorResponse(using = DictTypeResponseStatusEnum.class,
            value = "DICT_TYPE_CODE_NOT_NULL")
    private String dictCode;

    @Schema(name = "itemName", description = "字典项名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @ParamErrorResponse(using = DictItemResponseStatusEnum.class,
            value = "DICT_ITEM_NAME_NOT_NULL")
    private String itemName;

    @Schema(name = "itemCode", description = "字典项编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @ParamErrorResponse(using = DictItemResponseStatusEnum.class,
            value = "DICT_ITEM_CODE_NOT_NULL")
    private String itemCode;

    @Schema(name = "isAllowModified", description = "是否允许修改，仅返回")
    private Boolean isAllowModified;

    @Schema(name = "isEnabled", description = "状态：启用/禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @ParamErrorResponse(using = DictItemResponseStatusEnum.class,
            value = "STATUS_NOT_NULL")
    private Boolean isEnabled;

    @Schema(name = "displayOrder", description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @ParamErrorResponse(using = DictItemResponseStatusEnum.class,
            value = "DISPLAY_ORDER_NOT_NULL")
    private Integer displayOrder;

    @Schema(name = "remark", description = "备注")
    @Length(max = 255)
    @ParamErrorResponse(using = DictItemResponseStatusEnum.class,
            value = "REMARK_LENGTH_TOO_LONG")
    private String remark;
}
