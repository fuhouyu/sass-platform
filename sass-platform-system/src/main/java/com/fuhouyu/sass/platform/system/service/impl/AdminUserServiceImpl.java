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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.AdminUsersAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.AdminUsers;
import com.fuhouyu.sass.platform.system.enums.UserTypeEnum;
import com.fuhouyu.sass.platform.system.enums.response.UserResponseStatusEnum;
import com.fuhouyu.sass.platform.system.mapper.AdminUserMapper;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.AdminUserService;
import com.fuhouyu.sass.platform.system.service.UserHasRoleService;
import com.fuhouyu.sass.platform.system.service.UserPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUsers> implements AdminUserService {

    private static final AdminUsersAssembler USERS_ASSEMBLER = AdminUsersAssembler.INSTANCE;

    private final AdminUserMapper adminUserMapper;

    private final AccountService accountService;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final UserPositionService userPositionService;

    private final UserHasRoleService userHasRoleService;


    @Override
    public long saveUser(AdminUserDetailDTO userDTO) {
        this.validUsernameExists(userDTO.getUsername());
        long id = snowflakeIdWorker.nextId();
        AdminUsers entity = USERS_ASSEMBLER.toEntity(userDTO);
        entity.setId(id);
        if (Objects.isNull(entity.getOwnerTenantId())) {
            entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        }
        this.adminUserMapper.insert(entity);
        // 保存账号信息
        userDTO.setId(id);
        this.saveAccounts(userDTO);
        // 保存职位信息
        this.userPositionService.saveUserPosition(id, userDTO.getUserPosition());
        // 保存角色信息
        this.userHasRoleService.saveOrUpdateUserRole(id, userDTO.getRoleIds());
        return id;
    }

    @Override
    public AdminUserDTO findByUsername(String username) {
        return USERS_ASSEMBLER.toDTO(this.adminUserMapper.queryByUsername(username));
    }

    @Override
    public void recordLoginSuccess(Long userId) {
        this.adminUserMapper.recordLoginSuccess(userId,
                ContextHolderStrategy.getContext().getRequest().getRequestIp(),
                LocalDateTime.now());
    }

    @Override
    public AdminUserDetailDTO findDetailById(Long id) {
        AdminUserDetailDTO adminUserDetailDTO = this.adminUserMapper.queryDetailById(id);
        if (Objects.isNull(adminUserDetailDTO)) {
            return null;
        }
        List<Long> roleIds = this.userHasRoleService.findRoleIdsByUserId(id);
        adminUserDetailDTO.setRoleIds(roleIds);
        return adminUserDetailDTO;
    }

    @Override
    public void editUser(AdminUserDetailDTO userDTO) {
        this.adminUserMapper.update(USERS_ASSEMBLER.toEntity(userDTO));
        this.userPositionService.saveUserPosition(userDTO.getId(), userDTO.getUserPosition());
        AccountDTO account = userDTO.getAccount();
        this.userHasRoleService.saveOrUpdateUserRole(userDTO.getId(), userDTO.getRoleIds());
        if (Objects.nonNull(account) && Objects.nonNull(account.getCredentials())) {
            this.accountService.editAccounts(account);
        }
    }

    @Override
    public void edit(AdminUserDTO userDTO) {
        this.adminUserMapper.update(USERS_ASSEMBLER.toEntity(userDTO));
    }

    @Override
    public void removeByTenantIds(Collection<Long> tenantIds) {
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        this.adminUserMapper.deleteByTenantIds(tenantIds);
        this.accountService.removeByTenantIds(tenantIds);
    }

    @Override
    public AdminUserDetailDTO findDetailByIdAndOrganizationId(Long id, Long organizationId) {
        return this.adminUserMapper.findDetailByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public void editUserStatus(Long id, Boolean enabled) {
        AdminUsers adminUsers = new AdminUsers();
        adminUsers.setIsEnabled(enabled);
        adminUsers.setId(id);
        this.accountService.editAccountStatusByUserId(id, enabled);
        this.adminUserMapper.update(adminUsers);
    }

    @Override
    public AdminUserDTO findById(Long userId) {
        AdminUsers adminUsers = this.adminUserMapper.selectById(userId);
        return USERS_ASSEMBLER.toDTO(adminUsers);
    }

    @Override
    public PageResultDTO<AdminUserDTO> pageList(AdminUserPageQueryDTO pageQueryDTO) {
        IPage<AdminUserDTO> page = this.adminUserMapper.queryDetailList(pageQueryDTO);
        return PageResultDTO.buildPageResult(page);
    }

    /**
     * 验证用户名是否存在
     * 存在则抛出异常
     *
     * @param username 用户名
     */
    private void validUsernameExists(String username) {
        AdminUsers adminUsers = this.adminUserMapper.queryByUsername(username);
        if (Objects.nonNull(adminUsers)) {
            throw new ServiceException(UserResponseStatusEnum.USER_ALREADY_EXISTS,
                    "%s 用户名已存在", username);
        }
    }

    /**
     * 保存账号列表
     *
     * @param adminUserDetailDTO 用户详情dto
     */
    private void saveAccounts(AdminUserDetailDTO adminUserDetailDTO) {
        AccountDTO accountDTO = adminUserDetailDTO.getAccount();
        accountDTO.setAccount(adminUserDetailDTO.getUsername());
        accountDTO.setUserId(adminUserDetailDTO.getId());
        accountDTO.setIsEnabled(true);
        accountDTO.setUserType(UserTypeEnum.ADMIN);
        if (Objects.isNull(accountDTO.getOwnerTenantId())) {
            accountDTO.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        }
        accountDTO.encodeCredentials();
        try {
            this.accountService.saveAccounts(accountDTO);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户账号注册失败: {}", accountDTO, e);
            throw new ServiceException(UserResponseStatusEnum.USER_REGISTER_ERROR);
        }
    }
}
