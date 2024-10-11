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
package com.fuhouyu.sass.interfaces.controller.dto;

import com.fuhouyu.sass.interfaces.controller.enums.PageQueryDirectionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 分页查询基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/11 00:28
 */
@Data
@Schema(name = "BasePageQuery", description = "基类分页查询")
public class BasePageQueryDTO implements Serializable {

    @Schema(name = "pageNum", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(name = "keyword", description = "关键字查询", requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyword;

    @Schema(name = "direction", description = "排序方向", requiredMode = Schema.RequiredMode.REQUIRED)
    private PageQueryDirectionEnum direction;

    @Schema(name = "sortColumn", description = "排序列", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sortColumn;

    public BasePageQueryDTO() {
        this.pageNum = 1;
        this.pageSize = 10;
    }
}
