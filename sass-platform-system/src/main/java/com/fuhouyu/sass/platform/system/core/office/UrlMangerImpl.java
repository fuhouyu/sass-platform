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

import com.fuhouyu.framework.common.utils.NumberFormatUtil;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import lombok.NonNull;

/**
 * <p>
 * url 管理实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 15:32
 */
public class UrlMangerImpl extends DefaultUrlManager {


    private final ResourceService resourceService;

    public UrlMangerImpl(SettingsManager settingsManager,
                         ResourceService resourceService) {
        super(settingsManager);
        this.resourceService = resourceService;
    }

    @Override
    public String getFileUrl(@NonNull String fileId) {
        return resourceService.generatePresignerDownloadUrl(NumberFormatUtil.toLong(fileId));
    }


}
