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


import {DefaultApiImpl} from "@/apis/baseApi.tsx";
import {Organization} from "@/model/organization.tsx";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {request} from "@/utils";

class OrganizationApi extends DefaultApiImpl<Organization> {

    constructor() {
        super(BaseUrlConstant.ORGANIZATION_API_PREFIX);
    }

    /**
     * 通过父级id获取子集
     * @param parentId 父级id
     */
    getOrganizationListApi: (parentId?: string) => Promise<Organization[]> = (parentId?: string): Promise<Organization[]> =>
        parentId ? request.get(`${this.baseUrl}/list/${parentId}`) : request.get(`${this.baseUrl}/list`);

    /**
     * 获取组织树
     */
    getOrganizationTreeSelect: () => Promise<Organization[]> = (): Promise<Organization[]> =>
        request.get(`${this.baseUrl}/tree`)
}

export const organizationApi = new OrganizationApi();