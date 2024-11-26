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
import {UserAuthentication, UserToken} from "@/model/authentication";
import {BaseUrlConstant} from "@/constants/baseUrlConstant";

const baseAuthUrl = BaseUrlConstant.AUTHENTICATION_API_PREFIX


/**
 * 用户登录
 * @param loginData 登录的表单信息
 */
const loginApi = (loginData: UserAuthentication): Promise<UserToken> =>
    request.post(`${baseAuthUrl}/login`, loginData)


/**
 * 退出登录
 */
const logoutApi = (): Promise<void> => request.post(`${baseAuthUrl}/logout`);


export {
    loginApi,
    logoutApi
}