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
import {
  IThirdPartyBindAuthentication,
  IUserAuthentication,
  UserBind,
  UserToken
} from "@/types/authentication";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";


class AuthenticationApi {
  baseUrl: string;

  constructor() {
    this.baseUrl = BaseApiUrlConstant.AUTHENTICATION_API_PREFIX;
  }

  /**
   * 用户登录
   * @param loginData 登录的表单信息
   */
  adminLoginApi = (loginData: IUserAuthentication): Promise<UserToken | UserBind> =>
    request.post(`${this.baseUrl}/admin-login`, loginData);


  /**
   * 刷新token
   * @param refreshToken 刷新令牌
   */
  refreshTokenApi = (refreshToken: string): Promise<UserToken> =>
    request.put(`${this.baseUrl}/refresh-token`, {
      refreshToken
    })

  /**
   * 登录的
   * @param loginData
   */
  loginBindApi = (loginData: IThirdPartyBindAuthentication): Promise<UserToken> =>
    request.post(`${this.baseUrl}/login-bind`, loginData);

  /**
   * 退出登录
   */
  logoutApi = (): Promise<void> => request.post(`${this.baseUrl}/logout`);
}


export const authenticationApi = new AuthenticationApi();
