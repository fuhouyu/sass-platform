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
package com.fuhouyu.sass.platform.system;

import com.fuhouyu.sass.platform.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.dto.RoleDTO;
import com.fuhouyu.sass.platform.system.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * <p>
 * 测试角色存储库
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:11
 */
class TestRoleService extends TestBaseService {

    @Autowired
    private RoleService roleService;

    @Test
    void testRole() {
        // 保存

        RoleDTO roleDTO = this.generateRoleEntity();
        this.roleService.save(roleDTO);
        Long onceId = roleDTO.getId();
        Assertions.assertNotNull(onceId, "返回的id为空");

        Assertions.assertThrowsExactly(ServiceException.class, () -> this.roleService.save(roleDTO),
                "二次保存，角色编码冲突未抛出异常");

        // 修改
        String roleName = roleDTO.getRoleName();
        roleDTO.setRoleName(super.getUUIDStr(10));
        this.roleService.edit(roleDTO);
        Assertions.assertNotEquals(roleName, roleDTO.getRoleName(), "修改未成功");

        // 查询
        RoleDTO queryById = this.roleService.findById(onceId);
        Assertions.assertNotNull(queryById, "未查询到对应数据");
        RoleDTO queryByRoleDTO = this.roleService.findByRoleCode(roleDTO.getRoleCode());
        Assertions.assertNotNull(queryByRoleDTO, "未查询到对应数据");

        // 删除
        int result = this.roleService.removeById(roleDTO.getId());
        Assertions.assertEquals(1, result, "数据删除不正常");

    }


    private RoleDTO generateRoleEntity() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(UUID.randomUUID().toString());
        roleDTO.setRoleCode(UUID.randomUUID().toString());
        roleDTO.setDisplayOrder(1);
        roleDTO.setDataScope("ALL");
        roleDTO.setIsSystemd(true);
        roleDTO.setIsEnabled(true);
        roleDTO.setIsAllowModified(true);
        return roleDTO;

    }
}
