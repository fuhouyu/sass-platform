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
import {Position} from "@/model/position.tsx";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {request} from "@/utils";

class PositionApi extends DefaultApiImpl<Position> {

    constructor() {
        super(BaseUrlConstant.POSITION_API_PREFIX);
    }

    /**
     * 检查职位是否存在
     * @param positionCode 职位编码
     */
    checkPositionCode: (positionCode: string) => Promise<boolean> = (positionCode: string): Promise<boolean> =>
        request.get(`${this.baseUrl}/exists?positionCode=${positionCode}`)
}

export const positionApi = new PositionApi();