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

import com.fuhouyu.sass.platform.system.entity.TenantPermission;
import com.fuhouyu.sass.platform.system.mapper.TenantPermissionMapper;
import com.fuhouyu.sass.platform.system.service.TenantPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户权限关系的实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:48
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TenantPermissionServiceImpl implements TenantPermissionService {

    private final TenantPermissionMapper tenantPermissionMapper;

    @Override
    public void saveTenantPermission(Long tenantId, Collection<Long> permissionIds) {
        // 先删除所有
        tenantPermissionMapper.delete(tenantId, null);
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<TenantPermission> list = new ArrayList<>(permissionIds.size());
        for (Long permissionId : permissionIds) {
            TenantPermission tenantPermission = new TenantPermission();
            tenantPermission.setPermissionId(permissionId);
            tenantPermission.setTenantId(tenantId);
            list.add(tenantPermission);
        }
        this.tenantPermissionMapper.insertBatch(list);

    }

    @Override
    public void deleteTenantPermission(Long tenantId, Collection<Long> permissionIds) {
        this.tenantPermissionMapper.delete(tenantId, permissionIds);
    }

    @Override
    public void deleteTenantPermissions(Collection<Long> tenantIds) {
        this.tenantPermissionMapper.deleteTenantPermissions(tenantIds);
    }

    @Override
    public List<Long> findPermissionIdByTenantId(Long tenantId) {
        return this.tenantPermissionMapper.queryPermissionIdByTenantId(tenantId);
    }
}
