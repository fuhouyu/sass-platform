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
package com.fuhouyu.sass.domain.repository;

import com.fuhouyu.sass.domain.model.permission.PermissionEntity;

/**
 * <p>
 * 权限存储接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:57
 */
public interface PermissionRepository extends BaseRepository<PermissionEntity, Long> {


    /**
     * 通过权限编码查询权限实体
     *
     * @param permissionCode 权限编码
     * @return 权限实体
     */
    PermissionEntity findByPermissionCode(String permissionCode);

}
