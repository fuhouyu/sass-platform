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
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.DictTypeAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictTypeDTO;
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictTypePageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.DictType;
import com.fuhouyu.sass.platform.system.enums.response.DictTypeResponseStatusEnum;
import com.fuhouyu.sass.platform.system.mapper.DictTypeMapper;
import com.fuhouyu.sass.platform.system.service.DictTypeService;
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
 * 字典类型实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 17:10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    private static final DictTypeAssembler DICT_TYPE_ASSEMBLER = DictTypeAssembler.INSTANCE;

    private final DictTypeMapper dictTypeMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Boolean checkDictCodeExists(String dictCode) {
        DictType dictType = this.dictTypeMapper.queryByDictCode(dictCode);
        return Objects.nonNull(dictType);
    }

    @Override
    public List<DictTypeDTO> findList() {
        DictTypePageQueryDTO dictTypePageQueryDTO = new DictTypePageQueryDTO();
        dictTypePageQueryDTO.addOrder(OrderItem.asc("display_order"));
        return DICT_TYPE_ASSEMBLER.toDTO(this.dictTypeMapper.queryList(dictTypePageQueryDTO));
    }

    @Override
    public long save(DictTypeDTO dto) {
        long id = snowflakeIdWorker.nextId();
        if (Objects.equals(Boolean.TRUE, this.checkDictCodeExists(dto.getDictCode()))) {
            throw new ServiceException(DictTypeResponseStatusEnum.DICT_TYPE_CODE_EXISTS,
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
        this.dictTypeMapper.updateById(DICT_TYPE_ASSEMBLER.toEntity(dto));
    }


    @Override
    public DictTypeDTO findById(Long id) {
        DictType dictType = this.dictTypeMapper.queryById(id);
        return DICT_TYPE_ASSEMBLER.toDTO(dictType);
    }

    @Override
    public PageResultDTO<DictTypeDTO> pageList(DictTypePageQueryDTO pageQueryDTO) {
        LambdaQueryWrapper<DictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(pageQueryDTO.getDictCode()), DictType::getDictCode, pageQueryDTO.getDictCode());
        lambdaQueryWrapper.eq(Objects.nonNull(pageQueryDTO.getIsEnabled()), DictType::getIsEnabled, pageQueryDTO.getIsEnabled());
        return PageResultDTO.buildPageResult(this.dictTypeMapper.selectPage(pageQueryDTO, lambdaQueryWrapper), DICT_TYPE_ASSEMBLER::toDTO);

    }
}
