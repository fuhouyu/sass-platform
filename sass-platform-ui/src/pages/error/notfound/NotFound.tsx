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
import {router} from "@/routes/routers";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";

export const NotFound = () => {
    // 如果用户未登录，跳回登录页
    useEffect(() => {
        const accessToken = getAccessToken();
        if (!accessToken) {
            router.navigate(BaseUrlConstant.LOGIN_URL, {state: {from: router.state.location.pathname}}).then();
        }
    }, [])
    const backHome = () => {
        router.navigate(BaseUrlConstant.HOME_URL).then()
    }
    return (
        <Result
            status="404"
            title="404"
            subTitle="当前访问的页面不存在"
            extra={<Button type="primary" onClick={backHome}>
                回到首页
            </Button>}
        />
    )
}