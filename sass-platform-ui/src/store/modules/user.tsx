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

import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {removeToken, storeToken} from "@/utils/Token/token";
import {UserModel,} from "@/model/user";
import {editUserinfoApi, getUserinfoApi} from "@/apis/user";
import {UserAuthenticationModel, UserToken} from "@/model/authentication";
import {loginApi, logoutApi} from "@/apis/authentication";
import {Menus} from "@/model/menus";
import {getUserPermissionApi} from "@/apis/permission";


const userStore = createSlice({
    name: "user",
    initialState: {
        token: {
            accessToken: "",
            refreshToken: "",
        },
        userinfo: {},
        // 用户菜单
        userMenus: [] as Menus[],
    },
    reducers: {
        storeToken: (state, action: PayloadAction<UserToken>) => {
            state.token = action.payload;
            storeToken(state.token)
            return state;
        },
        storeUserinfo: (state, action: PayloadAction<UserModel>) => {
            state.userinfo = action.payload;
            return state;
        },
        storeMenu: (state, action: PayloadAction<Menus[]>) => {
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
const fetchLogin = (loginForm: UserAuthenticationModel) => {
    return async (dispatch: (arg0: { payload: UserToken; type: `user/${string}` }) => void) => {
        const token = await loginApi(loginForm);
        if (token) {
            dispatch(userStore.actions.storeToken(token));
        }

    }
}


/**
 * 用户详情接口
 */
const fetchUserinfo = () => {
    return async (dispatch: (arg0: { payload: UserModel; type: `user/${string}` }) => void) => {
        const res: UserModel = await getUserinfoApi();
        dispatch(userStore.actions.storeUserinfo(res))
    }
}

/**
 * 获取menus
 */
const fetchUserMenus = () => {
    return async (dispatch: (arg0: { payload: Menus[]; type: `user/${string}` }) => Menus[]): Promise<Menus[]> => {
        const menus = await getUserPermissionApi();
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
        await logoutApi();
        dispatch(userStore.actions.logout())
        removeToken()
    }
}

/**
 * 修改用户详情
 * @param editUserinfo 用户详情接口修改
 */
const fetchEditUserinfo = (editUserinfo: UserModel) => {
    return async (dispatch: (arg0: { payload: UserModel; type: `user/${string}` }) => void) => {
        await editUserinfoApi(editUserinfo);
        const res = await getUserinfoApi();
        dispatch(userStore.actions.storeUserinfo(res))
    }
}

export {
    fetchLogin,
    fetchLogout,
    fetchUserinfo,
    fetchEditUserinfo,
    fetchUserMenus
};

export default userStore.reducer;