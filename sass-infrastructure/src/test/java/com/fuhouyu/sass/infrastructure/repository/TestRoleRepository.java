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
 * See the License for the specific language governing roles and
 * limitations under the License.
 */
package com.fuhouyu.sass.infrastructure.repository;

import com.fuhouyu.sass.domain.model.role.RoleEntity;
import com.fuhouyu.sass.domain.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.UUID;

/**
 * <p>
 * 测试角色存储库
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:11
 */
class TestRoleRepository extends TestBaseRepository {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testRole() {
        // 保存

        RoleEntity saveEntity = this.generateRoleEntity();
        this.roleRepository.save(saveEntity);
        Long onceId = saveEntity.getId();
        Assertions.assertNotNull(onceId, "返回的id为空");

        Assertions.assertThrowsExactly(DuplicateKeyException.class, () -> this.roleRepository.save(saveEntity),
                "二次保存，角色编码冲突未抛出异常");

        // 修改
        RoleEntity updateEntity = this.generateRoleEntity();
        updateEntity.setId(onceId);
        this.roleRepository.edit(updateEntity);
        Assertions.assertNotEquals(updateEntity.getRoleName(), saveEntity.getRoleName(), "修改未成功");

        // 查询
        RoleEntity queryById = this.roleRepository.findById(onceId);
        Assertions.assertNotNull(queryById, "未查询到对应数据");
        RoleEntity queryByUsername = this.roleRepository.findByRoleCode(saveEntity.getRoleCode());
        Assertions.assertNotNull(queryByUsername, "未查询到对应数据");

        // 删除
        int result = this.roleRepository.removeById(updateEntity.getId());
        Assertions.assertEquals(1, result, "数据删除不正常");

    }


    private RoleEntity generateRoleEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName(UUID.randomUUID().toString());
        roleEntity.setRoleCode(UUID.randomUUID().toString());
        roleEntity.setDisplayOrder(1);
        roleEntity.setDataScope("ALL");
        roleEntity.setIsSystemd(true);
        roleEntity.setIsEnabled(true);
        roleEntity.setIsAllowModified(true);
        return roleEntity;

    }
}
