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


import {Button} from 'antd';
import {useState} from 'react';
import {useAppDispatch, useAppSelector} from '@/store';
import {changeLanguage} from '@/store/modules/locale';
import i18n from 'i18next';
import {IconFont} from '@/components';

const useLanguageSwitcher = (className?: string) => {
    const dispatch = useAppDispatch();
    const currentLanguage = useAppSelector(state => state.locale.language);
    const [language, setLanguage] = useState<string>(currentLanguage);

    const switchLanguage = async () => {
        const newLanguage = language === 'zh' ? 'en' : 'zh';
        setLanguage(newLanguage);
        dispatch(changeLanguage(newLanguage));
        await i18n.changeLanguage(newLanguage);
    };

    const LanguageSwitcherButton = (
        <Button
            className={className}
            onClick={switchLanguage}
            icon={<IconFont type={language === 'zh' ? 'i-yingwen-shuangse' : 'i-zhongwen-shuangse'}/>}
        />
    );

    return {language, switchLanguage, LanguageSwitcherButton};
};

export default useLanguageSwitcher;