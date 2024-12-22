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


import {useEffect} from "react";
import {useTranslation} from "react-i18next";
import './index.scss'

declare global {
    interface Window {
        wlQrcodeLogin?: (config: {
            id: string;
            redirect_uri: string;
            client_id: string;
            response_type?: string;
            scope?: string;
            state?: string;
            style?: string;
            self_redirect?: string;
            lang?: string;
            nameCN?: string;
            nameEN?: string;
            isHideName?: boolean;
            height?: number;
            width?: number;
        }) => void;
    }
}

// 生成随机字符的函数
const generateRandomChars = () => {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < 8; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
};

export const WeLinkLogin = () => {
    const state: string = generateRandomChars();

    const {t} = useTranslation();
    useEffect(() => {
        openWeLinkQr();
        window.addEventListener("message", handleLoginCode);

        // 清理事件监听
        return () => {
            window.removeEventListener("message", handleLoginCode);
        };
    }, []);

    /**
     * 处理扫码事件
     * @param event 事件
     */
    const handleLoginCode = (event: MessageEvent) => {
        const origin = event.origin;
        if (origin == "https://login.welink.huaweicloud.com") { //判断是否来自WeLink wlLogin扫码事件。 测试环境先注释，上线后需放开
            const loginCode = event.data;
            //拿到loginCode后就可以在这里构造跳转链接进行跳转了
            console.log("loginCode", loginCode);
            redirectWithCode(loginCode);
        }
    };


    /**
     * 重定向到指定链接
     * @param code code
     */
    const redirectWithCode = (code: string) => {
        const serverUrl = "https://login.welink.huaweicloud.com/sso/oauth2/sns_authorize"
        const client_id = import.meta.env.VITE_WELINK_CLIENT_ID;
        const response_type = "code";
        const scope = "snsapi_login";
        const redirect_uri = encodeURIComponent(import.meta.env.VITE_LGOIN_REDIRECT_URI + "?accountType=WELINK");

        const url = serverUrl + "?"
            + "client_id" + "=" + client_id + "&"
            + "response_type" + "=" + response_type + "&"
            + "scope" + "=" + scope + "&"
            + "state" + "=" + "234kki55o4k4i4i" + "&"
            + "redirect_uri" + "=" + redirect_uri + "&"
            + "code" + "=" + code;
        console.log("=======", url);
        window.location.href = url;
    }

    /**
     * 打开weLink打码
     */
    const openWeLinkQr = () => {
        const script = document.createElement("script");
        script.src = "/js/weLink.js"; // 替换为实际路径
        script.async = true;
        script.onload = () => {
            if (window.wlQrcodeLogin) {

                window.wlQrcodeLogin({
                    id: "qrcode-frame", // 放置二维码的容器 ID
                    state: state,
                    redirect_uri: import.meta.env.VITE_LGOIN_REDIRECT_URI,
                    client_id: import.meta.env.VITE_WELINK_CLIENT_ID,
                    response_type: "code",
                    scope: "snsapi_login",
                    lang: "cn",
                    isHideName: true,
                    style: 'border:none;background-color:#FFFFFF;&isHideName=true',
                    height: 400,
                    width: 365,
                });
            }
        };
        document.body.appendChild(script);
    }

    return (
        <div className={'qrcode-container'}>
            <div className={'qrcode-title'}>
                <p dangerouslySetInnerHTML={{__html: t('Login.weLinkQRTips')}}></p>
            </div>
            <div id={'qrcode-frame'}/>
        </div>
    )
}