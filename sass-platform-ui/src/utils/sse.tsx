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


import {Event, EventSourcePolyfill, MessageEvent} from "event-source-polyfill";
import {getAccessToken} from "@/utils/token.tsx";

class SSEClient {
    private eventSource: EventSourcePolyfill | null = null;

    /**
     * 创建SSE连接
     * @param url SSE服务地址
     * @param handlers 事件监听器配置
     */
    connect(
        url: string,
        handlers: {
            onMessage: ((this: EventSource, ev: MessageEvent) => void) | null;
            onError?: ((this: EventSource, ev: Event) => void) | null;
            onOpen?: ((this: EventSource, ev: Event) => void) | null;
        },
    ) {
        // 关闭已有连接
        this.disconnect();
        // 初始化配置
        const headers = {Authorization: `Bearer ${getAccessToken()}`};
        this.eventSource = new EventSourcePolyfill(`${import.meta.env.VITE_API_URL ?? '/api'}${url}`, {headers});

        // 绑定事件监听
        this.eventSource.onmessage = handlers.onMessage;
        this.eventSource.onerror = handlers.onError || ((e) => console.error('SSE Error:', e));
        this.eventSource.onopen = handlers.onOpen || ((e) => console.log('SSE Connected', e));
    }

    /** 主动关闭连接 */
    disconnect() {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
    }
}

export const sseClient = new SSEClient();