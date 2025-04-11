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

import React from "react";
import {notification, Upload as AntdUpload} from "antd";
import {ChecksumAlgorithm, S3Client} from "@aws-sdk/client-s3";
import {Upload as s3Upload} from "@aws-sdk/lib-storage";
import {resourceApi} from "@/apis/resource.tsx";
import {S3UploadProps} from "@components/Upload/interface.tsx";
import {RcFile} from "antd/es/upload";
import {StsTemporaryTokenResponse} from "@/model/resource.tsx";
import {useUploadStore} from "@/store/modules/upload.tsx";
import {Progress} from "@aws-sdk/lib-storage/dist-types/types";
import {useTranslation} from "react-i18next";

type NotificationType = 'success' | 'info' | 'warning' | 'error';


export const S3Upload: React.FC<S3UploadProps> = (uploadProps) => {
    const {prefix, isPublic, children, showUploadFloatButton, onUploadSuccess} = uploadProps;
    const {t} = useTranslation();
    const storeUploadFiles = useUploadStore(state => state.storeUploadFiles);
    const [totalProgress, setTotalProgress] = React.useState(0);
    const [api, contextHolder] = notification.useNotification();

    const uploadNotification = (type: NotificationType, message: string) => {
        api[type]({
            message: t('Resource.uploadFile'),
            description: message,
        });
    };


    /**
     * 生成sts的Token
     */
    const generateStsToken = async (uploadFile: RcFile) => {
        const stsTokenResponse = await resourceApi.generateStsToken({
            prefix: prefix,
            fileNames: [uploadFile.webkitRelativePath]
        });
        const s3Client = new S3Client({
            region: stsTokenResponse.region,
            endpoint: stsTokenResponse.endpoint,
            forcePathStyle: stsTokenResponse.enabledPathStyle,
            credentials: {
                accessKeyId: stsTokenResponse.accessKeyId,
                secretAccessKey: stsTokenResponse.secretAccessKey,
                sessionToken: stsTokenResponse.stsToken,
            },
        });
        return {stsTokenResponse, s3Client};
    }

    /**
     * 文件上传
     * @param s3Client s3Client
     * @param stsTokenResponse stsToken响应
     * @param file 需要上传的文件
     */
    const doFileUpload = async (s3Client: S3Client, stsTokenResponse: StsTemporaryTokenResponse, file: RcFile) => {

        const objectKey = stsTokenResponse.objectsMap[file.webkitRelativePath];
        if (showUploadFloatButton) {
            storeUploadFiles({
                id: file.uid,
                name: file.name,
                type: file.type,
                size: file.size,
                progress: 0
            });
        }
        const upload = new s3Upload({
            client: s3Client,
            params: {
                Bucket: stsTokenResponse.bucketName,
                Key: objectKey,
                Body: file,
                ContentType: file.type,
                ChecksumAlgorithm: ChecksumAlgorithm.CRC32,
            },
        });

        upload.on("httpUploadProgress", (progress: Progress) => {
            const {loaded, total} = progress;
            let percentage = 100;
            if (loaded && total) {
                percentage = Math.round((loaded / total) * 100);
            }
            setTotalProgress(percentage);
            if (showUploadFloatButton) {
                storeUploadFiles({
                    id: file.uid,
                    name: file.name,
                    type: file.type,
                    size: file.size,
                    progress: percentage
                });
            }
        });


        const response = await upload.done();
        const resourceId = await resourceApi.saveInfoApi({
            businessName: prefix,
            eTag: JSON.parse(response.ETag!),
            name: file.name,
            size: file.size,
            mimeType: file.type,
            isPublic: isPublic,
            isDirectory: false,
            version: 1,
            objectKey: objectKey,
        });

        onUploadSuccess?.(resourceId);
        const success = `${file.name} ${t('Resource.uploadSuccess')}`;
        uploadNotification('success', success);
    }

    return (
        <>
            {contextHolder}
            <AntdUpload
                {...uploadProps}
                customRequest={async (options) => {
                    const {file, onSuccess, onProgress, onError} = options;
                    // 验证通过，进行上传
                    const rcFile = file as RcFile;
                    onProgress?.({percent: totalProgress})
                    generateStsToken(rcFile).then(async ({stsTokenResponse, s3Client}) => {
                        await doFileUpload(s3Client, stsTokenResponse, rcFile);
                        onSuccess?.({}, rcFile);
                    }).catch((e) => {
                        console.log(e);
                        onError?.(e, rcFile);
                        uploadNotification('error', `${rcFile.name} ${t('Resource.uploadError')}`);
                    });
                }}
            >
                {children === undefined ? <span>{t('Resource.uploadFile')}</span> : children}
            </AntdUpload>
        </>

    )

}