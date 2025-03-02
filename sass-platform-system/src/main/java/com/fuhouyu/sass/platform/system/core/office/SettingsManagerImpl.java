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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.sass.platform.system.properties.OnlyOfficeDocumentProperties;
import com.onlyoffice.manager.settings.DefaultSettingsManager;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * office 设置管理类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 14:47
 */
public class SettingsManagerImpl extends DefaultSettingsManager {

    private final Map<String, String> propertiesMap;

    public SettingsManagerImpl(OnlyOfficeDocumentProperties onlyOfficeDocumentProperties) {
        this.propertiesMap = JacksonUtil.tryParse(() ->
                JacksonUtil.getObjectMapper().convertValue(onlyOfficeDocumentProperties, new TypeReference<HashMap<String, String>>() {
                }));
    }

    @Override
    public String getSetting(String name) {
        return this.propertiesMap.get(name);
    }

    @Override
    public void setSetting(String name, String value) {
        this.propertiesMap.put(name, value);
    }
}
