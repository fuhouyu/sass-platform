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

import React, {useCallback, useEffect, useState} from "react"
import {ResourceViewProps} from "./interface"
import {ImageView} from "@components/ResourceView/image/imageView.tsx";
import "./index.scss"
import VideoView from "./video/videoView.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {useTranslation} from "react-i18next";
import {Spin} from "antd";
import {ResourceCategoryEnum} from "@/enums/ResourceCategoryEnum.tsx";
import {SourceCodeView} from "@components/ResourceView/code/sourceCodeView.tsx";
import AudioPlayer from 'react-h5-audio-player';
import 'react-h5-audio-player/lib/styles.css';

export const ResourceView = (resourceView: ResourceViewProps) => {
    const {isPublic, id} = resourceView;
    const {preview, generateSignedUrl} = useResourceAction();
    const {t} = useTranslation();
    const [viewUrl, setViewUrl] = useState<string | undefined>();

    const generateSignedUrlFunc = useCallback(async () => {
        if (isPublic) {
            setViewUrl(preview(resourceView.id));
            return
        }
        const signedUrl = await generateSignedUrl(id, true);
        setViewUrl(signedUrl);
    }, [generateSignedUrl, id, isPublic, preview, resourceView.id]);

    useEffect(() => {
        generateSignedUrlFunc().then();
    }, [generateSignedUrlFunc]);
    if (!viewUrl) {
        return ((
            <div className={'view-loading'}>
                <Spin
                    size="large"
                    percent={"auto"}
                />
            </div>
        ))

    }

    switch (resourceView.category) {
        case ResourceCategoryEnum.IMAGE:
            return <ImageView viewUrl={viewUrl}/>;
        case ResourceCategoryEnum.VIDEO:
            return <VideoView
                key={viewUrl} // 让 React 确认是播放新的视频（只有换源时才换）
                url={viewUrl}
            />
        case ResourceCategoryEnum.SOURCE_CODE:
        case ResourceCategoryEnum.MARKDOWN:
            return <SourceCodeView resourceId={resourceView.id} category={resourceView.category}/>;
        case ResourceCategoryEnum.AUDIO:
            return <AudioPlayer
                src={viewUrl}
                autoPlay/>
        default:
            return <div>{t('Resource.unknownType')}</div>;
    }
}