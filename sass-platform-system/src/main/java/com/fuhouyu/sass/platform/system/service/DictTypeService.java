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
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictTypeDTO;
import com.fuhouyu.sass.platform.system.domain.dto.dict.DictTypePageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.DictType;

import java.util.List;

/**
 * <p>
 * 字典类型接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 17:09
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 保存字典类型
     *
     * @param dto dto对象
     * @return 主键
     */
    long save(DictTypeDTO dto);

    /**
     * 修改字典类型
     *
     * @param dto dto对象
     */
    void edit(DictTypeDTO dto);

    /**
     * 根据主键查询字典类型     * @param id
     *
     * @return 字典类型
     */
    DictTypeDTO findById(Long id);

    /**
     * 分页查询字典类型
     *
     * @param pageQueryDTO 分页查询对象
     * @return 分页结果
     */
    PageResultDTO<DictTypeDTO> pageList(DictTypePageQueryDTO pageQueryDTO);

    /**
     * 检查字典编码是否存在
     *
     * @param dictCode 字典编码
     * @return true 已存在，false 不存在
     */
    Boolean checkDictCodeExists(String dictCode);

    /**
     * 查询字典类型集合
     *
     * @return 字典类型集合
     */
    List<DictTypeDTO> findList();
}
