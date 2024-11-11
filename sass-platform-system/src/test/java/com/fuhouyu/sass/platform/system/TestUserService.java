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

import com.fuhouyu.sass.platform.system.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户存储层测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/28 17:31
 */
class TestUserService extends TestBaseService {


    @Autowired
    private UserService userService;


    @Test
    void testUserRepository() {
        // 保存
        UserinfoDTO userinfoDTO = this.generateUserDTO();
        this.userService.save(userinfoDTO);
        Long saveId = userinfoDTO.getId();
        Assertions.assertNotNull(saveId, "返回的id为空");

        Assertions.assertThrowsExactly(DuplicateKeyException.class, () -> this.userService.save(userinfoDTO),
                "二次保存，用户名冲突未正常抛出异常");

        // 修改
        String realName = userinfoDTO.getRealName();
        userinfoDTO.setRealName(super.getUUIDStr(8));
        this.userService.edit(userinfoDTO);
        Assertions.assertNotEquals(realName, userinfoDTO.getRealName(), "修改未成功");

        // 查询
        UserinfoDTO queryById = this.userService.findById(saveId);
        Assertions.assertNotNull(queryById, "未查询到对应数据");
        UserinfoDTO queryByUsername = this.userService.findByUsername(userinfoDTO.getUsername());
        Assertions.assertNotNull(queryByUsername, "未查询到对应数据");

        // 删除
        int result = this.userService.removeById(saveId);
        Assertions.assertEquals(1, result, "数据删除不正常");


    }


    private UserinfoDTO generateUserDTO() {
        UserinfoDTO userEntity = new UserinfoDTO();
        userEntity.setUsername(super.getUUIDStr(8));
        userEntity.setRealName(super.getUUIDStr(8));
        userEntity.setNickname(super.getUUIDStr(8));
        userEntity.setEmail(super.getUUIDStr(8));
        userEntity.setGender(super.getUUIDStr(8));
        userEntity.setAvatar(super.getUUIDStr(8));
        userEntity.setLoginDate(LocalDateTime.now());
        userEntity.setLoginIp("127.0.0.1");
        return userEntity;

    }
}
