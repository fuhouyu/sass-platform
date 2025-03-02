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

import {ReactNode} from "react";
import {IconFont} from "@/components";

export enum ResourceTypeEnum {
    UNKNOWN = "unknown",
    TEXT = "text",
    IMAGE = "image",
    VIDEO = "video",
    AUDIO = "audio",
    OFFICE = "office",
    APPLICATION = "application",
}

export interface ResourceType {
    type: ResourceTypeEnum;
    icon: ReactNode;
}


const useResourceType = () => {

    /**
     * 获取资源类型
     * @param mimeType mimeType
     */
    const parseResourceType = (mimeType: string): ResourceType => {
        if (!mimeType) return {
            type: ResourceTypeEnum.UNKNOWN,
            icon: <IconFont type={'i-weizhi'}/>,

        };
        // 提取 MIME 类型的第一部分（type）
        const [type, subtype] = mimeType.split("/");

        // 根据 type 和 subtype 判断资源的大类型
        switch (type) {
            case "image":
                return {
                    type: ResourceTypeEnum.IMAGE,
                    icon: <IconFont type={'i-tupian'}/>,
                };
            case "audio":
                return {
                    type: ResourceTypeEnum.AUDIO,
                    icon: <IconFont type={'i-audio'}/>,
                };
            case "video":
                return {
                    type: ResourceTypeEnum.VIDEO,
                    icon: <IconFont type={'i-video'}/>,
                };
            case "application":
                // 处理常见的 office 文档类型
                switch (subtype) {
                    case "vnd.openxmlformats-officedocument.presentationml.presentation":
                        return {
                            type: ResourceTypeEnum.OFFICE,
                            icon: <IconFont type={'i-excel'}/>,
                        }
                    case "msword":
                        return {
                            type: ResourceTypeEnum.OFFICE,
                            icon: <IconFont type={'i-word'}/>,
                        }
                    case "excel":
                        return {
                            type: ResourceTypeEnum.OFFICE,
                            icon: <IconFont type={'i-excel'}/>,
                        }
                    case "powerpoint":
                        return {
                            type: ResourceTypeEnum.OFFICE,
                            icon: <IconFont type={'i-ppt'}/>,
                        }
                    case "pdf":
                        return {
                            type: ResourceTypeEnum.OFFICE,
                            icon: <IconFont type={'i-pdf'}/>,
                        }
                    default:
                        return {
                            type: ResourceTypeEnum.UNKNOWN,
                            icon: <IconFont type={'i-weizhi'}/>
                        }
                }
            case "text":
                return {
                    type: ResourceTypeEnum.TEXT,
                    icon: <IconFont type={'i-filestext'}/>
                }
            default:
                return {
                    type: ResourceTypeEnum.UNKNOWN,
                    icon: <IconFont type={'i-weizhi'}/>
                }
        }
    };

    return {
        parseResourceType,
    }
};

export default useResourceType;