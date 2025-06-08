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


import {request} from "@/utils";
import {IMenu} from "@/types/menu";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {DefaultApiImpl} from "@/apis/baseApi.ts";


class PermissionApi extends DefaultApiImpl<IMenu> {
    constructor() {
        super(BaseApiUrlConstant.PERMISSION_API_PREFIX);
    }

    /**
     *  获取当前用户的权限api
     */
    getUserPermissionApi: () => Promise<IMenu[]> = (): Promise<IMenu[]> => request.get(`${this._baseUrl}/me`);

    /**
     * 查询子集列表
     * @param parentId 父级id，为空时查询出一级
     */
    getPermissionListApi: (parentId?: string) => Promise<IMenu[]> = (parentId?: string): Promise<IMenu[]> => {
      return parentId ? request.get(`${this._baseUrl}/list/${parentId}`) : request.get(`${this._baseUrl}/list`);
    }

    /**
     * 权限树选择器
     */
    getPermissionTreeSelect: () => Promise<IMenu[]> = (): Promise<IMenu[]> => {
      return request(`${this._baseUrl}/tree`)
    }

    /**
     * 检查权限是否存在
     * @param permissionCode 权限编码
     */
    checkPermissionCodeExistsApi: (permissionCode: string) => Promise<boolean> = (permissionCode: string): Promise<boolean> =>
      request.get(`${this._baseUrl}/exists?permissionCode=${permissionCode}`)
}

/**
 * 权限api
 */
export const permissionApi: PermissionApi = new PermissionApi();
