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

import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.tsx";

export function useResourceAction() {

    const preview = (resourceId?: string): string | undefined => {
        if (resourceId) {
            return `${import.meta.env.VITE_API_URL}${BaseApiUrlConstant.RESOURCE_API_PREFIX}/preview/${resourceId}`
        }
        return undefined;
    }

    const download = (resourceId?: string): string => {
        if (resourceId) {
            return `${import.meta.env.VITE_API_URL}${BaseApiUrlConstant.RESOURCE_API_PREFIX}/download/${resourceId}`
        }
        return "#";
    }

    return {preview, download}
}