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

import {IPageQuery, IPageResult} from "@/types/pageQuery";
import {request} from "@/utils";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {IOnlineUser} from "@/types/onlineUser";
import {Key} from "react";

class OnlineUserApi {
    _baseUrl: string;

    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * 在线用户
     * @param pageQuery 分页查询对象
     */
    onlineUserList: (pageQuery: IPageQuery) => Promise<IPageResult<IOnlineUser>> = (pageQuery: IPageQuery): Promise<IPageResult<IOnlineUser>> =>
        request.get(`${this._baseUrl}/list`, {
            params: {...pageQuery}
        });

    /**
     * 强制登出用户
     * @param sessionIds 会话ids
     */
    logout: (sessionIds: string[] | Key[]) => Promise<void> = (sessionIds: string[] | Key[]): Promise<void> =>
        request.delete(`${this._baseUrl}`, {
            data: [...sessionIds]
        })
}

export const onlineUserApi: OnlineUserApi = new OnlineUserApi(BaseApiUrlConstant.ONLINE_USER_URL)
