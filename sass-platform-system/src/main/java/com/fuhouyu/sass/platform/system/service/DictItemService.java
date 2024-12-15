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

import com.fuhouyu.sass.platform.system.dto.dict.DictItemDTO;

/**
 * <p>
 * 字典项接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 18:27
 */
public interface DictItemService extends BaseService<Long, DictItemDTO> {


    /**
     * 检查字典项编码是否存在
     *
     * @param dictCode 字典编码
     * @param itemCode 字典项编码
     * @return true 已存在 false 不存在
     */
    Boolean checkItemCodeExists(String dictCode,
                                String itemCode);
}
