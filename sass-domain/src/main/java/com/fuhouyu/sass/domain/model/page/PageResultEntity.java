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
package com.fuhouyu.sass.domain.model.page;

import com.fuhouyu.sass.common.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分页结果集的model对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 21:27
 */
@Getter
@Setter
@ToString
public class PageResultEntity<T> implements Entity<List<T>>, Serializable {

    @Serial
    private static final long serialVersionUID = 1123871238761238761L;

    private Integer pageNum;

    private Integer pageSize;

    private long total;

    private transient List<T> list;

    public PageResultEntity(Integer pageNum, Integer pageSize, long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageResultEntity(Integer pageNum, Integer pageSize, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    @Override
    public List<T> getIdentifierId() {
        return this.list;
    }
}
