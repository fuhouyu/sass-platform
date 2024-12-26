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

    /**
     * 获取当前自己绑定的账号
     */
    getAccountForMe: () => Promise<Account[]> = (): Promise<Account[]> =>
        request.get(`${this.baseUrl}/me`);

    /**
     * 绑定第三方账号
     * @param accountType 账号类型
     * @param code 临时授权码
     */
    bindThirdPartyAccount: (accountType: string, code: string) => Promise<Account> = (accountType: string, code: string): Promise<Account> =>
        request.post(`${this.baseUrl}/bind`, {account: code, accountType: accountType});


    /**
     * 取消绑定第三方账号
     * @param accountType 账号类型
     * @param account 账号
     */
    unbindThirdPartyAccount: (accountType: string, account: string) => Promise<void> = (accountType: string, account: string): Promise<void> =>
        request.delete(`${this.baseUrl}/unbind`, {data: {accountType: accountType, account: account}});
}


export const accountApi = new AccountApi();