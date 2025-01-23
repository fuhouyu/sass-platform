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


import {Spin} from "antd";
import React from "react";
import './index.scss'

export const PageLoading = () => {
    const contentStyle: React.CSSProperties = {
        background: 'rgba(0, 0, 0, 0.05)',
        borderRadius: 10,
    };

    const content = <div style={contentStyle}/>;
    return (
        <Spin delay={1000} tip="页面加载中..." fullscreen={true} size="large" className="page-loading">
            {content}
        </Spin>
    )
}