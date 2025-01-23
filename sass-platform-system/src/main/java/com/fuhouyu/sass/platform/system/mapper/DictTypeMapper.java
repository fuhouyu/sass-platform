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


import com.fuhouyu.sass.platform.system.entity.DictType;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 字典类型mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:00
 */
public interface DictTypeMapper extends BaseMapper<Long, DictType> {

    /**
     * 通过字典类型编码查询出字典类型对象
     *
     * @param dictCode 字典类型编码
     * @return 字典类型do对象
     */
    DictType queryByDictCode(@Param("dictCode") String dictCode);
}
