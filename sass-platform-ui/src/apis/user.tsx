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
import {Userinfo} from "@/model/user";
import {BaseUrlConstant} from "@/constants/baseUrlConstant";
import {DefaultApiImpl} from "@/apis/baseApi";


const baseUserUrl = BaseUrlConstant.USER_API_PREFIX;

class UserApi extends DefaultApiImpl<Userinfo> {

    constructor() {
        super(baseUserUrl);
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     */
    checkUsernameExistsApi = (username: string): Promise<boolean> => {
        return request.get(`${baseUserUrl}/exists?username=${username}`, {})
    }
}

export const userApi: UserApi = new UserApi()