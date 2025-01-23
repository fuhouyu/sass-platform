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
package com.fuhouyu.sass.platform.system.dto.page;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.github.pagehelper.util.SqlSafeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 分页查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:49
 */
@ToString(callSuper = true)
@Getter
@Setter
@Slf4j
@Schema(name = "PageQueryDTO", description = "pageQueryDTO")
public class PageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 123876123971923123L;

    @Schema(name = "pageNum", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(name = "isAsc", description = "是否顺序排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isAsc;

    @Schema(name = "sortColumn", description = "排序列", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sortColumn;

    public PageQueryDTO() {
    }

    public PageQueryDTO(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.isAsc = true;
    }

    /**
     * 获取排序
     *
     * @return 排序字段
     */
    public String getOrderBy() {
        if (Objects.isNull(sortColumn)) {
            return null;
        }
        String orderBy = sortColumn + " " + (isAsc ? "ASC" : "DESC");
        if (SqlSafeUtil.check(orderBy)) {
            LoggerUtil.error(log, "排序字段不正确:{}", sortColumn);
            throw new IllegalArgumentException("排序字段设置错误！");
        }
        return orderBy;
    }
}
