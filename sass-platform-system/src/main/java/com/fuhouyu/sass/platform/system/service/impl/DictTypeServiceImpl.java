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
import com.fuhouyu.sass.platform.system.assembler.DictTypeAssembler;
import com.fuhouyu.sass.platform.system.dto.dict.DictTypeDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.DictType;
import com.fuhouyu.sass.platform.system.mapper.DictTypeMapper;
import com.fuhouyu.sass.platform.system.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 字典类型实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 17:10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DictTypeServiceImpl implements DictTypeService {

    private static final DictTypeAssembler DICT_TYPE_ASSEMBLER = DictTypeAssembler.INSTANCE;

    private final DictTypeMapper dictTypeMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Boolean checkDictCodeExists(String dictCode) {
        DictType dictType = this.dictTypeMapper.queryByDictCode(dictCode);
        return Objects.nonNull(dictType);
    }

    @Override
    public Long save(DictTypeDTO dto) {
        long id = snowflakeIdWorker.nextId();
        if (Objects.equals(Boolean.TRUE, this.checkDictCodeExists(dto.getDictCode()))) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    String.format("字典编码 [%s] 已存在", dto.getDictCode()));
        }
        DictType entity = DICT_TYPE_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        entity.setIsAllowModified(true);
        this.dictTypeMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(DictTypeDTO dto) {
        this.dictTypeMapper.update(DICT_TYPE_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.dictTypeMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.dictTypeMapper.deleteByIds(ids);
    }

    @Override
    public DictTypeDTO findById(Long id) {
        DictType dictType = this.dictTypeMapper.queryById(id);
        return DICT_TYPE_ASSEMBLER.toDTO(dictType);
    }

    @Override
    public Function<PageQueryDTO, List<DictTypeDTO>> getPageResult() {
        return p -> DICT_TYPE_ASSEMBLER.toDTO(this.dictTypeMapper.queryList(p));
    }
}
