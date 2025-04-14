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
import {notification} from "antd";
import {ArgsProps} from "antd/es/notification/interface";

export type NotificationType = 'success' | 'info' | 'warning' | 'error';

export interface NotificationProps extends ArgsProps {
    type: NotificationType;
}

/**
 * 自定义通过
 */
export function useNotification() {
    const [api, contextHolder] = notification.useNotification();
    const notificationMessage = (props: NotificationProps) => {
        api[props.type]({
            ...props
        });
    }

    return {
        contextHolder,
        notificationMessage
    }
}