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
import {LANGUAGE_KEY} from "@/constants/commonConstant";

const localeStore = createSlice({
    name: 'locale',
    initialState: {
        language: localStorage.getItem(LANGUAGE_KEY) ?? 'zh',

    },
    reducers: {
        changeLanguage: (state, action: PayloadAction<string>) => {
            state.language = action.payload;

            return state;
        }
    }
})

/**
 * 切换语言
 * @param language 语言类型
 */
const changeLanguage = (language: string) => {
    return (dispatch: (arg0: { payload: string; type: `locale/${string}` }) => void) => {
        localStorage.setItem(LANGUAGE_KEY, language);
        dispatch(localeStore.actions.changeLanguage(language))
    }
};

export {
    changeLanguage
}
export default localeStore.reducer;