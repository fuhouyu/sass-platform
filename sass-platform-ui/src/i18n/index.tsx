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

import i18n from "i18next";
import {initReactI18next} from "react-i18next";
import {ZhCN} from "@/i18n/zh-CN";
import {LANGUAGE_KEY} from "@/constants/commonConstant";
import {EnUS} from "@/i18n/en-US";

i18n
    // 将 i18n 实例传递给 react-i18next
    .use(initReactI18next)
    // 初始化 i18next
    // 所有配置选项: https://www.i18next.com/overview/configuration-options
    .init({
        resources: {
            "en": {
                translation: EnUS
            },
            "zh": {
                translation: ZhCN
            },
        },
        fallbackLng: "zh",
        lng: localStorage.getItem(LANGUAGE_KEY) ?? 'zh',
        debug: true,
        interpolation: {
            escapeValue: false, // not needed for react as it escapes by default
        }
    }).then();
export default i18n;