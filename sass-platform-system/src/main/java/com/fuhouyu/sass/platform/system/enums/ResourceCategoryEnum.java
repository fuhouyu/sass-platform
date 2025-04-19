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
package com.fuhouyu.sass.platform.system.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 资源分类枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/17 11:34
 */
@Getter
public enum ResourceCategoryEnum {

    /**
     * office
     */
    DOCUMENT("application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/pdf",
            "application/vnd.oasis.opendocument.text",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.oasis.opendocument.spreadsheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.oasis.opendocument.presentation"),

    /**
     * 音频
     */
    AUDIO("audio/mpeg",
            "audio/ogg",
            "audio/wav",
            "audio/aac",
            "audio/x-ms-wma"),

    /**
     * 视频
     */
    VIDEO("video/mp4",
            "video/mpeg",
            "video/quicktime",
            "video/x-msvideo",
            "video/webm"),

    /**
     * 图片
     */
    IMAGE("image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"),

    /**
     * 代码
     */
    SOURCE_CODE("java",
            "tsx",
            "yaml",
            "text/javascript",
            "application/typescript",
            "text/jsx",
            "text/tsx",
            "application/json",
            "text/html",
            "text/css",
            "text/x-scss",
            "text/x-sass",
            "text/x-less",
            "text/markdown",
            "application/x-yaml",
            "application/xml",
            "text/x-java-source",
            "text/x-kotlin",
            "application/x-groovy",
            "text/x-python",
            "application/x-ruby",
            "application/x-httpd-php",
            "text/x-c",
            "text/x-c++src",
            "text/x-csharp",
            "text/x-go",
            "text/rust",
            "application/x-sh",
            "application/x-powershell",
            "text/x-makefile",
            "text/x-dockerfile",
            "application/sql",
            "application/graphql",
            "application/toml",
            "text/javascript",
            "application/x-yaml",
            "yml",
            "ts",
            "js",
            "css",
            "scss",
            "c",
            "py",
            "html",
            "xml",
            "json",
            "sql",
            "bash",
            "shell",
            "powershell",
            "go",
            "kotlin",
            "swift",
            "php",
            "ruby",
            "r",
            "dart",
            "objectivec",
            "cpp",
            "csharp",
            "fsharp",
            "groovy",
            "less",
            "markdown",
            "md",
            "ini",
            "properties",
            "dockerfile",
            "makefile",
            "rust",
            "scala",
            "vbnet",
            "wasm"),


    /**
     * 压缩文件
     */
    ARCHIVE("application/zip",
            "application/x-rar-compressed",
            "application/x-tar",
            "application/gzip",
            "application/x-bzip2"),

    /**
     * 文件夹
     */
    DIRECTORY("dir"),

    /**
     * 其它
     */
    OTHER("application/octet-stream");

    private final String[] mimeTypes;

    ResourceCategoryEnum(String... mimeTypes) {
        this.mimeTypes = mimeTypes;
    }


    /**
     * 根据mimeType获取分类名称
     *
     * @param mimeType mimeType
     * @return 分类名称
     */
    public static String resolveCategoryNameByMimeType(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return OTHER.name();
        }
        String lowerMimeType = mimeType.toLowerCase();
        for (ResourceCategoryEnum category : ResourceCategoryEnum.values()) {
            for (String supportedMimeType : category.mimeTypes) {
                if (lowerMimeType.equals(supportedMimeType)) {
                    return category.name();
                }
            }
        }
        return OTHER.name();
    }
}
