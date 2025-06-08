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

/**
 * 基类通用的api
 */
interface BaseApi<T> {
    /**
     * 保存详情
     * @param info 详情
     */
    saveInfoApi: (info: T) => Promise<string>;

    /**
     * 修改详情
     * @param id 主键id
     * @param info 详情
     */
    editInfoApi: (id: string, info: T) => Promise<void>;

    /**
     * 通过id获取详情
     * @param id 主键id
     */
    getInfoByIdApi: (id: string) => Promise<T>;

    /**
     * 删除api
     * @param ids ids
     */
    deleteInfoApi: (ids: string[]) => Promise<void>;

    /**
     * 分页查询
     * @param pageQuery 查询对象
     */
    pageInfoListApi: (pageQuery: IPageQuery) => Promise<IPageResult<T>>;

    /**
     * 修改状态
     * @param id 主键id
     * @param enabled 启禁用状态
     */
    status: (id: string, enabled: boolean) => Promise<void>;
}


/**
 * 默认实现
 */
export class DefaultApiImpl<T> implements BaseApi<T> {
    /**
     * baseUrl
     */
    protected readonly _baseUrl: string;

    constructor(baseUrl: string) {
      this._baseUrl = baseUrl;
    }

    saveInfoApi = (info: T): Promise<string> => {
      return request.post(this._baseUrl, info);
    }

    editInfoApi = (id: string, info: T): Promise<void> => {
      return request.put(`${this._baseUrl}/${id}`, info);
    };

    getInfoByIdApi = (id: string): Promise<T> => {
      return request.get(`${this._baseUrl}/${id}`);
    };

    getInfoMeApi = (): Promise<T> => {
      return request.get(`${this._baseUrl}/me`);
    }

    deleteInfoApi = (ids: string[]): Promise<void> => {
      return request.delete(`${this._baseUrl}`, {data: ids});
    };

    pageInfoListApi = (pageQuery: IPageQuery): Promise<IPageResult<T>> => {
      return request.get(`${this._baseUrl}/page`, {params: pageQuery})
    }

    status: (id: string, enabled: boolean) => Promise<void> = (id: string, enabled: boolean): Promise<void> =>
      request.put(`${this._baseUrl}/${id}/status?enabled=${enabled}`)


}
