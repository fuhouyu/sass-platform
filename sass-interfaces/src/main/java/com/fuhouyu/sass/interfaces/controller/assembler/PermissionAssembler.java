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

import com.fuhouyu.sass.domain.model.permission.PermissionEntity;
import com.fuhouyu.sass.interfaces.controller.dto.permission.PermissionInfoTreeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * <p>
 * 权限转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:26
 */
@Mapper
public interface PermissionAssembler {

    PermissionAssembler INSTANCE = Mappers.getMapper(PermissionAssembler.class);

    /**
     * 实体转换为权限树dto对象
     *
     * @param permissionEntity 权限实体
     * @return 权限详情dto对象
     */
    List<PermissionInfoTreeDTO> toPermissionInfoTreeDTOList(List<PermissionEntity> permissionEntity);

}
