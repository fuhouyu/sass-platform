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

import {IUserPosition} from "@/types/user";
import {request} from "@/utils";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";

class UserPositionApi {

    _baseUrl: string


    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * 保存用户职务
     * @param userPosition 用户职务
     */
    saveUserPosition: (userPosition: IUserPosition) => Promise<void> = (userPosition: IUserPosition): Promise<void> => request.post(`${this._baseUrl}`, userPosition)

    /**
     * 通过组织id和用户id删除职位
     * @param organizationId 组织id
     * @param userIds 用户id集合
     */
    deleteUserPosition: (organizationId: string, userIds: string[]) => Promise<void> = (organizationId: string, userIds: string[]): Promise<void> =>
        request.delete(`${this._baseUrl}/${organizationId}`, {data: userIds})
}

/**
 * 用户职务api
 */
export const userPositionApi = new UserPositionApi(BaseApiUrlConstant.USER_POSITION_API_PREFIX)
