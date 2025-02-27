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

import React, {useCallback, useEffect} from "react";
import {message, Upload as AntdUpload} from "antd";
import {ChecksumAlgorithm, S3Client} from "@aws-sdk/client-s3";
import {Upload as s3Upload} from "@aws-sdk/lib-storage";
import {resourceApi} from "@/apis/resource.tsx";
import {S3UploadProps} from "@components/Upload/interface.tsx";
import {RcFile} from "antd/es/upload";
import {StsTemporaryTokenResponse} from "@/model/resource.tsx";

export const S3Upload: React.FC<{
    uploadProps: S3UploadProps,
    children: React.ReactNode
}> = ({uploadProps, children}: { uploadProps: S3UploadProps, children: React.ReactNode }) => {

    const [uploadFiles, setUploadFiles] = React.useState<RcFile[]>([]);

    /**
     * 生成sts的Token
     */
    const generateStsToken = useCallback(async () => {
        const stsTokenResponse = await resourceApi.generateStsToken({
            prefix: uploadProps.prefix,
            fileNames: uploadFiles.map(file => file.webkitRelativePath),
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
    }, [uploadFiles, uploadProps.prefix])

    /**
     * 文件上传
     * @param s3Client s3Client
     * @param stsTokenResponse stsToken响应
     * @param file 需要上传的文件
     */
    const doFileUpload = useCallback(async (s3Client: S3Client, stsTokenResponse: StsTemporaryTokenResponse, file: RcFile) => {

        const objectKey = stsTokenResponse.objectsMap[file.webkitRelativePath];
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
        const response = await upload.done();
        const resourceId = await resourceApi.saveInfoApi({
            businessName: uploadProps.prefix,
            eTag: JSON.parse(response.ETag!),
            name: file.name,
            size: file.size,
            mimeType: file.type,
            isPublic: true,
            isDirectory: false,
            version: 1,
            objectKey: objectKey,
        });
        uploadProps.onUploadSuccess?.(resourceId);
        message.success(`${file.name} 上传成功`);
    }, [uploadProps])

    const fileUploadHandle = useCallback(async () => {
        try {
            // 批量上传
            const {stsTokenResponse, s3Client} = await generateStsToken();
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
            uploadFiles.forEach((uploadFile) => doFileUpload(s3Client, stsTokenResponse, uploadFile))
        } catch (e) {
            console.log(e);
            message.error('文件上传失败');
        } finally {
            setUploadFiles([]);
        }
    }, [doFileUpload, generateStsToken, uploadFiles])


    useEffect(() => {
        if (uploadFiles.length === 0) {
            return;
        }
        // 上传文件
        fileUploadHandle().then();
        return () => setUploadFiles([]);
    }, [fileUploadHandle, uploadFiles])

    /**
     * 设置文件处理，返回false 表示不使用默认上传行为
     * @param file 文件
     * @param fileList 文件集合
     */
    const fileHandle = (file: RcFile, fileList: RcFile[]) => {
        const beforeUpload = uploadProps.beforeUpload;
        let isUpload = true;
        if (beforeUpload) {
            isUpload = beforeUpload(file, fileList) as boolean;
        }
        if (isUpload) {
            if (uploadProps.directory) {
                setUploadFiles(fileList);
            } else {
                setUploadFiles([file]);
            }
        }
        return false;
    }
    return (
        <AntdUpload
            {...uploadProps}
            beforeUpload={fileHandle}
            className={'avatar-uploader'}
            showUploadList={false}
        >
            {children}
        </AntdUpload>

    )

}