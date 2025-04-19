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

/**
 * 文件工具方法合集
 */
export class FileUtils {

    /**
     * 将字节大小转换为人类可读格式
     * @param size 文件大小（单位：字节）
     * @returns 格式化后的字符串（例如："2.34 MB"）
     */
    static formatSize(size: number): string {

        const kb = size / 1024;
        const mb = kb / 1024;
        const gb = mb / 1024;
        const tb = gb / 1024;

        if (tb >= 1) return `${tb.toFixed(2)} TB`;
        if (gb >= 1) return `${gb.toFixed(2)} GB`;
        if (mb >= 1) return `${mb.toFixed(2)} MB`;
        if (kb >= 1) return `${kb.toFixed(2)} KB`;
        return `${size} B`;
    }

    /**
     * 提取文件后缀名
     * @param filename 文件名
     * @returns 后缀名（例如："pdf"），无后缀返回空字符串
     */
    static getFileExtension(filename: string): string {
        if (!filename) return '';
        const parts = filename.split('.');
        return parts.length > 1 ? parts.pop()!.toLowerCase() : '';
    }

    /**
     * 截断过长文件名并保留后缀
     * @param filename 文件名
     * @param maxLength 最大长度，默认 20
     * @returns 截断后的文件名
     */
    static truncateFileName(filename: string, maxLength = 20): string {
        if (!filename || filename.length <= maxLength) return filename;
        const ext = this.getFileExtension(filename);
        const base = filename.substring(0, maxLength - ext.length - 3); // -3 为 "..." 保留
        return `${base}...${ext ? '.' + ext : ''}`;
    }
}
