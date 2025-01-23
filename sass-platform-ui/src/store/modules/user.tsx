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

import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Userinfo,} from "@/model/user";
import {ThirdPartyBindAuthentication, UserAuthentication, UserToken} from "@/model/authentication";
import {Menu} from "@/model/menu";
import {userApi} from "@/apis/user";
import {permissionApi} from "@/apis/permission";
import {removeToken, storeToken} from "@/utils";
import {authenticationApi} from "@/apis/authentication.tsx";


const userStore = createSlice({
    name: "user",
    initialState: {
        token: {
            accessToken: "",
            refreshToken: "",
        },
        userinfo: {},
        // 用户菜单
        userMenus: [] as Menu[],
    },
    reducers: {
        storeToken: (state, action: PayloadAction<UserToken>) => {
            state.token = action.payload;
            storeToken(state.token)
            return state;
        },
        storeUserinfo: (state, action: PayloadAction<Userinfo>) => {
            state.userinfo = action.payload;
            return state;
        },
        storeMenu: (state, action: PayloadAction<Menu[]>) => {
            state.userMenus = action.payload;
            return state;
        },
        logout: (state) => {
            state.userinfo = {};
            state.token = {
                accessToken: "",
                refreshToken: "",
            };
            state.userMenus = [];
            return state;
        }
    },
});

/**
 * 用户登录接口
 * @param loginForm 表单参数
 */
const fetchLogin = (loginForm: UserAuthentication) => {
    return async (dispatch: (arg0: { payload: UserToken; type: `user/${string}` }) => void) => {
        const authenticationRes = await authenticationApi.loginApi(loginForm);
        if ('isUserBind' in authenticationRes) {
            return authenticationRes;
        }
        if (authenticationRes) {
            dispatch(userStore.actions.storeToken(authenticationRes));
        }
    }
}


/**
 * 用户登录且绑定
 */
const fetchLoginBind = (loginForm: ThirdPartyBindAuthentication) => {
    return async (dispatch: (arg0: { payload: UserToken; type: `user/${string}` }) => void) => {
        const authenticationRes = await authenticationApi.loginBindApi(loginForm);
        if (authenticationRes) {
            dispatch(userStore.actions.storeToken(authenticationRes));
        }
    }
}


/**
 * 用户详情接口
 */
const fetchUserinfo = () => {
    return async (dispatch: (arg0: { payload: Userinfo; type: `user/${string}` }) => void) => {
        const res: Userinfo = await userApi.getInfoMeApi();
        dispatch(userStore.actions.storeUserinfo(res))
    }
}

/**
 * 获取menus
 */
const fetchUserMenus = () => {
    return async (dispatch: (arg0: { payload: Menu[]; type: `user/${string}` }) => Menu[]): Promise<Menu[]> => {
        const menus = await permissionApi.getUserPermissionApi();
        if (menus) {
            dispatch(userStore.actions.storeMenu(menus));
        }
        return menus;
    }
}

/**
 * 用户退出登录
 */
const fetchLogout = () => {
    return async (dispatch: (arg0: { payload: undefined; type: `user/${string}` }) => void) => {
        await authenticationApi.logoutApi();
        dispatch(userStore.actions.logout())
        removeToken()
    }
}

/**
 * 修改用户详情
 * @param editUserinfo 用户详情接口修改
 */
const fetchEditUserinfo = (editUserinfo: Userinfo) => {
    return async (dispatch: (arg0: { payload: Userinfo; type: `user/${string}` }) => void) => {
        await userApi.editInfoApi(editUserinfo.id!, editUserinfo);
        const res = await userApi.getInfoByIdApi(editUserinfo.id!);
        dispatch(userStore.actions.storeUserinfo(res))
    }
}

export {
    fetchLogin,
    fetchLogout,
    fetchUserinfo,
    fetchEditUserinfo,
    fetchUserMenus,
    fetchLoginBind
};

export default userStore.reducer;