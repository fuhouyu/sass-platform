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
 * 用户和角色的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/13 22:03
 */
public interface UserHasRoleService {


    /**
     * 保存用户和角色的关系
     *
     * @param userId  用户id
     * @param roleIds 角色id集合
     */
    void saveOrUpdateUserRole(Long userId, Collection<Long> roleIds);

    /**
     * 通过用户id删除用户角色关系
     *
     * @param userIds 用户id集合
     */
    void removeByUserIds(Collection<Long> userIds);
}
