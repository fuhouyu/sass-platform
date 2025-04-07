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
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.domain.dto.role.RoleDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.SaveOrEditTenantInfoDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserPositionDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    private final AdminUserService adminUserService;

    private final ParamConfigService paramConfigService;

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
    private void onCreate(SaveOrEditTenantInfoDTO tenantInfoDTO) {
        Collection<Long> addPermissionIds = tenantInfoDTO.getAddPermissionIds();
        Long tenantId = tenantInfoDTO.getId();
        List<PermissionDTO> sourcePermissionList = this.checkPermissionIdsExists(addPermissionIds);
        // 关联租户和权限的关系
        this.tenantHasPermissionService.saveOrUpdateTenantPermission(tenantId, addPermissionIds, null);
        // 复制权限到新的租户权限
        List<Long> targetPermissionIds = this.permissionService.copyPermissionToTenant(sourcePermissionList, tenantId);
        // 新增角色
        Long roleId = roleService.createTenantDefaultRole(tenantId, targetPermissionIds);
        // 组织配置
        Long organizationId = this.organizationService.createTenantDefaultOrganization(tenantInfoDTO);
        // 创建用户
        Long userId = this.createAdminUser(tenantInfoDTO.getContactPerson(),
                tenantId, roleId, organizationId);
        // 管理员配置
        this.userHasRoleService.saveOrUpdateUserRole(userId, List.of(roleId));
    }


    /**
     * 更新租户
     *
     * @param tenantInfoDTO 租户dto对象
     */
    private void onUpdate(SaveOrEditTenantInfoDTO tenantInfoDTO) {
        Set<Long> addPermissionIds = tenantInfoDTO.getAddPermissionIds();
        Set<Long> deletePermissionIds = tenantInfoDTO.getDeletePermissionIds();
        Long tenantId = tenantInfoDTO.getId();
        List<PermissionDTO> sourcePermissionList = this.checkPermissionIdsExists(addPermissionIds);
        // 关联租户和权限的关系
        this.tenantHasPermissionService.saveOrUpdateTenantPermission(tenantId, addPermissionIds, deletePermissionIds);
        // 复制权限
        List<Long> targetPermissionIds = this.permissionService.copyPermissionToTenant(sourcePermissionList, tenantId);
        // 通过原始的权限id进行删除
        this.permissionService.removePermissionForTenant(deletePermissionIds, tenantId);
        // 更新角色和权限
        RoleDTO roleDTO = this.roleService.findByRoleCodeAndTenantId(TenantConstant.DEFAULT_TENANT_ROLE_CODE,
                tenantId);
        // 这里的角色不会为空
        this.roleHasPermissionService.removeRolePermissionByRoleId(roleDTO.getId());
        this.roleHasPermissionService.saveRolePermission(roleDTO.getId(), targetPermissionIds);
    }


    /**
     * 检查权限id是否存在
     *
     * @param permissionIds 权限id集合
     * @return 权限集合
     */
    private List<PermissionDTO> checkPermissionIdsExists(Collection<Long> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return Collections.emptyList();
        }
        List<PermissionDTO> list = this.permissionService.findByIds(permissionIds);
        if (!Objects.equals(list.size(), permissionIds.size())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "选择权限有误，请刷新页面后重新选择");
        }
        return list;
    }

    /**
     * 创建管理员用户
     *
     * @param username       用户名
     * @param tenantId       租户id
     * @param roleId         角色id
     * @param organizationId 组织id
     * @return 用户id
     */
    private long createAdminUser(String username,
                                 long tenantId,
                                 long roleId,
                                 long organizationId) {
        List<ParamConfigDTO> paramConfigList = this.paramConfigService.findListByGroupKey("TENANT");
        Optional<ParamConfigDTO> optional = paramConfigList.stream()
                .filter(config -> Objects.equals(config.getConfigKey(), "DEFAULT_PASSWORD"))
                .findAny();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setOwnerTenantId(tenantId);
        accountDTO.setAccount(username);
        accountDTO.setAccountType(AccountTypeEnum.PASSWORD.name());
        if (optional.isEmpty()) {
            accountDTO.setCredentials("Aa123123..");
        } else {
            accountDTO.setCredentials(optional.get().getConfigValue());
        }

        AdminUserDetailDTO adminUserDTO = new AdminUserDetailDTO();
        adminUserDTO.setAccount(accountDTO);
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        userPositionDTO.setOrganizationId(organizationId);
        userPositionDTO.setPositionName("系统所有者");
        userPositionDTO.setIsMain(true);
        userPositionDTO.setOrderInOrganization(1L);

        adminUserDTO.setUserPosition(userPositionDTO);
        adminUserDTO.setRoleIds(List.of(roleId));

        adminUserDTO.setUsername(username);
        adminUserDTO.setRealName(username);
        adminUserDTO.setNickname(username);
        adminUserDTO.setGender("UNKNOWN");
        adminUserDTO.setOwnerTenantId(tenantId);

        return this.adminUserService.saveUser(adminUserDTO);
    }
}
