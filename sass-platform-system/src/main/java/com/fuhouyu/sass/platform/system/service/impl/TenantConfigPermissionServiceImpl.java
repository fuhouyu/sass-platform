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

import com.fuhouyu.sass.platform.system.entity.TenantConfigPermission;
import com.fuhouyu.sass.platform.system.mapper.TenantConfigPermissionMapper;
import com.fuhouyu.sass.platform.system.service.TenantConfigPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户配置权限关系的实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/24 19:48
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TenantConfigPermissionServiceImpl implements TenantConfigPermissionService {

    private final TenantConfigPermissionMapper tenantConfigPermissionMapper;

    @Override
    public void saveConfigPermission(Long configId, Collection<Long> permissionIds) {
        // 先删除所有
        tenantConfigPermissionMapper.delete(configId, null);
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<TenantConfigPermission> list = new ArrayList<>(permissionIds.size());
        for (Long permissionId : permissionIds) {
            TenantConfigPermission tenantConfigPermission = new TenantConfigPermission();
            tenantConfigPermission.setPermissionId(permissionId);
            tenantConfigPermission.setTenantConfigId(configId);
            list.add(tenantConfigPermission);
        }
        this.tenantConfigPermissionMapper.insertBatch(list);

    }

    @Override
    public void deleteConfigPermission(Long configId, Collection<Long> permissionIds) {
        this.tenantConfigPermissionMapper.delete(configId, permissionIds);
    }

    @Override
    public void deleteConfigPermissions(Collection<Long> configIds) {
        this.tenantConfigPermissionMapper.deleteConfigPermissions(configIds);
    }

    @Override
    public List<Long> findPermissionIdByConfigId(Long configId) {
        return this.tenantConfigPermissionMapper.queryPermissionIdByConfigId(configId);
    }
}
