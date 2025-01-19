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


/**
 * api常量
 */
export class BaseUrlConstant {

    /**
     * 版本
     */
    static readonly VERSION: string = '/v1';

    /**
     * 用户api
     */
    static readonly USER_API_PREFIX = `${BaseUrlConstant.VERSION}/user`;

    /**
     *认证api
     */
    static readonly AUTHENTICATION_API_PREFIX = `${BaseUrlConstant.VERSION}/auth`;

    /**
     * 权限api
     */
    static readonly PERMISSION_API_PREFIX = `${BaseUrlConstant.VERSION}/permission`;

    /**
     * 租户api
     */
    static readonly TENANT_API_PREFIX = `${BaseUrlConstant.VERSION}/tenant`;


    /**
     * 角色api
     */
    static readonly ROLE_API_PREFIX = `${BaseUrlConstant.VERSION}/role`;

    /**
     * 字典类型api
     */
    static readonly DICT_TYPE_API_PREFIX = `${BaseUrlConstant.VERSION}/dict-type`

    /**
     * 字典项api
     */
    static readonly DICT_ITEM_API_PREFIX = `${BaseUrlConstant.VERSION}/dict-item`

    /**
     * 账号api接口
     */
    static readonly ACCOUNT_API_PREFIX = `${BaseUrlConstant.VERSION}/account`

    /**
     * 组织api接口
     */
    static readonly ORGANIZATION_API_PREFIX = `${BaseUrlConstant.VERSION}/organization`

    /**
     * 用户角色api接口
     */
    static readonly USER_HAS_ROLE_API_PREFIX = `${BaseUrlConstant.VERSION}/user-role`

}