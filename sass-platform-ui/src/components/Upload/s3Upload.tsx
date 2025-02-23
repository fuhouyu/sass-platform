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
import {message, Upload as AntdUpload} from "antd";
import {UploadRequestOption} from "rc-upload/lib/interface";
import {ChecksumAlgorithm, S3Client} from "@aws-sdk/client-s3";
import {Upload as s3Upload} from "@aws-sdk/lib-storage";
import {resourceApi} from "@/apis/resource.tsx";
import {S3UploadProps} from "@components/Upload/interface.tsx";
import {useFileMd5} from "@/hooks/useFileMd5.tsx";

export const S3Upload: React.FC<{
    uploadProps: S3UploadProps,
    children: React.ReactNode
}> = ({uploadProps, children}: { uploadProps: S3UploadProps, children: React.ReactNode }) => {

    const {calculateMD5} = useFileMd5();
    const uploadFile = async (options: UploadRequestOption) => {

        try {
            const {file} = options;

            let contentType = 'application/octet-stream';
            let fileName = '';
            let fileSize = 0;
            if (file instanceof File) {
                contentType = file.type;
                fileName = file.name;
                fileSize = file.size;
                const startTime = Date.now(); // 记录开始时间
                const md5 = await calculateMD5(file);
                const endTime = Date.now(); // 记录结束时间
                const totalTime = (endTime - startTime) / 1000; // 计算总耗时
                console.log('File MD5:', md5);
                console.log('Total time taken:', totalTime, 's');

                const existsResource = await resourceApi.getResourceByEtag(md5);
                if (existsResource) {
                    uploadProps.onUploadSuccess(existsResource.id!);
                    return;
                }
            }

            const stsTokenResponse = await resourceApi.generateStsToken();
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
            const upload = new s3Upload({
                client: s3Client,
                params: {
                    Bucket: stsTokenResponse.bucketName,
                    Key: stsTokenResponse.objectKey,
                    Body: file,
                    ContentType: contentType,
                    ChecksumAlgorithm: ChecksumAlgorithm.CRC32,
                },
            });
            // 监听上传进度
            // upload.on("httpUploadProgress", (progress) => {
            //     const {loaded, total} = progress;
            //     if (loaded && total) {
            //         const percentage = Math.round((loaded / total) * 100);
            //         setProgress(percentage);
            //     } else {
            //         setProgress(100);
            //     }
            // });
            const response = await upload.done();
            const resourceId = await resourceApi.saveInfoApi({
                businessName: uploadProps.businessName,
                eTag: JSON.parse(response.ETag!),
                name: fileName,
                size: fileSize,
                mimeType: contentType,
                isPublic: true,
                version: 1,
                objectKey: stsTokenResponse.objectKey,
            });
            uploadProps.onUploadSuccess(resourceId);
        } catch (e) {
            console.log(e);
            message.error('文件上传失败');
        }

    }
    return (
        <AntdUpload
            {...uploadProps}
            className={'avatar-uploader'}
            customRequest={uploadFile}
            showUploadList={false}
        >
            {children}
        </AntdUpload>

    )

}