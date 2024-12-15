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


import {DefaultApiImpl} from "@/apis/baseApi";
import {DictItem} from "@/model/dictItem";
import {BaseUrlConstant} from "@/constants/baseUrlConstant";
import {request} from "@/utils";

class DictItemApi extends DefaultApiImpl<DictItem> {

    constructor() {
        super(BaseUrlConstant.DICT_ITEM_API_PREFIX);
    }

    /**
     * 检查项编码是否存在
     * @param dictCode 字典项编码
     * @param itemCode 字典项编码
     */
    checkItemCodeExists: (dictCode: string, itemCode: string) => Promise<boolean> = (dictCode: string, itemCode: string): Promise<boolean> =>
        request.get(`${this.baseUrl}/exists?dictCode=${dictCode}&itemCode=${itemCode}`)
}

export const dictItemApi: DictItemApi = new DictItemApi();