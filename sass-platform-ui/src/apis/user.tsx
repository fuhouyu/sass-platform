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
import {UserinfoInterface} from "@/model/user";
import {PageQuery} from "@/model/page";


const baseUserUrl = '/v1/user'
/**
 * 用户登录
 * @param loginData 登录的表单信息
 */
const loginApi = (loginData: string) =>
    request.post(`${baseUserUrl}/login`, loginData, {
        headers: {
            'Authorization': 'Basic dGVzdDE6cGFzc3dvcmQ='
        },
    })

/**
 * 获取用户详情
 */
const getUserinfoApi = () => request.get(`${baseUserUrl}/info`);


/**
 * 用户详情修改api
 * @param editUserinfo 修改用户详情
 */
const editUserinfoApi =
    (editUserinfo: UserinfoInterface) => request.put(`${baseUserUrl}/info`, editUserinfo, {})

/**
 * 获取用户列表
 */
const getUserListApi = <P extends PageQuery>(pageQuery: P) =>
    request.get(`${baseUserUrl}/list`, {
        params: {...pageQuery}
    });

/**
 * 退出登录
 */
const logoutApi = () => request.post(`${baseUserUrl}/logout`);


export {
    loginApi,
    logoutApi,
    editUserinfoApi,
    getUserinfoApi,
    getUserListApi
}