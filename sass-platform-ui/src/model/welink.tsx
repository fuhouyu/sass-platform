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


export interface WeLinkQrCodeRequest {
    /**
     * 客户端id
     */
    client_id: string;

    /**
     * 响应类型，固定为code
     */
    response_type: string;

    /**
     * 网页应用使用snsapi_login
     */
    scope: string;

    /**
     * 回调地址，需要与网页端保持一致
     */
    redirect_uri: string;

    /**
     * 是否隐藏二维码下方文字，默认false不隐藏。
     */
    isHideName?: boolean;
}

export interface WeLinkQrCodeResponse {

    /**
     * 二维码唯一id
     */
    unique_id: string;

    /**
     * 二维码url
     */
    qrcode_url: string;

    /**
     * 二维码状态
     */
    code: string;

    /**
     * 返回信息，包括接口请求发生错误时的详细信息。
     */
    message: string;
}