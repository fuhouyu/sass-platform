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
package com.fuhouyu.sass.platform.system.listener;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.constants.TenantConstant;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户事件监听
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/17 21:16
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TenantEventListener implements ApplicationListener<TenantEvent> {

    private final PermissionService permissionService;

    private final TenantHasPermissionService tenantHasPermissionService;

    private final RoleService roleService;

    private final RoleHasPermissionService roleHasPermissionService;

    private final UserHasRoleService userHasRoleService;

    private final OrganizationService organizationService;

    @Override
    public void onApplicationEvent(TenantEvent event) {
        switch (event.getTenantEventEnum()) {
            case CREATE -> this.onCreate(event.getSource());
            case UPDATE -> this.onUpdate(event.getSource());
        }
    }

    /**
     * 租户创建事件
     *
     * @param tenantInfoDTO 租户dto对象
     */
    private void onCreate(TenantInfoDTO tenantInfoDTO) {
        List<Long> permissionIds = tenantInfoDTO.getPermissionIds();
        this.checkPermissionIdsExists(permissionIds);
        // 关联租户和权限的关系
        this.tenantHasPermissionService.saveOrUpdateTenantPermission(tenantInfoDTO.getId(), permissionIds);
        // 新增角色
        Long roleId = roleService.createTenantDefaultRole(tenantInfoDTO.getId(), permissionIds);
        // 组织配置
        this.organizationService.createTenantDefaultOrganization(tenantInfoDTO);
        // 管理员配置
        this.userHasRoleService.saveOrUpdateUserRole(tenantInfoDTO.getAdminUserId(), List.of(roleId));
    }


    /**
     * 更新租户
     *
     * @param tenantInfoDTO 租户dto对象
     */
    private void onUpdate(TenantInfoDTO tenantInfoDTO) {
        List<Long> permissionIds = tenantInfoDTO.getPermissionIds();
        this.checkPermissionIdsExists(permissionIds);
        // 关联租户和权限的关系
        this.tenantHasPermissionService.saveOrUpdateTenantPermission(tenantInfoDTO.getId(), permissionIds);
        // 更新角色和权限
        RoleDTO roleDTO = this.roleService.findByRoleCodeAndTenantId(TenantConstant.DEFAULT_TENANT_ROLE_CODE,
                tenantInfoDTO.getId());
        // 这里的角色不会为空
        this.roleHasPermissionService.removeRolePermissionByRoleId(roleDTO.getId());
        this.roleHasPermissionService.saveRolePermission(roleDTO.getId(), permissionIds);
    }


    /**
     * 检查权限id是否存在
     *
     * @param permissionIds 权限id集合
     */
    private void checkPermissionIdsExists(Collection<Long> permissionIds) {
        List<PermissionDTO> list = this.permissionService.findByIds(permissionIds);
        if (!Objects.equals(list.size(), permissionIds.size())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "选择权限有误，请刷新页面后重新选择");
        }
    }
}
