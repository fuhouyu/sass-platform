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

import com.fuhouyu.sass.platform.common.constants.ConfigPropertiesConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * cloudflare配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/2 21:27
 */
@ConfigurationProperties(prefix = CloudflareProperties.PREFIX)
@Data
public class CloudflareProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "cloudflare";

    /**
     * cloudflare 验证码的配置项
     */
    private Turnstile turnstile;

    @Data
    public static class Turnstile {

        /**
         * 启禁用状态
         */
        private Boolean enabled;

        /**
         * url
         */
        private String url;

        /**
         * 密钥
         */
        private String secret;
    }
}
