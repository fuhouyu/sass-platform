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

import axios, {AxiosInstance} from "axios";
import {getAccessToken, getRefreshToken, removeToken, storeToken} from "@/utils";
import {message, notification} from "antd";
import {UserBind} from "@/model/authentication.tsx";
import {authenticationApi} from "@/apis/authentication.ts";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.ts";
import {useRouterStore} from "@/store";


const request: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? '/api',
    timeout: 10000,
});

/**
 * 请求参数错误
 */
interface ParamError {
    /**
     * 错误码
     */
    code: number;
    /**
     * 错误消息
     */
    message: string;
    /**
     * 错误等级
     */
    errorLevel: string;
}


request.interceptors.request.use(function (config) {

    // 在发送请求之前做些什么
    const {url, params, headers} = config;
    if (!params) {
        config.params = {};
    }
    // 登录，直接放行
    if (url?.includes('/v1/user/login')) {
        removeToken()
        return config;
    }
    const token = getAccessToken();
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

request.interceptors.response.use(async function (response) {
    // 检查 isSuccess 字段
    if (response.data.isSuccess) {
        // 如果 isSuccess 为 true，返回 data 数据
        return response.data.data;
    }
    const pathname = window.location.pathname;
    // 如果 isSuccess 为 false，抛出异常
    if (response.data.code === 402) {
        const refreshToken = getRefreshToken();
        if (refreshToken) {
            // 刷新token
            const res = await authenticationApi.refreshTokenApi(refreshToken);
            if (!res) {
                removeToken();
                useRouterStore.getState().router?.navigate(BaseUrlConstant.LOGIN_URL, {state: {from: pathname}}).then();
                return;
            }
            storeToken(res);
            window.location.reload();
            return;
        }
        removeToken();
        useRouterStore.getState().router?.navigate(BaseUrlConstant.LOGIN_URL, {state: {from: pathname}}).then();
        return
    }
    if (response.data.code === 403) {
        removeToken();
        useRouterStore.getState().router?.navigate(BaseUrlConstant.LOGIN_URL, {state: {from: pathname}}).then();
        return
    }
    // 如果是1001，表示用户需要绑定
    if (response.data.code === 400) {
        const paramErrors = response.data.data as ParamError[];
      paramErrors.forEach(({message, errorLevel}) => {
            // 根据 errorLevel 判断通知的类型
            if (errorLevel === 'ERROR') {
                notification.error({
                    showProgress: true,
                    message: '错误',
                    description: message,
                });
            } else if (errorLevel === 'WARNING') {
                notification.warning({
                    showProgress: true,
                    message: '警告',
                    description: message,
                });
            } else if (errorLevel === 'INFO') {
                notification.info({
                    showProgress: true,
                    message: '信息',
                    description: message,
                });
            }
        });
        return Promise.reject(new Error('参数错误，请检查表单输入.'));

    }
    if (response.data.code === 1001) {
        const isUserBind = response.headers['x-user-bind']; // 是否绑定账号
        const userBindToken = response.headers['x-user-bind-temporary-token']; // 绑定账号临时token
        if (isUserBind) {
            return {
                isUserBind,
                userBindToken
            } as UserBind;
        }
    }

    if (response.data.message) {
        const error = new Error(response.data.message);
        await message.error(error.message);
        return Promise.reject(error);
    }
    return response.data;

}, function (error: Error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    return Promise.reject(error);
});

export {request};
