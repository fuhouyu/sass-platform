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


import {request} from "@/utils";
import {UserModel} from "@/model/user";
import {PageQueryModel, PageResultModel} from "@/model/page";


const baseUserUrl = '/v1/user'

/**
 * 获取用户详情
 */
const getUserinfoApi = (): Promise<UserModel> => request.get(`${baseUserUrl}/info`);


/**
 * 用户详情修改api
 * @param editUserinfo 修改用户详情
 */
const editUserinfoApi =
    (editUserinfo: UserModel): Promise<void> => request.put(`${baseUserUrl}/info`, editUserinfo, {})

/**
 * 通过用户id修改详请
 * @param editUserinfo 修改用户详情
 */
const editUserinfoByIdApi =
    (editUserinfo: UserModel): Promise<void> => request.put(`${baseUserUrl}/info/${editUserinfo.id}`, editUserinfo)

/**
 * 保存用户详情
 * @param userinfo 用户详情
 */
const saveUserInfoApi = (userinfo: UserModel): Promise<void> => request.post(`${baseUserUrl}/info`, userinfo, {})

/**
 * 获取用户列表
 */
const getUserListApi = <P extends PageQueryModel, R extends object>(pageQuery: P): Promise<PageResultModel<R>> =>
    request.get(`${baseUserUrl}/list`, {
        params: {...pageQuery}
    });

/**
 * 通过用户id获取用户详情
 */
const getUserinfoByIdApi = (id: string): Promise<UserModel> => request.get(`${baseUserUrl}/info/${id}`);

/**
 * 通过id删除用户
 * @param ids 用户集合
 *
 */
const removeUserApi = (ids: string[]): Promise<void> => request.delete(`${baseUserUrl}`, {
    data: ids
});

/**
 * 验证用户名是否存在
 * @param username 用户名称
 */
const validUsernameExistsApi = (username: string): Promise<boolean> => request.get(`${baseUserUrl}/exists?username=${username}`, {})

export {
    saveUserInfoApi,
    editUserinfoApi,
    editUserinfoByIdApi,
    getUserinfoApi,
    getUserListApi,
    getUserinfoByIdApi,
    removeUserApi,
    validUsernameExistsApi
}