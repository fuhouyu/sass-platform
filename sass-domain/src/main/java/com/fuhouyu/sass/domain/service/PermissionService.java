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
package com.fuhouyu.sass.domain.service;

import com.fuhouyu.sass.domain.model.permission.PermissionEntity;

import java.util.List;

/**
 * <p>
 * 权限接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:12
 */
public interface PermissionService {

    /**
     * 获取当前用户的
     *
     * @param isSystemd 查询的是否为系统的角色权限
     * @return 权限实体
     */
    List<PermissionEntity> findPermissionListByMe(Boolean isSystemd);
}
