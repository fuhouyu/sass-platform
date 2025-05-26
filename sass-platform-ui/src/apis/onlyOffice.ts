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
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {IOnlyOffice as OnlyOfficeModal} from '@/types/office'

class OnlyOfficeApi {
    private readonly _baseUrl: string;

    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * office 预览
     * @param id 主键id
     */
    view: (id: string) => Promise<OnlyOfficeModal> = (id: string): Promise<OnlyOfficeModal> =>
        request.get(`${this._baseUrl}/${id}`);


    /**
     * office edit
     * @param id 主键id
     */
    edit: (id: string) => Promise<OnlyOfficeModal> = (id: string): Promise<OnlyOfficeModal> =>
        request.get(`${this._baseUrl}/${id}/edit`);

}

export const onlyOfficeApi: OnlyOfficeApi = new OnlyOfficeApi(BaseApiUrlConstant.OFFICE_API_URL);
