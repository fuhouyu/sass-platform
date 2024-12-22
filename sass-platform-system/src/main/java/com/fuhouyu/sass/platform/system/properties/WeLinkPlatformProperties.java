/*
 * Copyright 2024-2024 the original author or authors.
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
 * weLink配置类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/22 18:15
 */
@ConfigurationProperties(prefix = WeLinkPlatformProperties.PREFIX)
@Data
public class WeLinkPlatformProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "welink";

    private String clientId;

    private String clientSecret;

    private String baseUrl;

    public WeLinkPlatformProperties() {
        this.baseUrl = "https://open.welink.huaweicloud.com";
    }
}
