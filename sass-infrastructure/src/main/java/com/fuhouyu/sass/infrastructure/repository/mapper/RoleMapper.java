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

import com.fuhouyu.sass.infrastructure.repository.orm.RoleDO;

import java.util.List;

/**
 * <p>
 * 角色mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:00
 */
public interface RoleMapper extends BaseMapper<RoleDO, Long> {

    /**
     * 通过角色编码查询出角色对象
     *
     * @param roleCode 角色编码
     * @return 角色do对象
     */
    RoleDO queryByRoleCode(String roleCode);

    /**
     * 通过用户id查询系统级的角色集合
     *
     * @param userId 用户id
     * @return 角色实体对象集合
     */
    List<RoleDO> findSystemRoleListByUserId(Long userId);
}
