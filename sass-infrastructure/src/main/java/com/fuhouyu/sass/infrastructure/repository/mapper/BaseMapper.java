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
package com.fuhouyu.sass.infrastructure.repository.mapper;

import com.fuhouyu.sass.common.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 存储库基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 22:39
 */
public interface BaseMapper<T, ID> {

    /**
     * 添加一个对象，返回影响的行数
     *
     * @param t 实体对象
     */
    void insert(T t);

    /**
     * 批量插入，返回影响的行数
     *
     * @param list 实体对象集合
     * @return 影响的行数
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     * 通过id删除，返回影响的行数
     *
     * @param id 主键id
     * @return 影响行数
     */
    int deleteById(ID id);

    /**
     * 通过id集合进行删除
     *
     * @param list id集合
     * @return 返回影响的行数
     */
    int deleteByIds(@Param("list") List<ID> list);

    /**
     * 更新对象，返回影响的行数
     *
     * @param t 需要更新的实体对象
     * @return 影响的行数
     */
    int update(T t);

    /**
     * 通过id进行查询
     *
     * @param id 主键id
     * @return 实体对象
     */
    T queryById(ID id);

    /**
     * 批量通过id进行查询
     *
     * @param list id集合
     * @return 查询到的实体对象
     */
    List<T> queryByIds(@Param("list") List<ID> list);


    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @param <P> 范围查询的类型
     * @return 批量查询
     */
    <P extends PageQuery> List<T> queryList(PageQuery pageQuery);
}
