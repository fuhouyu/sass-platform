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

import {IUserinfo,} from "@/types/user";
import {
  IThirdPartyBindAuthentication,
  IUserAuthentication,
  UserBind,
  UserToken
} from "@/types/authentication";
import {IMenu} from "@/types/menu";
import {userApi} from "@/apis/adminUser.ts";
import {permissionApi} from "@/apis/permission.ts";
import {removeToken, storeToken} from "@/utils";
import {authenticationApi} from "@/apis/authentication.ts";
import {create} from "zustand/react";
import {StateCreator} from "zustand";
import {ITenantInfo} from "@/types/tenant";

/**
 * 用户状态
 */
interface UserState {
    /**
     * 用户token
     */
    token: UserToken;
    /**
     * 用户详情
     */
    userinfo: IUserinfo;
    /**
     * 用户菜单
     */
    userMenus: IMenu[] | undefined;

    /**
     * 当前用户的租户
     */
    tenant?: ITenantInfo;
}

/**
 * 用户操作
 */
interface UserAction {
    /**
     * 用户登录
     * @param loginForm 登录表单对象
     */
    fetchLogin: (loginForm: IUserAuthentication) => Promise<UserToken | UserBind>
    /**
     * 刷新用户令牌
     * @param token 刷新令牌
     */
    fetchRefreshToken: (refreshToken: string) => Promise<UserToken>
    /**
     * 登录并绑定
     * @param loginForm 表单对象
     */
    fetchLoginAndBind: (loginForm: IThirdPartyBindAuthentication) => Promise<UserToken>
    /**
     * 用户详情
     */
    fetchUserinfo: () => Promise<IUserinfo>
    /**
     * 用户菜单
     */
    fetchUserMenus: () => Promise<IMenu[]>
    /**
     * 用户登出
     */
    fetchLogout: () => Promise<void>
    /**
     * 修改用户详情
     * @param editUserinfo 用户详情
     */
    fetchEditUserinfo: (editUserinfo: IUserinfo) => Promise<void>,
    /**
     * 存储租户
     * @param tenant 租户
     *
     */
    storeTenant: (tenant?: ITenantInfo) => void;
}

/**
 * 用户切片
 */
const createUserSlice: StateCreator<UserState & UserAction> = (set, get) => ({
    token: {
        accessToken: '',
        refreshToken: '',
    },
    userinfo: {},
    userMenus: undefined,
    tenant: {},
    storeTenant: (tenant) => set({tenant}),
    fetchLogin: async (loginForm) => {
        const authenticationRes = await authenticationApi.adminLoginApi(loginForm);
        if (!('isUserBind' in authenticationRes)) {
            set({token: authenticationRes});
            storeToken(authenticationRes);
        }
        return authenticationRes;
    },

    fetchRefreshToken: async (refreshToken) => {
        const res = await authenticationApi.refreshTokenApi(refreshToken);
        if (res) {
            set({token: res});
            storeToken(res);
        }
        return res;
    },

  fetchLoginAndBind: async (loginForm: IThirdPartyBindAuthentication) => {
        const authenticationRes = await authenticationApi.loginBindApi(loginForm);
        set({token: authenticationRes});
        storeToken(authenticationRes);
        return authenticationRes;
    },

    fetchUserinfo: async () => {
        if (Object.keys(get().userinfo).length > 0) {
            return get().userinfo;
        }
      const res: IUserinfo = await userApi.getInfoMeApi();
        set({userinfo: res});
        return res;
    },

    fetchUserMenus: async () => {
        const menus = await permissionApi.getUserPermissionApi();
        set((state) => ({...state, userMenus: menus ?? undefined}));
        return menus;
    },

    fetchLogout: async () => {
        await authenticationApi.logoutApi();
        set({userinfo: {}, token: {accessToken: '', refreshToken: ''}});
        removeToken();
    },

    fetchEditUserinfo: async (editUserinfo) => {
        await userApi.editInfoApi(editUserinfo.id!, editUserinfo);
        set({userinfo: editUserinfo});
    }
});

/**
 * 用户的store
 */
export const useUserStore = create<UserState & UserAction>((...a) => ({
    ...createUserSlice(...a),
}))
