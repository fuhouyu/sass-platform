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
package com.fuhouyu.sass.platform.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.sass.platform.system.assembler.SiteSettingAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.site.SiteSettingDTO;
import com.fuhouyu.sass.platform.system.domain.entity.SiteSetting;
import com.fuhouyu.sass.platform.system.mapper.SiteSettingMapper;
import com.fuhouyu.sass.platform.system.service.SiteSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网站设置实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:30
 */
@Service
@Slf4j
public class SiteSettingServiceImpl extends ServiceImpl<SiteSettingMapper, SiteSetting> implements SiteSettingService {

    private static final SiteSettingAssembler SITE_SETTING_ASSEMBLER = SiteSettingAssembler.INSTANCE;

    @Override
    public synchronized SiteSettingDTO getSiteSetting() {
        List<SiteSetting> siteSettings = this.baseMapper.selectList(null);
        return siteSettings.stream().map(SITE_SETTING_ASSEMBLER::toDTO).findFirst().orElse(null);
    }

    @Override
    public boolean updateSiteSetting(SiteSettingDTO siteSetting) {
        int count = this.baseMapper.updateById(SITE_SETTING_ASSEMBLER.toEntity(siteSetting));
        return count > 0;
    }
}
