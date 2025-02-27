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


export interface StsTemporaryTokenRequest {

    /**
     * 前缀名称
     */
    prefix?: string | undefined;

    /**
     * 文件名称集合
     */
    fileNames: string[];

}

export interface StsTemporaryTokenResponse {

    /**
     * ak
     */
    accessKeyId: string;

    /**
     * sk
     */
    secretAccessKey: string;

    /**
     * stsToken
     */
    stsToken: string;

    /**
     * bucketName
     */
    bucketName: string;

    /**
     * key
     */
    objectsMap: Record<string, string>;

    /**
     * endpoint
     */
    endpoint: string;

    /**
     * 区域
     */
    region: string;

    /**
     * 启用路径样式
     */
    enabledPathStyle: boolean;
}


export interface Resource {

    /**
     * id
     */
    id?: string;

    /**
     * 父级id
     */
    parentId?: string;

    /**
     * 业务名称
     */
    businessName?: string | undefined | null;

    /**
     * eTag
     */
    eTag: string;

    /**
     * 名称
     */
    name: string;

    /**
     * 可访问的url
     */
    url?: string;

    /**
     * 大小
     */
    size: number;

    /**
     * 类型
     */
    mimeType: string;

    /**
     * 对象key
     */
    objectKey: string;

    /**
     * version
     */
    version: number;

    /**
     * 是否公开
     */
    isPublic: boolean;

    /**
     * 是否目录
     */
    isDirectory: boolean;
}