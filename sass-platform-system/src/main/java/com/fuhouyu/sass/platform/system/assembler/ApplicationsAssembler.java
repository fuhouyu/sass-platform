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
package com.fuhouyu.sass.platform.system.assembler;

import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Applications;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Set;

/**
 * <p>
 * 账号转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 20:58
 */
@Mapper
public interface ApplicationsAssembler extends BaseAssembler<Applications, ApplicationDTO> {

    ApplicationsAssembler INSTANCE = Mappers.getMapper(ApplicationsAssembler.class);


    /**
     * set 转成 string
     *
     * @param set set
     * @return string
     */
    default String set2Str(Set<String> set) {
        return set == null ? null : StringUtils.collectionToCommaDelimitedString(set);
    }


    /**
     * str转换成set
     *
     * @param value value
     * @return set
     */
    default Set<String> str2Set(String value) {
        return value == null || value.isBlank()
                ? Collections.emptySet()
                : StringUtils.commaDelimitedListToSet(value);
    }
}
