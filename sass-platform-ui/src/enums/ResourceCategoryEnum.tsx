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

import {IconFont} from "@/components";
import {ReactNode} from "react";

/**
 * 分类配置
 */
interface ResourceCategory {
    typeName: string;
    icon: ReactNode;
}


/**
 * 资源类型枚举
 */
export enum ResourceCategoryEnum {
    // 文档类型
    DOCUMENT = 'DOCUMENT',
    // 图片类型
    IMAGE = 'IMAGE',
    // 视频类型
    VIDEO = 'VIDEO',
    // 音频类型
    AUDIO = 'AUDIO',
    // 压缩包类型
    ARCHIVE = 'ARCHIVE',
    // 其他类型
    OTHER = 'OTHER',
    // 源代码
    SOURCE_CODE = 'SOURCE_CODE',
}

/**
 * 资源类型分类配置
 */
const resourceTypeInfo: Record<ResourceCategoryEnum, ResourceCategory> = {
    [ResourceCategoryEnum.DOCUMENT]: {
        typeName: '文档',
        icon: <IconFont type={'i-Document'}/>,
    },
    [ResourceCategoryEnum.IMAGE]: {
        typeName: '图片',
        icon: <IconFont type={'i-tupian'}/>,
    },
    [ResourceCategoryEnum.ARCHIVE]: {
        typeName: '压缩包',
        icon: <IconFont type={'i-zip'}/>,
    },

    [ResourceCategoryEnum.VIDEO]: {
        typeName: '视频',
        icon: <IconFont type={'i-video'}/>,
    },
    [ResourceCategoryEnum.SOURCE_CODE]: {
        typeName: '代码',
        icon: <IconFont type={'i-hc-code'}/>,
    },
    [ResourceCategoryEnum.AUDIO]: {
        typeName: '音频',
        icon: <IconFont type={'i-audio'}/>,
    },
    [ResourceCategoryEnum.OTHER]: {
        typeName: '其他',
        icon: <IconFont type={'i-weizhi'}/>,
    },
};

/**
 * 获取资源分类
 * @param category 分类
 */
export function getCategoryInfo(category: string): ResourceCategory {
    const fileType = ResourceCategoryEnum[category as keyof typeof ResourceCategoryEnum];
    if (fileType) {
        return resourceTypeInfo[fileType];
    }
    return resourceTypeInfo[ResourceCategoryEnum.OTHER]
}

