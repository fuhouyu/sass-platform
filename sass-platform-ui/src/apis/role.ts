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


import {DefaultApiImpl} from "@/apis/baseApi.ts";
import {IRole} from "@/types/role";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {request} from "@/utils";

/**
 * 角色api
 */
class RoleApi extends DefaultApiImpl<IRole> {

    constructor() {
        super(BaseApiUrlConstant.ROLE_API_PREFIX);
    }

    /**
     * 检查角色编码是否存在
     * @param roleCode 角色编码
     */
    checkRoleCodeExists: (roleCode: string) => Promise<boolean> = (roleCode: string): Promise<boolean> => request.get(`${this._baseUrl}/exists`, {params: {roleCode}})

    /**
     * 获取角色列表
     */
    list: () => Promise<IRole[]> = (): Promise<IRole[]> => request.get(`${this._baseUrl}/list`)
}

export const roleApi: RoleApi = new RoleApi();
