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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.Positions;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 岗位mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:15
 */
public interface PositionMapper extends BaseMapper<Long, Positions> {

    /**
     * 通过id进行查询
     *
     * @param id 主键id
     * @return 实体对象
     */
    @TenantQuery
    @Override
    Positions queryById(Long id);

    /**
     * 批量通过id进行查询
     *
     * @param list id集合
     * @return 查询到的实体对象
     */
    @TenantQuery
    @Override
    List<Positions> queryByIds(@Param("list") Collection<Long> list);


    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @param <P>       范围查询的类型
     * @return 批量查询
     */
    @TenantQuery
    @Override
    <P extends PageQueryDTO> List<Positions> queryList(@Param("pageQuery") P pageQuery);


    /**
     * 通过职位编码查询出职位信息
     *
     * @param positionCode 职位编码
     * @return 职位对象
     */
    @TenantQuery
    Positions queryPositionByPositionCode(@Param("positionCode") String positionCode);
}
