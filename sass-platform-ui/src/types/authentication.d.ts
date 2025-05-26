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

/**
 * 用户认证
 */
export interface IUserAuthentication {
    /**
     * 登录标识
     */
    identify: string;
    /**
     * 登录凭证
     */
    credentials?: string;
    /**
     * 账号类型
     */
    accountType: string;

    /**
     * cloudflare 验证的token
     */
    cloudflareTurnstileToken?: string;

    /**
     * 租户id
     */
    tenantId?: string;
}


/**
 * 三方平台账号绑定
 */
export interface IThirdPartyBindAuthentication extends IUserAuthentication {

    /**
     * 临时token
     */
    temporaryToken: string;

}


/**
 * 用户token对象
 */
export interface UserToken {
    /**
     * 认证令牌
     */
    accessToken: string;
    /**
     * 刷新令牌
     */
    refreshToken: string;
    /**
     * 认证令牌过期时间
     */
    accessTokenExpireAt?: Date;
    /**
     * 认证令牌签发时长，单位：秒
     */
    accessTokenExpireSeconds?: number;
    /**
     * 认证令牌签发时间
     */
    accessTokenIssuedAt?: Date;
    /**
     * 刷新令牌过期时间
     */
    refreshTokenExpireAt?: Date;
    /**
     * 刷新令牌签发时长，单位：秒
     */
    refreshTokenExpireSeconds?: number;
    /**
     * 刷新令牌签发时间
     */
    refreshTokenIssuedAt?: Date;
    /**
     * token类型，一般为bearer
     */
    tokenType?: string;
}


/**
 * 用户绑定
 */
export interface UserBind {
    /**
     * 用户绑定
     */
    isUserBind: boolean;
    /**
     * 用户绑定的临时token
     */
    userBindToken: string;
}
