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

import {useState} from "react";
import {resourceApi} from "@/apis/resource.tsx";
import SyntaxHighlighter from 'react-syntax-highlighter';
import {vs, vscDarkPlus} from "react-syntax-highlighter/dist/esm/styles/prism";
import {useThemeStore} from "@/store/modules/theme.tsx";


export const YamlView = ({resourceId}: { resourceId: string }) => {
    const [yamlContent, setYamlContent] = useState<string>('');
    const {theme} = useThemeStore();
    const loadYaml = async () => {
        // 替换为你的后端 API 地址
        const response = await resourceApi.previewFile(resourceId);
        const text = await response.text();
        setYamlContent(text);
    }
    loadYaml().then()
    return <div className={'view-container'}>
        <SyntaxHighlighter language="yaml" style={theme === 'dark' ? vscDarkPlus : vs}>
            {yamlContent}
        </SyntaxHighlighter>
    </div>
}