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


/**
 * 用户token对象
 */
export interface UserToken {
    /**
     * 认证令牌
     */
    accessToken?: string;
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
     * 刷新令牌
     */
    refreshToken?: string;
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
 * 用户详情
 */
export interface Userinfo {
    /**
     * 头像地址
     */
    avatar?: string;
    /**
     * 邮件地址
     */
    email?: string;
    /**
     * 性别
     */
    gender?: string;
    /**
     * 主键id
     */
    id?: string;
    /**
     * 登录时间
     */
    loginDate?: Date;
    /**
     * 登录ip
     */
    loginIp?: string;
    /**
     * 真实姓名
     */
    realName?: string;
    /**
     * 用户名
     */
    username?: string;
}