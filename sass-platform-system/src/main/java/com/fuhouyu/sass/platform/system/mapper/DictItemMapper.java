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


import com.fuhouyu.sass.platform.system.entity.DictItem;
import org.apache.ibatis.annotations.Param;

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
    DictItem queryByDictCodeAndItemCode(@Param("dictCode") String dictCode,
                                        @Param("itemCode") String itemCode);
}
