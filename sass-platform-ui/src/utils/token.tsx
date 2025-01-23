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

import {UserToken} from "@/model/authentication";

/**
 * accessTokenKey
 */
const ACCESS_TOKEN_KEY: string = "ACCESS_TOKEN";

/**
 * 刷新令牌
 */
const REFRESH_TOKEN_KEY: string = "REFRESH_TOKEN";

const storeToken = (token: UserToken) => {
    localStorage.setItem(ACCESS_TOKEN_KEY, token.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, token.refreshToken);
}

const removeToken = () => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
}

/**
 * 获取认证令牌
 */
const getAccessToken: () => (null | string) = () => localStorage.getItem(ACCESS_TOKEN_KEY);

/**
 * 获取刷新令牌
 */
const getRefreshToken: () => (null | string) = () => localStorage.getItem(REFRESH_TOKEN_KEY);


export {storeToken, removeToken, getAccessToken, getRefreshToken}