/*
 * Copyright 2024-2024 the original author or authors.
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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.PositionAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.position.PositionDTO;
import com.fuhouyu.sass.platform.system.entity.Positions;
import com.fuhouyu.sass.platform.system.mapper.PositionMapper;
import com.fuhouyu.sass.platform.system.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 职位实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private static final PositionAssembler POSITION_ASSEMBLER = PositionAssembler.INSTANCE;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final PositionMapper positionMapper;


    @Override
    public Long save(PositionDTO dto) {
        if (this.checkPositionExists(dto.getPositionCode())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前岗位编码:[%s] 已存在", dto.getPositionCode());
        }
        long id = snowflakeIdWorker.nextId();
        Positions entity = POSITION_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        this.positionMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(PositionDTO dto) {
        Positions positions = POSITION_ASSEMBLER.toEntity(dto);
        this.positionMapper.update(positions);
    }

    @Override
    public int removeById(Long id) {
        return this.positionMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.positionMapper.deleteByIds(ids);
    }

    @Override
    public PositionDTO findById(Long id) {
        return POSITION_ASSEMBLER.toDTO(this.positionMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<PositionDTO>> getPageResult() {
        return p -> POSITION_ASSEMBLER.toDTO(this.positionMapper.queryList(p));
    }

    @Override
    public Boolean checkPositionExists(String positionCode) {
        Positions positions = this.positionMapper.queryPositionByPositionCode(positionCode);
        return Objects.nonNull(positions);
    }
}
