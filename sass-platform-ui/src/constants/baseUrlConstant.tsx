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


/**
 * api常量
 */
export class BaseApiUrlConstant {

    /**
     * 版本
     */
    static readonly VERSION: string = '/v1';

    /**
     * 用户api
     */
    static readonly ADMIN_USER_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/admin-user`;

    /**
     *认证api
     */
    static readonly AUTHENTICATION_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/auth`;

    /**
     * 权限api
     */
    static readonly PERMISSION_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/permission`;

    /**
     * 租户api
     */
    static readonly TENANT_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/tenant`;


    /**
     * 角色api
     */
    static readonly ROLE_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/role`;

    /**
     * 字典类型api
     */
    static readonly DICT_TYPE_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/dict-type`;

    /**
     * 字典项api
     */
    static readonly DICT_ITEM_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/dict-item`;

    /**
     * 账号api接口
     */
    static readonly ACCOUNT_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/account`;

    /**
     * 组织api接口
     */
    static readonly ORGANIZATION_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/organization`;

    /**
     * 用户角色api接口
     */
    static readonly USER_HAS_ROLE_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/user-role`;

    /**
     * 用户职务api接口
     */
    static readonly USER_POSITION_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/user-position`;

    /**
     * 资源api接口
     */
    static readonly RESOURCE_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/resource`;

    /**
     * 租户空间api接口
     */
    static readonly TENANT_SPACE_API_PREFIX: string = `${BaseApiUrlConstant.VERSION}/tenant-space`

    /**
     * office url
     */
    static readonly OFFICE_API_URL: string = `${BaseApiUrlConstant.VERSION}/office`;

    /**
     * 操作日志
     */
    static readonly OPERATION_LOG_URL: string = `${BaseApiUrlConstant.VERSION}/log`;

    /**
     * 参数配置
     */
    static readonly PARAM_CONFIG_URL: string = `${BaseApiUrlConstant.VERSION}/param-config`

    /**
     * 服务监控
     */
    static readonly SERVER_MONITOR: string = `${BaseApiUrlConstant.VERSION}/monitor/server`

    /**
     * 日志监控
     */
    static readonly LOG_MONITOR: string = `${BaseApiUrlConstant.VERSION}/monitor/log`
}

/**
 * url 常量
 */
export class BaseUrlConstant {
    /**
     * 登录页
     */
    static readonly LOGIN_URL: string = '/login';

    /**
     * 第三方登录的回调地址
     */
    static readonly REDIRECT_URL: string = '/redirect';

    /**
     * 首页
     */
    static readonly HOME_URL: string = '/home';

    /**
     * 个人资料
     */
    static readonly USER_PROFILE_URL: string = '/profile';

    /**
     * 租户空间
     */
    static readonly TENANT_SPACE_URL: string = '/tenant-space';

    /**
     * office预览
     */
    static readonly OFFICE_URL: string = '/office/preview';
}