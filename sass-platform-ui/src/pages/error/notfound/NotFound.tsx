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

import {useEffect} from "react";
import {getAccessToken} from "@/utils";
import {Button, Result} from "antd";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {useLocation, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

export const NotFound = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const {t} = useTranslation();
    // 如果用户未登录，跳回登录页
    useEffect(() => {
        const accessToken = getAccessToken();
        if (!accessToken) {
            navigate(BaseUrlConstant.LOGIN_URL, {state: {from: location.pathname}});
        }
    }, [location.pathname, navigate])
    const backHome = () => {
        navigate(BaseUrlConstant.HOME_URL);
    }
    return (
        <Result
            status="404"
            title="404"
            subTitle={t('Common.pageNotfound')}
            extra={<Button type="primary" onClick={backHome}>
                {t('Common.backHome')}
            </Button>}
        />
    )
}