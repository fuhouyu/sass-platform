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

import com.fuhouyu.sass.platform.system.domain.entity.RoleHasPermission;
import com.fuhouyu.sass.platform.system.mapper.RoleHasPermissionMapper;
import com.fuhouyu.sass.platform.system.service.RoleHasPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色和权限关系实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/14 16:41
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleHasPermissionServiceImpl implements RoleHasPermissionService {

    private final RoleHasPermissionMapper roleHasPermissionMapper;

    @Override
    public void saveRolePermission(Long roleId, Collection<Long> permissionIds) {
        this.roleHasPermissionMapper.deleteByRoleId(roleId, permissionIds);
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<RoleHasPermission> list = permissionIds.stream().map(permissionId -> {
            RoleHasPermission roleHasPermission = new RoleHasPermission();
            roleHasPermission.setPermissionId(permissionId);
            roleHasPermission.setRoleId(roleId);
            return roleHasPermission;
        }).toList();
        this.roleHasPermissionMapper.insertBatch(list);
    }

    @Override
    public void removeRolePermissionByRoleId(Long roleId) {
        this.roleHasPermissionMapper.deleteByRoleId(roleId, null);
    }


    @Override
    public List<Long> findPermissionIdsByRoleId(Long roleId) {
        return this.roleHasPermissionMapper.queryPermissionIdByRoleId(roleId);
    }


    @Override
    public void removeRolePermissionByTenantIds(Collection<Long> tenantIds) {
        this.roleHasPermissionMapper.deleteRolePermissionByTenantIds(tenantIds);
    }

    @Override
    public void removeByPermissionIds(Collection<Long> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        this.roleHasPermissionMapper.deleteByPermissionIds(permissionIds);
    }
}
