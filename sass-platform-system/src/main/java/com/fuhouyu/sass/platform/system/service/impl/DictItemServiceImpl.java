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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.DictItemAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictItemDTO;
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictItemPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.DictItem;
import com.fuhouyu.sass.platform.system.mapper.DictItemMapper;
import com.fuhouyu.sass.platform.system.service.DictItemService;
import com.fuhouyu.sass.platform.system.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典项实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 18:37
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DictItemServiceImpl implements DictItemService {

    private static final DictItemAssembler DICT_ITEM_ASSEMBLER = DictItemAssembler.INSTANCE;

    private final DictItemMapper dictItemMapper;

    private final DictTypeService dictTypeService;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Boolean checkItemCodeExists(String dictCode, String itemCode) {
        return Objects.nonNull(dictItemMapper.queryByDictCodeAndItemCode(dictCode, itemCode));
    }

    @Override
    public List<DictItemDTO> findDictItemList(String dictCode) {
        DictItemPageQueryDTO pageQueryDTO = new DictItemPageQueryDTO();
        pageQueryDTO.setDictCode(dictCode);
        return DICT_ITEM_ASSEMBLER.toDTO(this.dictItemMapper.queryList(pageQueryDTO));
    }

    @Override
    public Map<String, List<DictItemDTO>> findDictCodeItemMap(String dictCodes) {
        Collection<String> dictCodeList = StringUtils.commaDelimitedListToSet(dictCodes);
        List<DictItemDTO> list = DICT_ITEM_ASSEMBLER.toDTO(this.dictItemMapper.queryListByDictCodes(dictCodeList));
        return list.stream().collect(Collectors.groupingBy(DictItemDTO::getDictCode));
    }

    @Override
    public Long save(DictItemDTO dto) {
        if (!dictTypeService.checkDictCodeExists(dto.getDictCode())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    String.format("字典类型 [%s] 不存在", dto.getDictCode()));
        }
        if (this.checkItemCodeExists(dto.getDictCode(), dto.getItemCode())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    String.format("字典项编码 [%s] 已存在", dto.getItemCode()));
        }
        long id = snowflakeIdWorker.nextId();
        DictItem entity = DICT_ITEM_ASSEMBLER.toEntity(dto);
        entity.setIsAllowModified(true);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        entity.setId(id);
        this.dictItemMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(DictItemDTO dto) {
        this.dictItemMapper.update(DICT_ITEM_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.dictItemMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> id) {
        return this.dictItemMapper.deleteByIds(id);
    }

    @Override
    public DictItemDTO findById(Long id) {
        DictItem dictItem = this.dictItemMapper.queryById(id);
        return DICT_ITEM_ASSEMBLER.toDTO(dictItem);
    }

    @Override
    public Function<PageQueryDTO, List<DictItemDTO>> getPageResult() {
        return p -> DICT_ITEM_ASSEMBLER.toDTO(this.dictItemMapper.queryList(p));
    }
}
