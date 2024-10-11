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
package com.fuhouyu.sass.infrastructure.repository.impl;

import com.fuhouyu.sass.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.domain.model.page.PageQueryValue;
import com.fuhouyu.sass.domain.model.page.PageResultEntity;
import com.fuhouyu.sass.domain.model.role.RoleEntity;
import com.fuhouyu.sass.domain.repository.RoleRepository;
import com.fuhouyu.sass.infrastructure.repository.assembler.RoleAssembler;
import com.fuhouyu.sass.infrastructure.repository.mapper.RoleMapper;
import com.fuhouyu.sass.infrastructure.repository.orm.RoleDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色存储层实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 21:08
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private static final RoleAssembler ROLE_ASSEMBLER = RoleAssembler.INSTANCE;
    private final RoleMapper roleMapper;
    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public List<RoleEntity> findSystemRoleListByUserId(Long userId) {
        List<RoleDO> roleList = roleMapper.findSystemRoleListByUserId(userId);
        return ROLE_ASSEMBLER.toEntity(roleList);
    }

    @Override
    public RoleEntity findByRoleCode(String roleCode) {
        return ROLE_ASSEMBLER.toEntity(this.roleMapper.queryByRoleCode(roleCode));
    }

    @Override
    public RoleEntity findById(Long id) {
        return ROLE_ASSEMBLER.toEntity(this.roleMapper.queryById(id));
    }

    @Override
    public int removeById(Long id) {
        return this.roleMapper.deleteById(id);
    }

    @Override
    public void save(RoleEntity entity) {
        long id = snowflakeIdWorker.nextId();
        entity.setId(id);
        RoleDO roleDO = ROLE_ASSEMBLER.toDO(entity);
        this.roleMapper.insert(roleDO);
    }

    @Override
    public void edit(RoleEntity entity) {
        this.roleMapper.update(ROLE_ASSEMBLER.toDO(entity));
    }

    @Override
    public <P extends PageQueryValue> PageResultEntity<RoleEntity> pageList(P pageable) {
        try (Page<Object> page = PageMethod.startPage(pageable.getPageNum(), pageable.getPageSize())) {
            page.setOrderBy(pageable.getOrderBy());
            List<RoleDO> roleList = this.roleMapper.queryList(pageable);
            List<RoleEntity> modelList = ROLE_ASSEMBLER.toEntity(roleList);
            return new PageResultEntity<>(pageable.getPageNum(), pageable.getPageSize(), (int) page.getTotal(), modelList);
        }
    }
}
