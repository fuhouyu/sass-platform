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
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {Resource, StsTemporaryTokenResponse} from "@/model/resource.tsx";
import {DefaultApiImpl} from "@/apis/baseApi.tsx";

class ResourceApi extends DefaultApiImpl<Resource> {


    constructor(baseUrl: string) {
        super(baseUrl);
    }

    /**
     *生成临时的stsToken
     */
    generateStsToken: () =>
        Promise<StsTemporaryTokenResponse> = (): Promise<StsTemporaryTokenResponse> =>
        request.get(`${this.baseUrl}/sts-token`);

}

export const resourceApi: ResourceApi = new ResourceApi(BaseUrlConstant.RESOURCE_API_PREFIX);