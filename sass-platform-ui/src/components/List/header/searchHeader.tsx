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

import {SearchComponentProps} from "./interface";
import {Button} from "antd";
import {SearchOutlined} from "@ant-design/icons";
import './index.scss'
import {useTranslation} from "react-i18next";
import {useState} from "react";

/**
 * 搜索头
 */
const SearchHeader = (searchComponentProps: SearchComponentProps) => {
    const {components, onSearchClick} = searchComponentProps;
    const [buttonLoading, setButtonLoading] = useState<boolean>(false);
    const {t} = useTranslation()

    const [keys] = useState(() =>
        components.map(() => Math.random().toString(36).slice(2))
    );

    return (
        <div className="search-header">
            <div className='search-components'>
                {components?.map((item, index) =>
                    (
                        <div className='search-component' key={keys[index]}>
                            {item}
                        </div>
                    )
                )}

            </div>
            <div className='search-button'>
                {(components?.length ?? 0) > 0 &&
                    <Button type="primary" loading={buttonLoading} icon={<SearchOutlined/>}
                            onClick={() => {
                                if (!onSearchClick) {
                                    return
                                }
                                setButtonLoading(true);
                                onSearchClick();
                                setButtonLoading(false);
                            }}>{t('Button.search')}</Button>
                }
            </div>
        </div>
    );
};

export default SearchHeader;
