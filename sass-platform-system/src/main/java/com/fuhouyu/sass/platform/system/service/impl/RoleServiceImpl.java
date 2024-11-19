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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.RolesAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.entity.Roles;
import com.fuhouyu.sass.platform.system.mapper.RoleMapper;
import com.fuhouyu.sass.platform.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 角色接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 20:57
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final RolesAssembler ROLES_ASSEMBLER = RolesAssembler.INSTANCE;

    private final RoleMapper roleMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public RoleDTO findByRoleCode(String roleCode) {
        return ROLES_ASSEMBLER.toDTO(this.roleMapper.queryByRoleCode(roleCode));
    }

    @Override
    public Long save(RoleDTO dto) {
        String roleCode = dto.getRoleCode();
        Roles roles = this.roleMapper.queryByRoleCode(roleCode);
        if (Objects.nonNull(roles)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "角色编码: %s 已存在", roleCode);
        }
        long id = snowflakeIdWorker.nextId();
        Roles entity = ROLES_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        this.roleMapper.insert(entity);
        return id;
    }

    @Override
    public void saveBatch(List<RoleDTO> dtoList) {
        List<Roles> rolesList = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return ROLES_ASSEMBLER.toEntity(dto);
        }).toList();
        this.roleMapper.insertBatch(rolesList);
    }

    @Override
    public void edit(RoleDTO dto) {
        this.roleMapper.update(ROLES_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.roleMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.roleMapper.deleteByIds(ids);
    }

    @Override
    public RoleDTO findById(Long id) {
        return ROLES_ASSEMBLER.toDTO(this.roleMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<RoleDTO>> getPageResult() {
        return (p) -> ROLES_ASSEMBLER.toDTO(this.roleMapper.queryList(p));
    }
}
