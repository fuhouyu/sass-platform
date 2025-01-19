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


import {request} from "@/utils";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";

class UserHasRole {

    private readonly baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    /**
     * 通过用户id查询角色id集合
     * @param userId 用户id
     */
    getRoleIdListByUserId: (userId: string) => Promise<string[]> = (userId: string): Promise<string[]> =>
        request.get(`${this.baseUrl}/${userId}`)

    /**
     * 保存用户和角色的关系
     * @param userId 用户id
     * @param roleIds 角色id集合
     */
    saveUserRole: (userId: string, roleIds: string[]) => Promise<void> = (userId: string, roleIds: string[]): Promise<void> =>
        request.post(`${this.baseUrl}/${userId}`, roleIds);
}

export const userHasRoleApi = new UserHasRole(BaseUrlConstant.USER_HAS_ROLE_API_PREFIX);