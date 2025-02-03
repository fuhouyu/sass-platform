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


import {DictItem} from "@/model/dictItem.tsx";
import {dictItemApi} from "@/apis/dictItem.tsx";
import {create} from "zustand/react";
import {StateCreator} from "zustand";

/**
 * 字典状态
 */
interface DictState {
    /**
     * 字典类型item映射
     */
    dictTypeItemMapping: Record<string, DictItem[]>;
}

/**
 * action
 */
interface DictAction {
    /**
     * 字典类型item映射
     * @param dictCodes 字典在编码
     */
    fetchDictItemTypeMapping: (dictCodes: string) => Promise<void>
}

/**
 * 创建字典状态
 */
const createDictSlice: StateCreator<DictState & DictAction> = (set) => ({

    dictTypeItemMapping: {},

    fetchDictItemTypeMapping: async (dictCodes: string) => {
        const dictItemMapping = await dictItemApi.getDictItemTypeMappingList(dictCodes);
        set({dictTypeItemMapping: dictItemMapping})
    }
});


export const useDictStore = create<DictState & DictAction>()((...a) => ({
    ...createDictSlice(...a)
}));