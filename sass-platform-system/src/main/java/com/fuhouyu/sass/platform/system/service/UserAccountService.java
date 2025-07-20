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

import com.fuhouyu.sass.platform.system.domain.dto.account.ThirdPartyBindPlatformDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.AuthenticationPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserLoginDTO;

/**
 * <p>
 * 账号接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 18:10
 */
public interface UserAccountService {

    /**
     * 通过账号id进行登录
     *
     * @param userLoginDTO 账号dto对象
     * @return token实体
     */
    UserTokenDTO login(UserLoginDTO userLoginDTO);

    /**
     * 通行密钥登录
     *
     * @param authenticationPasskeyDTO dto对象
     * @return token实体
     */
    UserTokenDTO loginByPasskey(AuthenticationPasskeyDTO authenticationPasskeyDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 登录并绑定第三方账号
     *
     * @param thirdPartyBindPlatformDTO 第三方账号dto对象
     * @return 用户token
     */
    UserTokenDTO loginBindThirdParty(ThirdPartyBindPlatformDTO thirdPartyBindPlatformDTO);

    /**
     * 管理员登录
     *
     * @param userLoginDTO 用户登录的dto对象
     * @return 用户token dto对象
     */
    UserTokenDTO adminLogin(UserLoginDTO userLoginDTO);

    /**
     * 通过刷新令牌更新token
     *
     * @param refreshToken 刷新令眚
     * @return 用户token dto对象
     */
    UserTokenDTO refreshToken(String refreshToken);
}
