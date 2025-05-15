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

import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典类型分页查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 17:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DictTypePageQueryDTO", description = "字典类型分页查询的dto对象")
public class DictTypePageQueryDTO extends PageQueryDTO<DictType> {

    @Schema(name = "dictCode", description = "字典类型编码")
    private String dictCode;

    @Schema(name = "isEnabled", description = "启禁用状态")
    private Boolean isEnabled;

}
