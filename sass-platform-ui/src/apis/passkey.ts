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

import {request} from "@/utils";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {Passkeys} from "@/types/passkeys";


class PasskeyApi {
  baseUrl: string;

  constructor() {
    this.baseUrl = BaseApiUrlConstant.PASSKEY_API_PREFIX;
  }

  /**
   * 通行证密钥生成
   */
  passkeyGeneratorApi = (): Promise<string> => request.get(`${this.baseUrl}/attestation-options/generate`);


  /**
   * 通行证密钥注册
   * @param passkeyName 通行证密钥名称
   * @param registrationResponse 注册的响应信息
   */
  registerPasskeyApi = ({passkeyName, registrationResponse}: {
    registrationResponse: string,
    passkeyName: string
  }) => request.post(`${this.baseUrl}/register`, {
    passkeyName,
    registrationResponse
  });

  /**
   * 通行密钥列表
   */
  passkeyListApi = ():Promise<Passkeys[]> => request.get(`${this.baseUrl}/list/me`)

  /**
   * 通过通行密钥id删除
   * @param passkeyId 通行密钥id
   */
  deleteByPasskeyIdApi = (passkeyId: string) => request.delete(`${this.baseUrl}?passkeyId=${passkeyId}`);

  /**
   * 通过用户名查询
   * @param username 用户名
   */
  queryPasskeyByUsername = (username: string):Promise<string> => request.get(`${this.baseUrl}/attestation-options?username=${username}`);
}


export const passkeyApi = new PasskeyApi();
