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
package com.fuhouyu.sass.platform.system.properties;

import com.fuhouyu.sass.platform.system.enums.OpenPlatformTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 开放平台配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 21:41
 */
@Data
@ConfigurationProperties(prefix = OpenPlatformProperties.PREFIX)
public class OpenPlatformProperties {

    public static final String PREFIX = "sass.platform";

    /**
     * 开放平台配置项
     */
    private Map<OpenPlatformTypeEnum, Properties> openPlatform;

    public OpenPlatformProperties() {
        this.openPlatform = new HashMap<>();
    }

    @Data
    public static class Properties {

        /**
         * ak
         */
        private String accessKey;

        /**
         * sk
         */
        private String secretKey;

        /**
         * url
         */
        private String baseUrl;
    }
}
