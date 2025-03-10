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

import com.fuhouyu.sass.platform.system.domain.entity.UserHasRole;
import com.fuhouyu.sass.platform.system.mapper.UserHasRoleMapper;
import com.fuhouyu.sass.platform.system.service.UserHasRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户和角色的接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/13 22:04
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserHasRoleServiceImpl implements UserHasRoleService {

    private final UserHasRoleMapper userHasRoleMapper;

    @Override
    public void saveOrUpdateUserRole(Long userId, Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<UserHasRole> userHasRoleList = roleIds.stream().map(roleId -> {
            UserHasRole userHasRole = new UserHasRole();
            userHasRole.setRoleId(roleId);
            userHasRole.setUserId(userId);
            return userHasRole;
        }).toList();
        this.userHasRoleMapper.insertBatch(userHasRoleList);
    }

    @Override
    public void removeByUserIds(Collection<Long> userIds) {
        this.userHasRoleMapper.deleteByUserIds(userIds);
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return this.userHasRoleMapper.queryRoleIdsByUserId(userId);
    }
}
