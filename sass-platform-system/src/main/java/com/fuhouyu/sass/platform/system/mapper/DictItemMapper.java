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
package com.fuhouyu.sass.platform.system.mapper;


import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.DictItem;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典项mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:00
 */
public interface DictItemMapper extends BaseMapper<Long, DictItem> {

    /**
     * 通过字典项编码查询出字典项对象
     *
     * @param dictCode 字典编码
     * @param itemCode 字典项编码
     * @return 字典项dto对象
     */
    @TenantQuery
    DictItem queryByDictCodeAndItemCode(@Param("dictCode") String dictCode,
                                        @Param("itemCode") String itemCode);

    /**
     * 通过字典编码查询出字典项列表
     *
     * @param dictCodeList 字典编码列表
     * @return 字典项列表
     */
    @TenantQuery
    List<DictItem> queryListByDictCodes(@Param("dictCodeList") Collection<String> dictCodeList);

    /**
     * 通过id进行查询
     *
     * @param id 主键id
     * @return 实体对象
     */
    @TenantQuery
    @Override
    DictItem queryById(Long id);

    /**
     * 批量通过id进行查询
     *
     * @param list id集合
     * @return 查询到的实体对象
     */
    @TenantQuery
    @Override
    List<DictItem> queryByIds(@Param("list") Collection<Long> list);


    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @param <P>       范围查询的类型
     * @return 批量查询
     */
    @TenantQuery
    @Override
    <P extends PageQueryDTO> List<DictItem> queryList(@Param("pageQuery") P pageQuery);
}
