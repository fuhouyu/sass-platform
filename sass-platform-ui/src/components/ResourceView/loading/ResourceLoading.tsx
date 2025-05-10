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


import './index.scss'
import {useTranslation} from "react-i18next";
import {Space} from "antd";
import React from "react";

export const ResourceLoading = ({title}: { title?: string | undefined }) => {
    const {t} = useTranslation();
    return (
        <div className={'loading-container'}>
            <Space align={'center'} direction={'vertical'}>
                <div>
                    <div className="resource-loader"></div>
                </div>
                <div className="loading-span">{title ?? t('Common.resourceLoading')}</div>
            </Space>
        </div>
    )
}