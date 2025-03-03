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
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {Resource, StsTemporaryTokenRequest, StsTemporaryTokenResponse} from "@/model/resource.tsx";
import {DefaultApiImpl} from "@/apis/baseApi.tsx";

class ResourceApi extends DefaultApiImpl<Resource> {


    constructor(baseUrl: string) {
        super(baseUrl);
    }

    /**
     *生成临时的stsToken
     */
    generateStsToken: (stsTokenRequest: StsTemporaryTokenRequest) =>
        Promise<StsTemporaryTokenResponse> = (stsTokenRequest: StsTemporaryTokenRequest): Promise<StsTemporaryTokenResponse> =>
        request.post(`${this.baseUrl}/sts-token`, stsTokenRequest);


    /**
     * 通过etag 获取资源
     * @param etag etag
     */
    getResourceByEtag: (etag: string) => Promise<Resource> = (etag: string): Promise<Resource> => request.get(`${this.baseUrl}`, {params: {etag}})

    downloadFile: (id: string) => Promise<void> = (id: string): Promise<void> => request.get(`${this.baseUrl}/download/${id}`)
}

export const resourceApi: ResourceApi = new ResourceApi(BaseApiUrlConstant.RESOURCE_API_PREFIX);