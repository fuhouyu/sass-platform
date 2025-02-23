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


import {UploadProps} from "antd/lib";

/**
 * S3UploadProps
 */
export interface S3UploadProps extends UploadProps {
    /**
     * 业务名称
     */
    businessName: string;

    /**
     * 是否是公共访问资源
     */
    isPublic: boolean;

    /**
     * 上传成功回调
     * @param resourceId 资源id
     */
    onUploadSuccess: (resourceId: string) => void;
}