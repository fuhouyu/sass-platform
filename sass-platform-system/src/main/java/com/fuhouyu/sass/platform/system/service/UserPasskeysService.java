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
package com.fuhouyu.sass.platform.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.AuthenticationPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.UserPasskeyListDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.RegisterPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.domain.entity.UserPasskey;

import java.util.List;

/**
 * <p>
 * 用户通行密钥接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/19 23:04
 */
public interface UserPasskeysService extends IService<UserPasskey> {


    /**
     * 根据当前登录用户创建公钥凭证创建选项
     *
     * @return 公钥凭证创建选项
     */
    String generateRegistrationOptions();

    /**
     * 注册通行密钥
     * @param registerPasskey 通行密钥
     */
    void registerPasskey(RegisterPasskeyDTO registerPasskey);

    /**
     * 获取当前用户的通行证密钥
     *
     * @return 当前用户的通行证密钥
     */
    List<UserPasskeyListDTO> passkeyList();

    /**
     * 获取当前用户的通行证密钥断言
     * @param username 用户名
     * @return 断言数据
     */
    String getAttestationOptionsByUsername(String username);

    /**
     * 验证通行证密钥
     * @param authenticationPasskeyDTO 通行密钥dto对象
     */
    void verifyAuthentication(AuthenticationPasskeyDTO authenticationPasskeyDTO);
}
