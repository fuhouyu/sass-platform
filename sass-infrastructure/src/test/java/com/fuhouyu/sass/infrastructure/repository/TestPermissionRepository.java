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
package com.fuhouyu.sass.infrastructure.repository;

import com.fuhouyu.sass.domain.model.permission.PermissionEntity;
import com.fuhouyu.sass.domain.repository.PermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.UUID;

/**
 * <p>
 * 测试权限存储库
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:11
 */
class TestPermissionRepository extends TestBaseRepository {

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    void testPermission() {
        // 保存

        PermissionEntity saveEntity = this.generatePermissionEntity();
        this.permissionRepository.save(saveEntity);
        Long onceId = saveEntity.getId();
        Assertions.assertNotNull(onceId, "返回的id为空");

        Assertions.assertThrowsExactly(DuplicateKeyException.class, () -> this.permissionRepository.save(saveEntity),
                "二次保存，权限编码冲突未抛出异常");

        // 修改
        PermissionEntity updateEntity = this.generatePermissionEntity();
        updateEntity.setId(onceId);
        this.permissionRepository.edit(updateEntity);
        Assertions.assertNotEquals(updateEntity.getPermissionName(), saveEntity.getPermissionName(), "修改未成功");

        // 查询
        PermissionEntity queryById = this.permissionRepository.findById(onceId);
        Assertions.assertNotNull(queryById, "未查询到对应数据");
        PermissionEntity permissionByQuery = this.permissionRepository.findByPermissionCode(saveEntity.getPermissionCode());
        Assertions.assertNotNull(permissionByQuery, "未查询到对应数据");

        // 删除
        int result = this.permissionRepository.removeById(updateEntity.getId());
        Assertions.assertEquals(1, result, "数据删除不正常");

    }


    private PermissionEntity generatePermissionEntity() {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setParentId(-1L);
        permissionEntity.setPermissionName(UUID.randomUUID().toString());
        permissionEntity.setPermissionCode("permissionCode");
        permissionEntity.setPermissionType("M");
        permissionEntity.setDisplayOrder(1);
        permissionEntity.setIcon("");
        permissionEntity.setRoutePath("");
        permissionEntity.setComponentPath("");
        permissionEntity.setUrlParams("");
        permissionEntity.setIsFrame(true);
        permissionEntity.setIsAllowModified(true);
        permissionEntity.setIsSystemd(true);
        permissionEntity.setIsVisible(true);
        return permissionEntity;

    }
}
