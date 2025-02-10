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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.UsersAssembler;
import com.fuhouyu.sass.platform.system.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.dto.user.UserDetailDTO;
import com.fuhouyu.sass.platform.system.entity.Users;
import com.fuhouyu.sass.platform.system.mapper.UserMapper;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
public class UserServiceImpl implements UserService {

    private static final UsersAssembler USERS_ASSEMBLER = UsersAssembler.INSTANCE;

    private final UserMapper userMapper;

    private final AccountService accountService;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final UserPositionService userPositionService;

    private final TenantHasUserService tenantHasUserService;

    private final UserHasRoleService userHasRoleService;

    @Override
    public Long save(UserDTO userinfoDTO) {
        this.validUsernameExists(userinfoDTO.getUsername());
        long id = snowflakeIdWorker.nextId();
        Users entity = USERS_ASSEMBLER.toEntity(userinfoDTO);
        entity.setId(id);
        this.userMapper.insert(entity);
        this.tenantHasUserService.save(
                ContextHolderStrategy.getContext().getUser().getTenantId(),
                id);
        return id;
    }

    @Override
    public Long saveUser(UserDetailDTO userDTO) {
        Long id = this.save(userDTO);
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
    public UserDTO findByUsername(String username) {
        return USERS_ASSEMBLER.toDTO(this.userMapper.queryByUsername(username));
    }

    @Override
    public void recordLoginSuccess(Long userId) {
        this.userMapper.recordLoginSuccess(userId,
                ContextHolderStrategy.getContext().getRequest().getRequestIp(),
                LocalDateTime.now());
    }

    @Override
    public UserDetailDTO findDetailById(Long id) {
        UserDetailDTO userDetailDTO = this.userMapper.queryDetailById(id);
        if (Objects.isNull(userDetailDTO)) {
            return null;
        }
        List<Long> roleIds = this.userHasRoleService.findRoleIdsByUserId(id);
        userDetailDTO.setRoleIds(roleIds);
        return userDetailDTO;
    }

    @Override
    public void editUser(UserDetailDTO userDTO) {
        this.userMapper.update(USERS_ASSEMBLER.toEntity(userDTO));
        this.userPositionService.saveUserPosition(userDTO.getId(), userDTO.getUserPosition());
        AccountDTO account = userDTO.getAccount();
        this.userHasRoleService.saveOrUpdateUserRole(userDTO.getId(), userDTO.getRoleIds());
        if (Objects.nonNull(account) && Objects.nonNull(account.getCredentials())) {
            this.accountService.edit(account);
        }
    }

    @Override
    public UserDTO findById(Long userId) {
        Users users = this.userMapper.queryById(userId);
        UserDTO userDTO = USERS_ASSEMBLER.toDTO(users);

        if (Objects.nonNull(ContextHolderStrategy.getContext().getUser())) {
            userDTO.setTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        }

        return userDTO;
    }

    @Override
    public void edit(UserDTO userinfoDTO) {
        this.userMapper.update(USERS_ASSEMBLER.toEntity(userinfoDTO));
    }

    @Override
    public void saveBatch(List<UserDTO> dtoList) {
        List<Users> list = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return USERS_ASSEMBLER.toEntity(dto);
        }).toList();
        this.userMapper.insertBatch(list);
    }

    @Override
    public int removeById(Long id) {
        User user = ContextHolderStrategy.getContext().getUser();
        if (id.equals(user.getId())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "不允许操作当前登录账号: %s", user.getUsername());
        }
        return this.userMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        User user = ContextHolderStrategy.getContext().getUser();
        if (ids.contains(user.getId())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "不允许操作当前登录账号: %s", user.getUsername());
        }
        int deleteUserCount = this.userMapper.deleteByIds(ids);
        this.accountService.removeByUserIds(ids);
        this.tenantHasUserService.removeByTenantIdAndUserIds(ContextHolderStrategy.getContext().getUser().getTenantId(), ids);
        this.userPositionService.removeByUserIds(ids);
        this.userHasRoleService.removeByUserIds(ids);
        return deleteUserCount;
    }

    @Override
    public Function<PageQueryDTO, List<UserDTO>> getPageResult() {
        return this.userMapper::queryDetailList;
    }

    /**
     * 验证用户名是否存在
     * 存在则抛出异常
     *
     * @param username 用户名
     */
    private void validUsernameExists(String username) {
        Users users = this.userMapper.queryByUsername(username);
        if (Objects.nonNull(users)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "%s 用户名已存在", username);
        }
    }

    /**
     * 保存账号列表
     *
     * @param userDetailDTO 用户详情dto
     */
    private void saveAccounts(UserDetailDTO userDetailDTO) {
        AccountDTO accountDTO = userDetailDTO.getAccount();
        accountDTO.setAccount(userDetailDTO.getUsername());
        accountDTO.setUserId(userDetailDTO.getId());
        accountDTO.setIsEnabled(true);
        try {
            this.accountService.save(accountDTO);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户账号注册失败: {}", accountDTO, e);
            throw new IllegalArgumentException("用户注册失败");
        }
    }
}
