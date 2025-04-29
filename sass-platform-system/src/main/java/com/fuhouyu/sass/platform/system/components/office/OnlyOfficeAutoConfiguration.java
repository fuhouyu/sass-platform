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

import cn.hutool.core.lang.Assert;
import com.fuhouyu.sass.platform.system.components.properties.OnlyOfficeDocumentProperties;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 15:44
 */
@Configuration
@ConditionalOnProperty(prefix = OnlyOfficeDocumentProperties.PREFIX,
        value = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class OnlyOfficeAutoConfiguration implements InitializingBean {

    private final OnlyOfficeDocumentProperties properties;

    private final S3Presigner s3Presigner;


    @Bean
    public DefaultSettingsManager settingsManager() {
        return new SettingsManagerImpl(properties);
    }

    @Bean
    public DocumentManager documentManager(DefaultSettingsManager settingsManager) {
        return new DocumentManagerImpl(settingsManager);
    }

    @Bean
    public UrlManager urlManager(SettingsManager settingsManager) {
        return new UrlMangerImpl(settingsManager, s3Presigner);
    }

    @Bean
    public ConfigService configService(DocumentManager documentManager,
                                       UrlManager urlManager,
                                       SettingsManager settingsManager) {
        return new ConfigServiceImpl(documentManager, urlManager,
                new DefaultJwtManager(settingsManager),
                settingsManager);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notBlank(properties.getUrl(), "only office url 未填写");
    }
}
