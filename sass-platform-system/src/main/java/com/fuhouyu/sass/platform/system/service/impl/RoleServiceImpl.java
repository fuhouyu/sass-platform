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
package com.fuhouyu.sass.platform.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.RolesAssembler;
import com.fuhouyu.sass.platform.system.constants.TenantConstant;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RolePageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Roles;
import com.fuhouyu.sass.platform.system.enums.response.RoleResponseStatusEnum;
import com.fuhouyu.sass.platform.system.mapper.RoleMapper;
import com.fuhouyu.sass.platform.system.service.RoleHasPermissionService;
import com.fuhouyu.sass.platform.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Roles> implements RoleService {

    private static final RolesAssembler ROLES_ASSEMBLER = RolesAssembler.INSTANCE;

    private final RoleMapper roleMapper;

    private final RoleHasPermissionService roleHasPermissionService;

    private final SnowflakeIdWorker snowflakeIdWorker;


    @Override
    public RoleDTO findByRoleCode(String roleCode) {
        return ROLES_ASSEMBLER.toDTO(this.roleMapper.queryByRoleCode(roleCode));
    }

    @Override
    public Long createTenantDefaultRole(Long tenantId, List<Long> permissionIds) {
        long id = this.snowflakeIdWorker.nextId();
        Roles role = new Roles();
        role.setId(id);
        role.setRoleName(TenantConstant.DEFAULT_TENANT_ROLE_NAME);
        role.setRoleCode(TenantConstant.DEFAULT_TENANT_ROLE_CODE);
        role.setDisplayOrder(1);
        role.setIsAllowModified(false);
        role.setDataScope(TenantConstant.DEFAULT_TENANT_ROLE_DATA_SCOPE);
        role.setIsEnabled(true);
        role.setOwnerTenantId(tenantId);
        this.roleMapper.insert(role);
        // 保存权限
        this.roleHasPermissionService.saveRolePermission(id, permissionIds);
        return id;
    }


    @Override
    public List<RoleDTO> getRoleList() {
        return ROLES_ASSEMBLER.toDTO(this.roleMapper.selectList(new QueryWrapper<>()));
    }

    @Override
    public void removeByTenantIds(Collection<Long> tenantIds) {
        this.roleHasPermissionService.removeRolePermissionByTenantIds(tenantIds);
        this.roleMapper.deleteByTenantIds(tenantIds);
    }

    @Override
    public RoleDTO findByRoleCodeAndTenantId(String roleCode, Long tenantId) {
        return ROLES_ASSEMBLER.toDTO(this.roleMapper.queryByRoleCodeByTenantId(roleCode, tenantId));
    }

    @Override
    public void editStatus(RoleDTO roleDTO) {
        this.roleMapper.updateById(ROLES_ASSEMBLER.toEntity(roleDTO));
    }

    @Override
    public long save(RoleDTO dto) {
        String roleCode = dto.getRoleCode();
        Roles roles = this.roleMapper.queryByRoleCode(roleCode);
        if (Objects.nonNull(roles)) {
            throw new ServiceException(RoleResponseStatusEnum.ROLE_CODE_ALREADY_EXISTS,
                    "角色编码: %s 已存在", roleCode);
        }
        long id = snowflakeIdWorker.nextId();
        Roles entity = ROLES_ASSEMBLER.toEntity(dto);
        entity.setIsAllowModified(true);
        entity.setId(id);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        this.roleMapper.insert(entity);
        // 保存角色和权限关系
        this.roleHasPermissionService.saveRolePermission(id, dto.getPermissionIds());
        return id;
    }

    @Override
    public void edit(RoleDTO dto) {
        this.roleMapper.updateById(ROLES_ASSEMBLER.toEntity(dto));
        // 保存角色和权限关系
        this.roleHasPermissionService.removeRolePermissionByRoleId(dto.getId());
        this.roleHasPermissionService.saveRolePermission(dto.getId(), dto.getPermissionIds());
    }


    @Override
    public RoleDTO findById(Long id) {
        Roles roles = this.roleMapper.queryById(id);
        if (Objects.isNull(roles)) {
            throw new ServiceException(RoleResponseStatusEnum.ROLE_NOT_EXISTS);
        }
        RoleDTO roleDTO = ROLES_ASSEMBLER.toDTO(roles);
        roleDTO.setPermissionIds(this.roleHasPermissionService.findPermissionIdsByRoleId(id));
        return roleDTO;
    }

    @Override
    public PageResultDTO<RoleDTO> pageList(RolePageQueryDTO pageQueryDTO) {
        LambdaQueryWrapper<Roles> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(pageQueryDTO.getRoleCode()), Roles::getRoleCode, pageQueryDTO.getRoleCode());
        lambdaQueryWrapper.eq(Objects.nonNull(pageQueryDTO.getIsEnabled()), Roles::getIsEnabled, pageQueryDTO.getIsEnabled());

        return PageResultDTO.buildPageResult(this.roleMapper.selectPage(pageQueryDTO, lambdaQueryWrapper), ROLES_ASSEMBLER::toDTO);
    }
}
