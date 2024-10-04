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

import com.fuhouyu.sass.domain.model.BaseEntity;

import java.util.List;

/**
 * <p>
 * 转换基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/28 16:47
 */
public interface BaseAssembler<DTO, Entity extends BaseEntity<?>> {

    DTO toDTO(Entity source);

    List<DTO> toDTO(List<Entity> sourceList);

    Entity toEntity(DTO source);

    List<Entity> toEntity(List<DTO> sourceList);
}
