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
package com.fuhouyu.sass.platform.system.components.properties;

import com.fuhouyu.sass.platform.common.constants.ConfigPropertiesConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * matomo 配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 20:11
 */
@ConfigurationProperties(prefix = MatomoProperties.PREFIX)
@Data
public class MatomoProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "matomo";

    /**
     * 访问url
     */
    private String apiUrl;

    /**
     * token
     */
    private String token;

    /**
     * 站点id
     */
    private Integer siteId;

}
