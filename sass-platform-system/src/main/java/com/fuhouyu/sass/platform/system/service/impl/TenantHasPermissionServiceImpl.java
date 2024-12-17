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

import com.fuhouyu.sass.platform.system.entity.TenantHasPermission;
import com.fuhouyu.sass.platform.system.mapper.TenantHasPermissionMapper;
import com.fuhouyu.sass.platform.system.service.TenantHasPermissionService;
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
public class TenantHasPermissionServiceImpl implements TenantHasPermissionService {

    private final TenantHasPermissionMapper tenantHasPermissionMapper;

    @Override
    public void saveOrUpdateTenantPermission(Long tenantId, Collection<Long> permissionIds) {
        // 先删除所有
        tenantHasPermissionMapper.delete(tenantId, null);
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<TenantHasPermission> list = new ArrayList<>(permissionIds.size());
        for (Long permissionId : permissionIds) {
            TenantHasPermission tenantHasPermission = new TenantHasPermission();
            tenantHasPermission.setPermissionId(permissionId);
            tenantHasPermission.setTenantId(tenantId);
            list.add(tenantHasPermission);
        }
        this.tenantHasPermissionMapper.insertBatch(list);

    }

    @Override
    public void removeTenantPermission(Long tenantId, Collection<Long> permissionIds) {
        this.tenantHasPermissionMapper.delete(tenantId, permissionIds);
    }

    @Override
    public void removeTenantPermissions(Collection<Long> tenantIds) {
        this.tenantHasPermissionMapper.deleteByTenantIds(tenantIds);
    }

    @Override
    public List<Long> findPermissionIdByTenantId(Long tenantId) {
        return this.tenantHasPermissionMapper.queryPermissionIdByTenantId(tenantId);
    }
}
