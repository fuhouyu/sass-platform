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


import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {Account} from "@/model/account.tsx";
import {request} from "@/utils";

class AccountApi {
    baseUrl: string;

    constructor() {
        this.baseUrl = BaseUrlConstant.ACCOUNT_API_PREFIX;
    }

    getAccountForme: () => Promise<Account[]> = async (): Promise<Account[]> =>
        request.get(`${this.baseUrl}/me`);
}


export const accountApi = new AccountApi();