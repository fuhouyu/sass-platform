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

import {FC, ReactNode, useState} from "react";
import {Upload as AntdUpload} from "antd";
import {resourceApi} from "@/apis/resource.ts";
import {S3UploadProps} from "@components/Upload/interface";
import {RcFile} from "antd/es/upload";
import {IStsTemporaryTokenResponse} from "@/types/resource";
import {useUploadStore} from "@/store/modules/upload.ts";
import {Progress} from "@aws-sdk/lib-storage/dist-types/types";
import {Trans, useTranslation} from "react-i18next";
import {NotificationType, useNotification} from "@/hooks/useNotification.tsx";
import type {S3Client} from "@aws-sdk/client-s3";


const loadAwsSdk = async () => {
    const [{S3Client, ChecksumAlgorithm}, {Upload}] = await Promise.all([
        import("@aws-sdk/client-s3"),
        import("@aws-sdk/lib-storage"),
    ]);
    return {S3Client, Upload, ChecksumAlgorithm};
};
export const S3Upload: FC<S3UploadProps> = (uploadProps) => {
    const {prefix, isPublic, children, showUploadList, onUploadSuccess} = uploadProps;
    const {t} = useTranslation();
    const storeUploadFiles = useUploadStore(state => state.storeUploadFiles);
  const [totalProgress, setTotalProgress] = useState(0);
    const {notificationMessage, contextHolder} = useNotification();

  const uploadNotification = (type: NotificationType, message: ReactNode) => {
        notificationMessage({
            type: type,
            message: t('Resource.upload.file'),
            description: message,
        });
    };


    /**
     * 生成sts的Token
     */
    const generateStsToken = async (uploadFile: RcFile) => {
        const {S3Client} = await loadAwsSdk();
        const stsTokenResponse = await resourceApi.generateStsToken({
            prefix: prefix,
            fileNames: [uploadFile.webkitRelativePath === '' ? uploadFile.name : uploadFile.webkitRelativePath]
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
    const doFileUpload = async (s3Client: S3Client, stsTokenResponse: IStsTemporaryTokenResponse, file: RcFile) => {
        const {Upload, ChecksumAlgorithm} = await loadAwsSdk();

        const objectKey = stsTokenResponse.objectsMap[file.webkitRelativePath === '' ? file.name : file.webkitRelativePath];
        const abortController = new AbortController();
        if (showUploadList) {
            storeUploadFiles({
                id: file.uid,
                name: file.name,
                type: file.type,
                size: file.size,
                progress: 0,
                abortController,
                status: 'pending'
            });
        }
        const upload = new Upload({
            client: s3Client,
            params: {
                Bucket: stsTokenResponse.bucketName,
                Key: objectKey,
                Body: file,
                ContentType: file.type,
                ChecksumAlgorithm: ChecksumAlgorithm.CRC32,
            },
        });

        upload['abortController'] = abortController;
        upload.on("httpUploadProgress", (progress: Progress) => {
            const {loaded, total} = progress;
            let percentage = 100;
            if (loaded && total) {
                percentage = Math.round((loaded / total) * 100);
            }
            setTotalProgress(percentage);
            const status = percentage === 100 ? 'success' : 'uploading';
            if (showUploadList) {
                storeUploadFiles({
                    id: file.uid,
                    name: file.name,
                    type: file.type,
                    size: file.size,
                    progress: percentage,
                    abortController,
                    status: status
                });
            }
        });


        try {
            const response = await upload.done();
            const resourceId = await resourceApi.saveInfoApi({
                businessName: prefix,
                eTag: JSON.parse(response.ETag!),
                name: file.name,
                size: file.size,
                mimeType: file.type,
                isPublic: isPublic,
                isDirectory: false,
                objectKey: objectKey,
            });
            onUploadSuccess?.(resourceId);
            uploadNotification('success', <Trans
                i18nKey={t('Resource.upload.successTips')}
                values={{name: file.name}}
                components={{strong: <span className="highlight"/>}}
            />);
        } catch (err: unknown) {
            console.log(err)
            if (!(err instanceof Error)) {
                return
            }
            if (err.name === 'AbortError') {
                uploadNotification('warning', <Trans
                    i18nKey={t('Resource.upload.canceledTips')}
                    values={{name: file.name}}
                    components={{strong: <span className="highlight"/>}}
                />);
                storeUploadFiles({
                    id: file.uid,
                    name: file.name,
                    type: file.type,
                    size: file.size,
                    abortController,
                    status: 'canceled'
                });
            } else {
                storeUploadFiles({
                    id: file.uid,
                    name: file.name,
                    type: file.type,
                    size: file.size,
                    status: 'error',
                    errorMessage: err.message,
                });
                uploadNotification('error', <Trans
                    i18nKey={t('Resource.upload.errorTips')}
                    values={{name: file.name}}
                    components={{strong: <span className="highlight"/>}}
                />);

            }
        }


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
                        console.log(e)
                        onError?.(e);
                        storeUploadFiles({
                            id: rcFile.uid,
                            name: rcFile.name,
                            type: rcFile.type,
                            size: rcFile.size,
                            status: 'error',
                            errorMessage: (e as Error).message
                        });
                        uploadNotification('error', <Trans
                            i18nKey={t('Resource.upload.error')}
                            values={{name: rcFile.name}}
                            components={{strong: <span className="highlight"/>}}
                        />);

                    });
                }}
            >
                {children === undefined ? <span>{t('Resource.upload.file')}</span> : children}
            </AntdUpload>
        </>

    )

}
