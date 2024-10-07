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
import {message} from "antd";
import {storeToken, TokenInterface} from "@/utils/token/token";
import {Userinfo} from "@/model/user";
import {getUserinfo, loginApi} from "@/apis/user";


const userStore = createSlice({
    name: "user",
    initialState: {
        token: {},
        userinfo: {}
    },
    reducers: {
        storeToken: (state, action: PayloadAction<TokenInterface>) => {
            state.token = action.payload;
            storeToken(state.token)
        },
        storeUserinfo: (state, action: PayloadAction<Userinfo>) => {
            state.userinfo = action.payload;
        }
    }
});

/**
 * 用户登录接口
 * @param loginForm 表单参数
 * @param callback 登录完成的回调
 */
const fetchLogin = (loginForm: string, callback: () => void) => {
    return async (dispatch: (arg0: { payload: PayloadAction<TokenInterface>; type: `user/${string}` }) => void) => {
        await loginApi(loginForm).then((res: TokenInterface) => {
            message.success('登录成功')
            dispatch(userStore.actions.storeToken(res))
            callback()
        }).catch((err: Error) => {
            message.error(err.message)
        })
    }
}


/**
 * 用户详情接口
 */
const fetchUserinfo = () => {
    return async (dispatch: (arg0: { payload: PayloadAction<Userinfo>; type: `user/${string}` }) => void) => {
        await getUserinfo()
            .then((res: Userinfo) => {
                dispatch(userStore.actions.storeUserinfo(res))
            })
    }
}

export {fetchLogin, fetchUserinfo};

export default userStore.reducer;