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

import com.fuhouyu.sass.platform.system.entity.TenantHasUser;
import com.fuhouyu.sass.platform.system.mapper.TenantHasUserMapper;
import com.fuhouyu.sass.platform.system.service.TenantHasUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 * 租户与用户的接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/12 17:46
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class TenantHasUserServiceImpl implements TenantHasUserService {

    private final TenantHasUserMapper tenantHasUserMapper;

    @Override
    public void save(Long tenantId, Long userId) {
        TenantHasUser tenantHasUser = new TenantHasUser();
        tenantHasUser.setTenantId(tenantId);
        tenantHasUser.setUserId(userId);
        tenantHasUserMapper.insert(tenantHasUser);
    }

    @Override
    public void removeByTenantIdAndUserIds(Long tenantId, Collection<Long> userIds) {
        this.tenantHasUserMapper.deleteByTenantIdAndUserIds(tenantId, userIds);
    }

    @Override
    public void removeByTenantIds(Collection<Long> tenantIds) {
        this.tenantHasUserMapper.deleteByTenantIds(tenantIds);
    }
}
