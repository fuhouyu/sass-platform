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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.sass.platform.common.enums.ResponseCodeEnum;
import com.fuhouyu.sass.platform.common.exception.ServiceException;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.PermissionAssembler;
import com.fuhouyu.sass.platform.system.dto.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.PermissionDTO;
import com.fuhouyu.sass.platform.system.dto.RoleDTO;
import com.fuhouyu.sass.platform.system.entity.Permissions;
import com.fuhouyu.sass.platform.system.mapper.PermissionMapper;
import com.fuhouyu.sass.platform.system.service.PermissionService;
import com.fuhouyu.sass.platform.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * permission实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:56
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private static final PermissionAssembler PERMISSION_ASSEMBLER = PermissionAssembler.INSTANCE;

    private final PermissionMapper permissionMapper;

    private final RoleService roleService;

    private final SnowflakeIdWorker snowflakeIdWorker;


    @Override
    public PermissionDTO findByPermissionCode(String permissionCode) {
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryByPermissionCode(permissionCode));
    }

    @Override
    public List<PermissionDTO> findPermissionListByMe() {
        User user = ContextHolderStrategy.getContext().getUser();

        Long userId = user.getId();
        List<RoleDTO> roleList = this.roleService.findRoleListByUserId(userId);
        if (CollectionUtils.isEmpty(roleList)) {
            return Collections.emptyList();
        }
        List<Long> roleIdList = roleList.stream().map(RoleDTO::getId).toList();
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryListByRoleIdList(roleIdList));
    }

    @Override
    public void save(PermissionDTO dto) {
        dto.setId(snowflakeIdWorker.nextId());
        String permissionCode = dto.getPermissionCode();
        Permissions permissions = this.permissionMapper.queryByPermissionCode(permissionCode);
        if (Objects.nonNull(permissions)) {
            throw new ServiceException(ResponseCodeEnum.INVALID_PARAM, String.format("权限编码:%s 已存在", permissionCode));
        }
        this.permissionMapper.insert(PERMISSION_ASSEMBLER.toEntity(dto));
    }

    @Override
    public void saveBatch(List<PermissionDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<Permissions> list = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return PERMISSION_ASSEMBLER.toEntity(dto);
        }).toList();
        this.permissionMapper.insertBatch(list);
    }

    @Override
    public void edit(PermissionDTO dto) {
        this.permissionMapper.update(PERMISSION_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.permissionMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.permissionMapper.deleteByIds(ids);
    }

    @Override
    public PermissionDTO findById(Long id) {
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<PermissionDTO>> getPageResult() {
        return (p) -> PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryList(p));
    }
}
