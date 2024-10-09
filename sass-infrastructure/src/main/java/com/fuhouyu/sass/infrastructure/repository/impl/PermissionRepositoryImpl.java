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

import com.fuhouyu.sass.common.PageQuery;
import com.fuhouyu.sass.common.PageResult;
import com.fuhouyu.sass.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.domain.model.permission.PermissionEntity;
import com.fuhouyu.sass.domain.repository.PermissionRepository;
import com.fuhouyu.sass.infrastructure.repository.assembler.PermissionAssembler;
import com.fuhouyu.sass.infrastructure.repository.mapper.PermissionMapper;
import com.fuhouyu.sass.infrastructure.repository.orm.PermissionDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 权限存储库实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:13
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class PermissionRepositoryImpl implements PermissionRepository {

    private static final PermissionAssembler PERMISSION_ASSEMBLER = PermissionAssembler.INSTANCE;

    private final PermissionMapper permissionMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PermissionEntity findById(Long id) {
        return PERMISSION_ASSEMBLER.toEntity(this.permissionMapper.queryById(id));
    }

    @Override
    public int removeById(Long id) {
        return this.permissionMapper.deleteById(id);
    }

    @Override
    public void save(PermissionEntity entity) {
        entity.setId(snowflakeIdWorker.nextId());
        PermissionDO permissionDO = PERMISSION_ASSEMBLER.toDO(entity);
        this.permissionMapper.insert(permissionDO);
    }

    @Override
    public void edit(PermissionEntity entity) {
        PermissionDO permissionDO = PERMISSION_ASSEMBLER.toDO(entity);
        this.permissionMapper.update(permissionDO);
    }

    @Override
    public <P extends PageQuery> PageResult<PermissionEntity> pageList(P pageable) {
        try (Page<Object> result = PageMethod.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<PermissionDO> tenantList = this.permissionMapper.queryList(pageable);
            List<PermissionEntity> modelList = PERMISSION_ASSEMBLER.toEntity(tenantList);
            return new PageResult<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), modelList);
        }
    }

    @Override
    public PermissionEntity findByPermissionCode(String permissionCode) {
        return PERMISSION_ASSEMBLER.toEntity(this.permissionMapper.queryByPermissionCode(permissionCode));
    }

    @Override
    public List<PermissionEntity> findPermissionListByRoleIdList(List<Long> roleIdList) {
        List<PermissionDO> list = this.permissionMapper.queryListByRoleIdList(roleIdList);
        return PERMISSION_ASSEMBLER.toEntity(list);
    }
}
