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
package com.fuhouyu.sass.platform.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuhouyu.sass.platform.system.domain.dto.site.SiteSettingDTO;
import com.fuhouyu.sass.platform.system.domain.entity.SiteSetting;

/**
 * <p>
 * 网站设置接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:29
 */
public interface SiteSettingService extends IService<SiteSetting> {

    /**
     * 获取网站设置
     *
     * @return 网站设置
     */
    SiteSettingDTO getSiteSetting();

    /**
     * 更新网站设置
     *
     * @param siteSetting 网站设置
     * @return 是否更新成功
     */
    boolean updateSiteSetting(SiteSettingDTO siteSetting);
}
