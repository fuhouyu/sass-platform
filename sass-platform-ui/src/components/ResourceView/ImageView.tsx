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

import {ResourceViewProps} from "@components/ResourceView/interface.tsx";
import {useResourcePreview} from "@/hooks/useResourcePreview.tsx";

export const ImageView = (resourceProps: ResourceViewProps) => {
    const {previewUrl} = useResourcePreview();
    return (<img
        width={'100%'}
        className={'resource-preview'}
        src={previewUrl(resourceProps.id)}
        alt="Preview Image"
    />)
}