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
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.ParamConfigs;

import java.util.List;

/**
 * <p>
 * 参数配置接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/2 23:12
 */
public interface ParamConfigService extends IService<ParamConfigs> {

    /**
     * 保存参数配置
     * @param dto 参数dto对象
     * @return 主键id
     */
    long save(ParamConfigDTO dto);

    /**
     * 编辑参数配置
     * @param dto  参数dto对象
     */
    void edit(ParamConfigDTO dto);

    /**
     * 通过主键id查询参数配置
     * @param id 主键id
     * @return 参数配置dto
     */
    ParamConfigDTO findById(Long id);

    /**
     * 分页查询对象
     * @param queryDTO 查询的对象
     * @return 分页结果
     */
    PageResultDTO<ParamConfigDTO> pageList(ParamConfigPageQueryDTO queryDTO);


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
    List<ParamConfigDTO> findListByGroupKey(String groupKey);
}
