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
import {ParamConfig} from "@/model/paramConfig.tsx";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {request} from "@/utils";

class ParamConfigApi extends DefaultApiImpl<ParamConfig> {

    constructor(baseUrl: string) {
        super(baseUrl);
    }

    /**
     * 检查参数编码是否存在
     * @param configKey 配置key
     */
    checkConfigKeyExists: (configKey: string) => Promise<boolean> = (configKey: string): Promise<boolean> =>
        request.get(`${this.baseUrl}/exists?configKey=${configKey}`)

}

export const paramConfigApi: ParamConfigApi = new ParamConfigApi(BaseApiUrlConstant.PARAM_CONFIG_URL);
