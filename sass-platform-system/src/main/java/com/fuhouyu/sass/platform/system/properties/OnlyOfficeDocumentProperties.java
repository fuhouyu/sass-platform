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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.sass.platform.common.constants.ConfigPropertiesConstant;
import com.onlyoffice.model.settings.SettingsConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * only office 配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 14:15
 */
@Data
@ConfigurationProperties(prefix = OnlyOfficeDocumentProperties.PREFIX)
public class OnlyOfficeDocumentProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "only-office";

    /**
     * only office 是否启用
     */
    private Boolean enabled;

    /**
     * URL to the Document Server.
     */
    @JsonProperty(SettingsConstants.URL)
    private String url;

    /**
     * Internal URL to the Document Server.
     */
    @JsonProperty(SettingsConstants.INNER_URL)
    private String innerUrl;

    /**
     * Internal URL to the product.
     */
    @JsonProperty(SettingsConstants.PRODUCT_INNER_URL)
    private String productInnerUrl;

    /**
     * Secret authorization key.
     */
    @JsonProperty(SettingsConstants.SECURITY_KEY)
    private String securityKey;

    /**
     * Authorization header.
     */
    @JsonProperty(SettingsConstants.SECURITY_HEADER)
    private String securityHeader;

    /**
     * Authorization prefix.
     */
    @JsonProperty(SettingsConstants.SECURITY_PREFIX)
    private String securityPrefix;

    /**
     * Setting to ignore SSL certificate.
     */
    @JsonProperty(SettingsConstants.HTTP_CLIENT_IGNORE_SSL_CERTIFICATE)
    private Boolean ignoreSslCertificate;

    /**
     * Setting to edit the specified extensions with the possible loss of information.
     */
    @JsonProperty(SettingsConstants.LOSSY_EDIT)
    private Boolean lossyEdit;
}
