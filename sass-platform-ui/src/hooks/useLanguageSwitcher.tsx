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


import {Button, Dropdown, MenuProps} from 'antd';
import {useState} from 'react';

import i18n from 'i18next';
import {IconFont} from '@/components';
import {useLocaleStore} from "@/store";
import {EN_LANGUAGE, ZH_CN_LANGUAGE} from "@/constants/commonConstant.tsx";
import type {ItemType} from "antd/es/menu/interface";

const useLanguageSwitcher = (className?: string) => {
    const {language, changeLanguage} = useLocaleStore(state => state);
    const [currentLanguage, setCurrentLanguage] = useState<string>(language);

    const switchLanguage: MenuProps['onClick'] = async (e: ItemType) => {
        if (!e) {
            return;
        }
        const newLanguage = e.key as string;
        setCurrentLanguage(newLanguage);
        changeLanguage(newLanguage);
        await i18n.changeLanguage(newLanguage);
    };

    const languageItems: MenuProps['items'] = [
        {
            key: ZH_CN_LANGUAGE,
            label: (
                <span>
                    简体中文
                </span>
            )
        },
        {
            key: EN_LANGUAGE,
            label: (
                <span>
                    English
                </span>
            ),
        }
    ]

    const LanguageSwitcherButton = (
        <Dropdown
            placement="bottomLeft"
            menu={{
                items: languageItems,
                onClick: switchLanguage,
                selectedKeys: [currentLanguage]
            }}
            className={className}>
            <Button
                ghost icon={<IconFont
                style={{fontSize: '2rem'}}
                type={'i-fanyi'}/>}/>
        </Dropdown>
    );

    return {language: language, LanguageSwitcherButton};
};

export default useLanguageSwitcher;