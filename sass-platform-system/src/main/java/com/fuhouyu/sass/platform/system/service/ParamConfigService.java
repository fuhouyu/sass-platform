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

import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;

import java.util.List;

/**
 * <p>
 * 参数配置接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/2 23:12
 */
public interface ParamConfigService extends BaseService<Long, ParamConfigDTO> {


    /**
     * 检查配置key是否存在
     *
     * @param configKey 配置key
     * @return true 存在，false不存在
     */
    Boolean checkConfigKeyExists(String configKey);

    /**
     * 通过分组标识查询参数配置
     *
     * @param groupKey 分组标识
     * @return 参数配置dto
     */
    List<ParamConfigDTO> findParamConfigListByGroupKey(String groupKey);
}
