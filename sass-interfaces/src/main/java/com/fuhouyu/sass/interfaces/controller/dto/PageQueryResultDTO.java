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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分页返回的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/11 15:22
 */
@Getter
@Setter
@ToString
public class PageQueryResultDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1238719237918263821L;

    @Schema(name = "pageNum", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(name = "total", description = "总条数", requiredMode = Schema.RequiredMode.REQUIRED, implementation = String.class)
    private Long total;

    @Schema(name = "list", description = "分页数据")
    private transient List<T> list;

    public PageQueryResultDTO(Integer pageNum, Integer pageSize, Long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageQueryResultDTO(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }
}
