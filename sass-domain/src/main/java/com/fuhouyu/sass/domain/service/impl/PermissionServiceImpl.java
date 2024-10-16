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
package com.fuhouyu.sass.domain.service.impl;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.User;
import com.fuhouyu.sass.domain.model.permission.PermissionEntity;
import com.fuhouyu.sass.domain.model.role.RoleEntity;
import com.fuhouyu.sass.domain.repository.PermissionRepository;
import com.fuhouyu.sass.domain.repository.RoleRepository;
import com.fuhouyu.sass.domain.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

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

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;


    @Override
    public List<PermissionEntity> findPermissionListByMe(Boolean isSystemd) {
        User user = ContextHolderStrategy.getContext().getUser();

        Long userId = user.getId();
        // 这里先直接返回
        if (Boolean.FALSE.equals(isSystemd)) {
            return Collections.emptyList();
        }
        List<RoleEntity> roleEntityList = this.roleRepository.findSystemRoleListByUserId(userId);
        if (CollectionUtils.isEmpty(roleEntityList)) {
            return Collections.emptyList();
        }
        List<Long> roleIdList = roleEntityList.stream().map(RoleEntity::getId).toList();
        return this.permissionRepository.findPermissionListByRoleIdList(roleIdList);
    }
}
