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

import React, {useCallback, useEffect, useState} from "react";
import SyntaxHighlighter from "react-syntax-highlighter";
import {resourceApi} from "@/apis/resource.tsx";

import {useThemeStore} from "@/store/modules/theme.tsx";
import {oneDark, oneLight} from 'react-syntax-highlighter/dist/esm/styles/prism'; // 暗色主题 // 亮色主题
import {Button, Flex, message, Spin} from "antd";
import {CopyOutlined} from "@ant-design/icons";
import {useTranslation} from "react-i18next";

interface SourceCodeProps {
    // 预览地址
    resourceId: string;
    // 语言
    language: string;
}

export const SourceCodeView = (sourceCodeProps: SourceCodeProps) => {
    const {resourceId, language} = sourceCodeProps;
    const currentTheme = useThemeStore(state => state.theme);
    const {t} = useTranslation();

    const [codeString, setCodeString] = useState<string>('');

    const initCodeString = useCallback(async () => {
        const text = await resourceApi.readFileBytes(resourceId);
        const bytes = Uint8Array.from(atob(text), c => c.charCodeAt(0));
        setCodeString(new TextDecoder('utf-8').decode(bytes));
    }, [resourceId]);

    useEffect(() => {
        initCodeString().then();
    }, [initCodeString]);

    /**
     * 复制
     */
    const handleCopy = async () => {
        await navigator.clipboard.writeText(codeString);
        await message.success(t('Common.copySuccess'));
    };

    return <>
        {codeString ? (
            <div className={'source-code-view-container'}>
                <Button
                    title={t('Common.copy')}
                    icon={<CopyOutlined/>}
                    onClick={handleCopy}
                    className={'copy-button'}
                />
                <SyntaxHighlighter
                    style={currentTheme === 'dark' ? oneDark : oneLight} className={'source-code-view'}>
                    {codeString}
                </SyntaxHighlighter>
            </div>
        ) : (
            <Flex className={'view-loading'} justify={'center'} align={'center'}>
                <Spin size={"large"} percent={"auto"}/>
            </Flex>
        )}
    </>
}