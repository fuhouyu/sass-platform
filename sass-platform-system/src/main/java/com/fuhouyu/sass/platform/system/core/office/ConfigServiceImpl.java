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
package com.fuhouyu.sass.platform.system.core.office;

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.documenteditor.config.document.Permissions;
import com.onlyoffice.service.documenteditor.config.DefaultConfigService;

/**
 * <p>
 * 配置实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 15:42
 */
public class ConfigServiceImpl extends DefaultConfigService {


    public ConfigServiceImpl(DocumentManager documentManager,
                             UrlManager urlManager,
                             JwtManager jwtManager,
                             SettingsManager settingsManager) {
        super(documentManager, urlManager, jwtManager, settingsManager);
    }

    @Override
    public Permissions getPermissions(String fileId) {
        // TODO 这里先不设置权限
        return Permissions.builder()
                .edit(true)
                .build();
    }
}
