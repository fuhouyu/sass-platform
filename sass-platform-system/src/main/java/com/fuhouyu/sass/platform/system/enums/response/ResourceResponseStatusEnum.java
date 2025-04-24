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
package com.fuhouyu.sass.platform.system.enums.response;

import com.fuhouyu.framework.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 资源响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:41
 */
@RequiredArgsConstructor
@Getter
public enum ResourceResponseStatusEnum implements BaseResponseStatus {

    /**
     * 资源名称不能为空
     */
    RESOURCE_NAME_NOT_NULL(200101, "资源名称不能为空"),

    /**
     * 资源etag不能为空
     */
    RESOURCE_ETAG_NOT_NULL(200102, "资源etag不能为空"),

    /**
     * 资源公开状态不能为空
     */
    RESOURCE_PUBLIC_NOT_NULL(200103, "资源公开状态不能为空"),

    /**
     * 资源目录不能为空
     */
    RESOURCE_DIRECTORY_NOT_NULL(200104, "资源目录不能为空"),

    /**
     * 资源无权访问
     */
    RESOURCE_NOT_AUTH_ACCESS(200301, "资源无权访问"),

    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXISTS(200501, "资源不存在"),

    /**
     * 资源桶不存在
     */
    RESOURCE_BUCKET_NOT_EXISTS(200502, "资源桶不存在"),
    ;
    private final int code;

    private final String message;
}
