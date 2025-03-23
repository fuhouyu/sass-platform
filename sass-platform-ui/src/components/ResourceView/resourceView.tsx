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

import React, {useEffect, useState} from "react"
import {ResourceViewProps} from "./interface"
import {OfficeView} from "@components/ResourceView/officeView.tsx";
import {ResourceTypeEnum} from "@/hooks/useResourceType.tsx";
import {ImageView} from "@components/ResourceView/imageView.tsx";
import "./index.scss"
import VideoView from "./videoView";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {useTranslation} from "react-i18next";
import {YamlView} from "@components/ResourceView/yamlView.tsx";
import {Flex, Spin} from "antd";


export const ResourceView = (resourceView: ResourceViewProps) => {
    const {preview, generateSignedUrl} = useResourceAction();
    const {t} = useTranslation();
    const [viewUrl, setViewUrl] = useState<string | undefined>(undefined);

    useEffect(() => {
        const fetchUrl = async () => {
            if (resourceView.isPublic) {
                setViewUrl(preview(resourceView.id)); // 直接赋值
            } else {
                const signedUrl = await generateSignedUrl(resourceView.id, true); // 等待异步请求
                setViewUrl(signedUrl);
            }
        };

        fetchUrl().then();
    }, [resourceView]);
    if (!viewUrl) {
        return <Flex className={'view-loading'} justify={'center'} align={'center'}>
            <Spin size={"large"} percent={"auto"}/>
        </Flex>
    }

    switch (resourceView.type) {
        case ResourceTypeEnum.OFFICE:
            return <OfficeView {...resourceView}/>;
        case ResourceTypeEnum.IMAGE:
            return <ImageView viewUrl={viewUrl}/>;
        case ResourceTypeEnum.VIDEO:
            return <VideoView options={{
                autoplay: true,
                controls: true,
                sources: [
                    {
                        src: viewUrl,
                        type: 'video/mp4'
                    }
                ]
            }}/>;
        case ResourceTypeEnum.YAML:
            return <YamlView resourceId={resourceView.id}/>;
        default:
            return <div>{t('Resource.unknownType')}</div>;
    }
}