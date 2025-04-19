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

import {create, StateCreator} from "zustand";

export interface UploadFile {

    /**
     * 文件id
     */
    id: string;

    /**
     * 文件名称
     */
    name: string;

    /**
     * 文件类型
     */
    type: string;

    /**
     * 文件总大小
     */
    size: number;

    /**
     * 上传进度
     */
    progress?: number;

    /**
     * 取消上传
     * @param fileUid
     */
    abortController?: AbortController;

    /**
     * 上传状态
     */
    status: 'pending' | 'uploading' | 'canceled' | 'success' | 'error';

    /**
     * 上传错误信息
     */
    errorMessage?: string;

}

interface UploadState {
    /**
     * 上传的文件
     */
    uploadFiles: UploadFile[]
}

interface UploadAction {

    /**
     * 存储文件上传状态
     * @param uploadFile 上传的文件
     */
    storeUploadFiles: (uploadFile: UploadFile) => void,

    /**
     * 通过id删除文件
     * @param id 文件id
     */
    removeUploadFile: (id: string) => void
}


const createUploadSlice: StateCreator<UploadState & UploadAction> = (set) => ({

    /**
     * 待上传的文件数组
     */
    uploadFiles: [],

    /**
     * 存储文件的上传状态
     * @param uploadFile 上传的文件
     */
    storeUploadFiles: uploadFile => {
        set((state) => {
            const fileExists = state.uploadFiles.some((file) => file.id === uploadFile.id);
            if (fileExists) {
                return {
                    uploadFiles: state.uploadFiles.map((file) =>
                        file.id === uploadFile.id
                            ? {...uploadFile}
                            : file
                    ),
                };
            }
            return {uploadFiles: [...state.uploadFiles, uploadFile]};
        });
    },
    removeUploadFile: id => {
        set((state) => ({
            uploadFiles: state.uploadFiles.filter((file) => file.id !== id),
        }));
    }
});


export const useUploadStore = create<UploadState & UploadAction>((...a) => ({
    ...createUploadSlice(...a),
}));