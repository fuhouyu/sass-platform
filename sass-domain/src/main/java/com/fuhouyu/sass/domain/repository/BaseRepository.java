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
package com.fuhouyu.sass.domain.repository;

import com.fuhouyu.sass.domain.model.page.PageQueryValue;
import com.fuhouyu.sass.domain.model.page.PageResultEntity;

/**
 * <p>
 * 基础存储库，定义curd
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:42
 */
public interface BaseRepository<T, ID> {

    /**
     * 通过id查询entity
     *
     * @param id 主键id
     * @return 返回entity
     */
    T findById(ID id);

    /**
     * 通过id删除
     *
     * @param id 主键id
     * @return 影响行数
     */
    int removeById(ID id);

    /**
     * 将数据保存到存储库
     *
     * @param entity entity
     */
    void save(T entity);


    /**
     * 修改存储库中的模型
     *
     * @param entity entity
     */
    void edit(T entity);

    /**
     * 分页查询对象
     *
     * @param pageable 分页查询的对象
     * @param <P>      具体的分页查询类型，由子类定义传入
     * @return 分页查询的entity
     */
    <P extends PageQueryValue> PageResultEntity<T> pageList(P pageable);
}
