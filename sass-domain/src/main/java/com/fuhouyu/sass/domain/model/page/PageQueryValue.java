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

import com.fuhouyu.sass.common.ValueObject;
import com.fuhouyu.sass.domain.enums.PageQueryDirection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serial;
import java.util.Objects;

/**
 * <p>
 * 基础的查询model对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:49
 */
@ToString
@Getter
@Setter
public class PageQueryValue implements ValueObject<PageQueryValue> {

    @Serial
    private static final long serialVersionUID = 123876123971923123L;

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    private PageQueryDirection direction;

    private String sortColumn;

    @Override
    public boolean sameValueAs(PageQueryValue other) {
        return EqualsBuilder.reflectionEquals(this, other);
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
        if (Objects.isNull(this.direction)) {
            return this.sortColumn;
        }
        return sortColumn + " " + this.direction.name();
    }
}
