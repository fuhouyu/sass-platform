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


import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {DictItem} from "@/model/dictItem.tsx";
import {dictItemApi} from "@/apis/dictItem.tsx";


/**
 * 字典存储
 */
export const dictStore = createSlice({
    name: 'dict',
    reducers: {
        storeDictItem: (state, action: PayloadAction<Record<string, DictItem[]>>) => {
            state.dictTypeItemMapping = {
                ...state.dictTypeItemMapping,
                ...action.payload
            };
            return state;
        },

    },
    initialState: {
        dictTypeItemMapping: {} as Record<string, DictItem[]>,
    }
});

/**
 * 获取字典项映射
 * @param dictCodes 字典编码，以,分隔
 */
const fetchDictItemTypeMapping = (dictCodes: string) => {
    return async (dispatch: (arg0: { payload: Record<string, DictItem[]>; type: `dict/${string}` }) => void) => {
        const dictItemMapping = await dictItemApi.getDictItemTypeMappingList(dictCodes);
        dispatch(dictStore.actions.storeDictItem(dictItemMapping));
    }
}

export {
    fetchDictItemTypeMapping
}

export default dictStore.reducer;