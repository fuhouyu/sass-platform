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
package com.fuhouyu.sass.platform.system.service;

import java.util.Collection;

/**
 * <p>
 * 租户与用户的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/12 17:46
 */
public interface TenantHasUserService {

    /**
     * 保存租户和用户的关系
     *
     * @param tenantId 租户id
     * @param userId   用户id
     */
    void save(Long tenantId, Long userId);

    /**
     * 通过租户id和用户id删除
     *
     * @param tenantId 租户id
     * @param userIds  用户id集合
     */
    void removeByTenantIdAndUserIds(Long tenantId, Collection<Long> userIds);
}
