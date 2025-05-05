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

import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoCountryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitSummaryDTO;

import java.util.List;

/**
 * <p>
 * matomo 接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 19:41
 */
public interface MatomoService {

    /**
     * 获取访问统计
     *
     * @param queryDTO 查询条件
     * @return 访问统计
     */
    List<MatomoVisitSummaryDTO> getVisitSummary(MatomoVisitQueryDTO queryDTO);

    /**
     * 获取国家访问统计
     *
     * @param queryDTO 查询的dto对象
     * @return 国家访问统计
     */
    List<MatomoCountryDTO> getCountryVisit(MatomoVisitQueryDTO queryDTO);
}
