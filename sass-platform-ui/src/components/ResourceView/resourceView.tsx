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

import React from "react"
import {ResourceViewProps} from "./interface"
import {OfficeView} from "@components/ResourceView/officeView.tsx";
import {ResourceTypeEnum} from "@/hooks/useResourceType.tsx";
import {ImageView} from "@components/ResourceView/imageView.tsx";
import "./index.scss"
import VideoView from "./videoView";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {useTranslation} from "react-i18next";


export const ResourceView = (resourceView: ResourceViewProps) => {
    const {preview} = useResourceAction();
    const {t} = useTranslation();
    const viewUrl = preview(resourceView.id);
    switch (resourceView.type) {
        case ResourceTypeEnum.OFFICE:
            return <OfficeView {...resourceView}/>;
        case ResourceTypeEnum.IMAGE:
            return <ImageView viewUrl={viewUrl}/>
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
            }}/>
        default:
            return <div>{t('Resource.unknownType')}</div>
    }
}