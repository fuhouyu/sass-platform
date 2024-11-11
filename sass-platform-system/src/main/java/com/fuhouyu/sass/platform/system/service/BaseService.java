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
package com.fuhouyu.sass.platform.system.service;

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 分页查询接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 19:18
 */
public interface BaseService<P extends PageQueryDTO, T extends BaseDTO, ID> {

    /**
     * 保存dto对象
     *
     * @param dto dto对象
     */
    void save(T dto);

    /**
     * 批量保存dto对象
     *
     * @param dtoList dto集合
     */
    void saveBatch(List<T> dtoList);

    /**
     * 修改dto对象
     *
     * @param dto dto对象
     */
    void edit(T dto);

    /**
     * 通过id进行删除
     *
     * @param id id
     */
    int removeById(ID id);

    /**
     * 批量通过id进行删除
     *
     * @param ids id集合
     */
    int removeByIds(Collection<ID> ids);

    /**
     * 通过id查询
     *
     * @param id id
     * @return dto对象
     */
    T findById(ID id);

    /**
     * 获取分页结果
     *
     * @return 分页结果
     */
    Function<P, List<T>> getPageResult();

    /**
     * 分页查询
     *
     * @param pageable 查询的dto对象
     * @return 分页查询结果
     */
    default PageResultDTO<T> pageList(P pageable) {
        try (Page<Object> page = PageMethod.startPage(pageable.getPageNum(), pageable.getPageSize())) {
            page.setUnsafeOrderBy(pageable.getOrderBy());
            List<T> result = this.getPageResult().apply(pageable);
            return new PageResultDTO<>(page.getPageNum(),
                    page.getPageSize(), page.getTotal(),
                    result);
        }
    }
}
