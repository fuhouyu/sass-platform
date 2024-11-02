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
package com.fuhouyu.sass.platform.system.dto;

import com.github.pagehelper.util.SqlSafeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class PageQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 123876123971923123L;

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    private boolean isAsc;

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
            return orderBy;
        }
        throw new IllegalArgumentException("排序不正确!");
    }
}
