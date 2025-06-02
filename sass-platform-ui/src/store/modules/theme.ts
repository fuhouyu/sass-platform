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


import {StateCreator} from "zustand";
import {CommonConstant} from "@/constants/commonConstant.ts";
import {create} from "zustand/react";

interface ThemeState {
    theme: string;
}


/**
 * theme action
 */
interface ThemeAction {
    changeTheme: (theme: string) => void
}

const getTheme = (): string => {
    const currentTheme = localStorage.getItem(CommonConstant.THEME_KEY) ?? (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    if (currentTheme === 'dark') {
        document.body.classList.add('dark');
    } else {
        document.body.classList.remove('dark');
    }
    return currentTheme
}


/**
 * slice
 * @param set set
 */
const createThemeSlice: StateCreator<ThemeState & ThemeAction> = (set) => ({
    theme: getTheme(),
    changeTheme: theme => {
        if (theme === 'dark') {
            document.body.classList.add('dark');
        } else {
            document.body.classList.remove('dark');
        }
        localStorage.setItem(CommonConstant.THEME_KEY, theme);
        set({theme})
    }
});

/**
 * use store
 */
export const useThemeStore = create<ThemeState & ThemeAction>((...a) => ({
    ...createThemeSlice(...a)
}));
