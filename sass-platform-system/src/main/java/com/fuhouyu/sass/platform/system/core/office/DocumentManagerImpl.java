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
import com.fuhouyu.sass.platform.system.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.onlyoffice.manager.document.DefaultDocumentManager;
import com.onlyoffice.manager.settings.SettingsManager;

/**
 * <p>
 * 文档管理的实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 14:27
 */
public class DocumentManagerImpl extends DefaultDocumentManager {

    private final ResourceService resourceService;

    public DocumentManagerImpl(SettingsManager settingsManager, ResourceService resourceService) {
        super(settingsManager);
        this.resourceService = resourceService;
    }


    @Override
    public String getDocumentKey(String fileId, boolean embedded) {
        return fileId;
    }

    @Override
    public String getDocumentName(String fileId) {
        ResourceDTO resourceDTO = this.resourceService.checkResourceExists(NumberFormatUtil.toLong(fileId));
        return resourceDTO.getName();
    }

}

