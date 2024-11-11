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
package com.fuhouyu.sass.platform.system;

import com.fuhouyu.sass.platform.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.service.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * <p>
 * 测试权限存储库
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 18:11
 */
class TestPermissionService extends TestBaseService {

    @Autowired
    private PermissionService permissionService;

    @Test
    void testPermission() {
        // 保存
        PermissionDTO permissionDTO = this.generatePermissionDTO();
        this.permissionService.save(permissionDTO);
        Long onceId = permissionDTO.getId();
        Assertions.assertNotNull(onceId, "返回的id为空");

        Assertions.assertThrowsExactly(ServiceException.class, () -> this.permissionService.save(permissionDTO),
                "二次保存，权限编码冲突未抛出异常");

        // 修改
        String permissionName = permissionDTO.getPermissionName();
        permissionDTO.setId(onceId);
        permissionDTO.setPermissionName(super.getUUIDStr(8));
        this.permissionService.edit(permissionDTO);
        Assertions.assertNotEquals(permissionName, permissionDTO.getPermissionName(), "修改未成功");

        // 查询
        PermissionDTO queryById = this.permissionService.findById(onceId);
        Assertions.assertNotNull(queryById, "未查询到对应数据");
        PermissionDTO permissionByQuery = this.permissionService.findByPermissionCode(permissionDTO.getPermissionCode());
        Assertions.assertNotNull(permissionByQuery, "未查询到对应数据");

        // 删除
        int result = this.permissionService.removeById(queryById.getId());
        Assertions.assertEquals(1, result, "数据删除不正常");

    }


    private PermissionDTO generatePermissionDTO() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setParentId(-1L);
        permissionDTO.setPermissionName(UUID.randomUUID().toString());
        permissionDTO.setPermissionCode("permissionCode");
        permissionDTO.setPermissionType("M");
        permissionDTO.setDisplayOrder(1);
        permissionDTO.setIcon("");
        permissionDTO.setRoutePath("");
        permissionDTO.setComponentPath("");
        permissionDTO.setUrlParams("");
        permissionDTO.setIsFrame(true);
        permissionDTO.setIsAllowModified(true);
        permissionDTO.setIsSystemd(true);
        permissionDTO.setIsVisible(true);
        return permissionDTO;

    }
}
