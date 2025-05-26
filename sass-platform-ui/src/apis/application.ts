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
import {IApplication} from "@/types/application";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {request} from "@/utils";

class ApplicationApi extends DefaultApiImpl<IApplication> {

    constructor(baseUrl: string) {
        super(baseUrl);
    }

    /**
     * 检查客户端id是否存在
     * @param clientId 客户端id
     */
    checkClientIdExists: (clientId: string) => Promise<boolean> = (clientId: string) =>
        request.get(`${this.baseUrl}/exists?clientId=${clientId}`);

    /**
     * 获取应用详情
     * @param id 主键id
     */
    getInfoByIdApi = (id: string): Promise<IApplication> => {
        return request.get(`${this.baseUrl}?clientId=${id}`);
    };

    /**
     * 修改状态
     * @param id 客户端id
     * @param enabled 是否启用
     */
    status: (id: string, enabled: boolean) => Promise<void> = (id: string, enabled: boolean): Promise<void> =>
        request.put(`${this.baseUrl}/status?clientId=${id}&enabled=${enabled}`)


    /**
     * 修改应用
     * @param id 应用id
     * @param info 应用信息
     */
    editInfoApi = (id: string, info: IApplication): Promise<void> => {
        return request.put(`${this.baseUrl}`, {...info, id});
    };

    /**
     * 生成客户端密钥
     */
    generateClientSecret: () => Promise<string> = (): Promise<string> =>
        request.get(`${this.baseUrl}/secret/generate`);
}

export const applicationApi: ApplicationApi = new ApplicationApi(BaseApiUrlConstant.APPLICATION_MANAGE_URL);
