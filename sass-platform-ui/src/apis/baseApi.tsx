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


import {PageQuery, PageResult} from "@/model/pageQuery";
import {request} from "@/utils";

/**
 * 基类通用的api
 */
interface BaseApi<T> {
    /**
     * api基础url
     */
    baseUrl: string;

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
    pageInfoListApi: (pageQuery: PageQuery) => Promise<PageResult<T>>;

}


/**
 * 默认实现
 */
export class DefaultApiImpl<T> implements BaseApi<T> {
    /**
     * baseUrl
     */
    baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    saveInfoApi = (info: T): Promise<string> => {
        return request.post(this.baseUrl, info);
    }

    editInfoApi = (id: string, info: T): Promise<void> => {
        return request.put(`${this.baseUrl}/${id}`, info);
    };

    getInfoByIdApi = (id: string): Promise<T> => {
        return request.get(`${this.baseUrl}/${id}`);
    };

    getInfoMeApi = (): Promise<T> => {
        return request.get(`${this.baseUrl}/me`);
    }

    deleteInfoApi = (ids: string[]): Promise<void> => {
        return request.delete(`${this.baseUrl}`, {data: ids});
    };

    pageInfoListApi = (pageQuery: PageQuery): Promise<PageResult<T>> => {
        return request.get(`${this.baseUrl}/page`, {params: pageQuery})
    }


}
