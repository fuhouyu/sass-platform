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
package com.fuhouyu.sass.interfaces.controller.assembler;

import com.fuhouyu.sass.domain.model.page.PageQueryValue;
import com.fuhouyu.sass.interfaces.controller.dto.BasePageQueryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 分页查询转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/11 12:22
 */
@Mapper
public interface PageQueryAssembler {

    PageQueryAssembler INSTANCE = Mappers.getMapper(PageQueryAssembler.class);

    /**
     * 将controller层的查询对象，转换为domain层对象
     *
     * @param basePageQueryDTO 分页查询dto对象
     * @return 分页查询对象
     */
    PageQueryValue toPageQuery(BasePageQueryDTO basePageQueryDTO);

}
