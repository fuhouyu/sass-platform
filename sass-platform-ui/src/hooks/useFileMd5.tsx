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

import {RcFile} from "antd/es/upload";
import SparkMD5 from 'spark-md5';

export function useFileMd5() {

    const calculateMD5 = (file: File | RcFile): Promise<string> => {
        return new Promise((resolve, reject) => {
            const chunkSize = 2097152; // 每次读取2MB
            const chunks = Math.ceil(file.size / chunkSize);
            const spark = new SparkMD5();
            const fileReader = new FileReader();

            let currentChunk = 0;

            fileReader.onload = (e) => {
                const result = e.target?.result;
                if (result === null || result === undefined) {
                    reject('File read result is null or undefined');
                    return;
                }
                spark.append(result);
                currentChunk++;

                if (currentChunk < chunks) {
                    loadNextChunk();
                } else {
                    const md5 = spark.end(); // 计算最终 MD5
                    resolve(md5);
                }
            };

            fileReader.onerror = () => {
                reject('Error reading file');
            };

            const loadNextChunk = () => {
                const start = currentChunk * chunkSize;
                const end = Math.min(start + chunkSize, file.size);
                fileReader.readAsArrayBuffer(file.slice(start, end));
            };

            loadNextChunk();
        });
    }

    return {calculateMD5}
}