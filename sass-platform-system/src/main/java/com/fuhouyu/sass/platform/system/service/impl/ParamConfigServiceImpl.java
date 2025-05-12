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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.ParamConfigAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.ParamConfigs;
import com.fuhouyu.sass.platform.system.mapper.ParamConfigMapper;
import com.fuhouyu.sass.platform.system.service.ParamConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 参数配置实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/2 23:13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ParamConfigServiceImpl extends ServiceImpl<ParamConfigMapper, ParamConfigs> implements ParamConfigService {

    private static final ParamConfigAssembler PARAM_CONFIG_ASSEMBLER = ParamConfigAssembler.INSTANCE;

    private final ParamConfigMapper paramConfigMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public long save(ParamConfigDTO dto) {
        this.checkConfigKeyExists(dto.getConfigKey());
        ParamConfigs entity = PARAM_CONFIG_ASSEMBLER.toEntity(dto);
        long id = snowflakeIdWorker.nextId();
        entity.setId(id);
        this.paramConfigMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(ParamConfigDTO dto) {
        this.paramConfigMapper.updateById(PARAM_CONFIG_ASSEMBLER.toEntity(dto));
    }


    @Override
    public ParamConfigDTO findById(Long id) {
        return PARAM_CONFIG_ASSEMBLER.toDTO(this.paramConfigMapper.selectById(id));
    }


    @Override
    public Boolean checkConfigKeyExists(String configKey) {
        return Objects.nonNull(this.paramConfigMapper.queryByConfigKey(configKey));
    }

    @Override
    public List<ParamConfigDTO> findListByGroupKey(String groupKey) {
        return PARAM_CONFIG_ASSEMBLER.toDTO(this.paramConfigMapper.queryByGroupKey(groupKey));
    }


    @Override
    public PageResultDTO<ParamConfigDTO> pageList(ParamConfigPageQueryDTO queryDTO) {
        LambdaQueryWrapper<ParamConfigs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(queryDTO.getGroupKey()), ParamConfigs::getGroupKey, queryDTO.getGroupKey());
        lambdaQueryWrapper.like(StringUtils.hasText(queryDTO.getConfigName()), ParamConfigs::getConfigName, queryDTO.getConfigName());
        lambdaQueryWrapper.like(StringUtils.hasText(queryDTO.getConfigKey()), ParamConfigs::getConfigKey, queryDTO.getConfigKey());
        return PageResultDTO.buildPageResult(this.paramConfigMapper.selectPage(queryDTO, lambdaQueryWrapper), PARAM_CONFIG_ASSEMBLER::toDTO);
    }
}
