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

}