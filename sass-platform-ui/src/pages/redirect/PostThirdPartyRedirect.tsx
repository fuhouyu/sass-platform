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


import {useLocation, useSearchParams} from "react-router-dom";
import {useCallback, useEffect} from "react";
import {fetchLogin} from "@/store/modules/user.tsx";
import {AccountType} from "@/model/account.tsx";
import {router} from "@/routes/routers.tsx";
import {BASE_LOGIN_URL, BASE_PORTAL_URL, BASE_USER_PROFILE_URL} from "@/constants/commonConstant.tsx";
import {message, Spin} from "antd";
import {useAppDispatch} from "@/store";
import {accountApi} from "@/apis/account.tsx";

export const PostThirdPartyRedirect = () => {

    const [searchParams] = useSearchParams();
    const location = useLocation();
    const dispatch = useAppDispatch();

    /**
     * 用户登录
     */
    const login = useCallback((accountType: string, code: string) => {
        dispatch(fetchLogin({accountType: accountType as AccountType, identify: code})).then(async () => {
            router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
        }).catch((err: Error) => {
            message.error(err.message).then()
            router.navigate(BASE_LOGIN_URL, {state: location.state}).then();
        })
    }, [dispatch, location.state]);

    /**
     * 账号绑定
     * @param code 授权码
     * @param accountType 账号类型
     */
    const bindAccount = useCallback(async (accountType: string, code: string) => {
        await accountApi.bindThirdPartyAccount(accountType, code);
        router.navigate(BASE_USER_PROFILE_URL).then();
    }, []);


    useEffect(() => {
        const redirectType = searchParams.get('redirectType');
        const code = searchParams.get('code')!;
        const accountType = searchParams.get('accountType')!;
        if (redirectType == 'bind') {
            bindAccount(accountType, code).then(() => {
                message.success('绑定成功').then();
            });
        } else {
            login(accountType, code);
        }

    }, [bindAccount, login, searchParams]);


    return (
        <Spin delay={500} tip="处理中，请稍候..." fullscreen={true} size="large" className="page-loading"/>
    )
};