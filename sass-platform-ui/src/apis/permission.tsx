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
import {Menu} from "@/model/menu";
import {BaseUrlConstant} from "@/constants/baseUrlConstant";
import {DefaultApiImpl} from "@/apis/baseApi";


class PermissionApi extends DefaultApiImpl<Menu> {
    constructor() {
        super(BaseUrlConstant.PERMISSION_API_PREFIX);
    }

    /**
     *  获取当前用户的权限api
     */
    getUserPermissionApi = (): Promise<Menu[]> => request.get(`${this.baseUrl}/me`);

    /**
     * 查询子集列表
     * @param parentId 父级id，为空时查询出一级
     */
    getPermissionListApi: (parentId?: string) => Promise<Menu[]> = (parentId?: string): Promise<Menu[]> => {
        return parentId ? request.get(`${this.baseUrl}/list/${parentId}`) : request.get(`${this.baseUrl}/list`);
    }
}

/**
 * 权限api
 */
export const permissionApi: PermissionApi = new PermissionApi();