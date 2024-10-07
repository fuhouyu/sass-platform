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

export interface TokenInterface {
    accessTokenExpireSeconds: number;
    refreshTokenExpireSeconds: number;
    tokenType: string;
    accessToken: string;
    refreshToken: string;
    accessTokenIssuedAt: number;
    refreshTokenIssuedAt: number;
    accessTokenExpireAt: number;
    refreshTokenExpireAt: number;
}

interface TokenService {
    getToken: () => TokenInterface | null;
    storeToken: (token: TokenInterface) => void;
    removeToken: () => void;
}

const tokenKey = 'token';
// 本地token存在
const localStoreToken: TokenService = {
    getToken: () => {
        const token = localStorage.getItem(tokenKey);
        if (token) {
            return JSON.parse(token) as TokenInterface;
        }
        return null;
    },
    storeToken: (token: TokenInterface) => {
        localStorage.setItem(tokenKey, JSON.stringify(token))
    },
    removeToken: () => {
        localStorage.removeItem(tokenKey);
    }
}

export default localStoreToken;