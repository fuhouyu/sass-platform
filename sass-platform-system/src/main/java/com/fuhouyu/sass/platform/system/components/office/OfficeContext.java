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
package com.fuhouyu.sass.platform.system.components.office;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fuhouyu.sass.platform.system.enums.OfficeModeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * office文件的上下文
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/27 20:43
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OfficeContext implements AutoCloseable {

    private static final ThreadLocal<OfficeContextDTO> RESOURCE_DETAIL_THREAD_LOCAL = new TransmittableThreadLocal<>();


    public static void set(OfficeContextDTO officeContextDTO) {
        RESOURCE_DETAIL_THREAD_LOCAL.set(officeContextDTO);
    }

    public static OfficeContextDTO get() {
        return RESOURCE_DETAIL_THREAD_LOCAL.get();
    }

    public static void clear() {
        RESOURCE_DETAIL_THREAD_LOCAL.remove();
    }

    @Override
    public void close() throws Exception {
        RESOURCE_DETAIL_THREAD_LOCAL.remove();
    }

    @Builder
    @Getter
    public static class OfficeContextDTO {

        /**
         * 主键id
         */
        private Long id;

        /**
         * 名称
         */
        private String name;

        /**
         * 文件类型
         */
        private String mimeType;

        /**
         * 桶名
         */
        private String bucketName;

        /**
         * 对象key
         */
        private String objectKey;

        /**
         * 版本号
         */
        private String version;

        /**
         * office模式
         */
        private OfficeModeEnum mode;

    }
}
