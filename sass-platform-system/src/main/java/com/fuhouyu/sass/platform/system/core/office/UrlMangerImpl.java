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

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import jakarta.servlet.http.HttpServletRequest;
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

    private static final String DOWNLOAD_FILE_PATH = "/v1/resource/download/";
    private final HttpServletRequest request;

    public UrlMangerImpl(SettingsManager settingsManager,
                         HttpServletRequest request) {
        super(settingsManager);
        this.request = request;
    }

    @Override
    public String getFileUrl(@NonNull String fileId) {
        return this.getServerUrl() + DOWNLOAD_FILE_PATH + fileId;
    }

    /**
     * 获取服务url
     *
     * @return url
     */
    private String getServerUrl() {
        return String.format("%s://%s:%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
    }

}
