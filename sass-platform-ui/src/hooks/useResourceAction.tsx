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
import {resourceApi} from "@/apis/resource.tsx";
import {useCallback} from "react";
import {request} from "@/utils";

export function useResourceAction() {

    const uri = request.getUri();

    const preview = useCallback((resourceId?: string): string | undefined => {
        if (resourceId) {
            return `${uri}${BaseApiUrlConstant.RESOURCE_API_PREFIX}/${resourceId}/download?preview=true`
        }
        return undefined;
    }, [uri]);

    const generateSignedUrl = useCallback(async (resourceId?: string,
                                                 preview?: boolean): Promise<string> => {
        if (resourceId) {
            return await resourceApi.generateSignedUrl(resourceId, preview);
        }
        return "#";
    }, [])


    return {preview, generateSignedUrl}
}