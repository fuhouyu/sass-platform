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
// useTokenRefresh.ts
import {useEffect} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {message} from 'antd';
import {useAppDispatch} from "@/store";
import {getAccessToken, removeToken} from "@/utils";
import {getRefreshToken} from "@/utils/Token/token";
import {fetchLogin} from "@/store/modules/user";
import {AccountType} from "@/constants/accountTypeConstant";

const useAuth = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const pathname = location.pathname;
    const token = getAccessToken();
    const dispatch = useAppDispatch();
    useEffect(() => {
        if (!token) {
            const refreshToken = getRefreshToken();
            if (!refreshToken) {
                message.warning("当前用户登录状态已失效").then();
                navigate('/login', {state: {from: pathname}});
                return;
            }

            // 通过刷新令牌更新token
            dispatch(fetchLogin({identify: refreshToken, accountType: AccountType.REFRESH_TOKEN}))
                .then(() => {
                    navigate(pathname);
                })
                .catch(() => {
                    removeToken();
                    navigate('/login', {state: {from: pathname}});
                });
        }
    }, [dispatch, navigate, pathname, token]);
    return getAccessToken();
};

export default useAuth;
