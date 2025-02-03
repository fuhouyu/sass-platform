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

import {LANGUAGE_KEY} from "@/constants/commonConstant";
import {create} from "zustand/react";
import {StateCreator} from "zustand";

/**
 * 状态
 */
interface LocaleState {
    /**
     * 语言
     */
    language: string;
}


interface LocaleAction {
    /**
     * 改变语言
     * @param language 语言类型
     */
    changeLanguage: (language: string) => void;

}

/**
 * 创建语言切片
 * @param set set
 */
const createLocaleSlice: StateCreator<LocaleState & LocaleAction> = (set) => ({
    language: localStorage.getItem(LANGUAGE_KEY) ?? 'zh',
    changeLanguage: (language: string) => set(({language})),
});


export const useLocaleStore = create<LocaleState & LocaleAction>((...a) => ({
    ...createLocaleSlice(...a)
}));