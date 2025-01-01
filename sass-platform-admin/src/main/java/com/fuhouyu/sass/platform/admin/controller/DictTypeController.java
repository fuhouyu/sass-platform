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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.sass.platform.system.dto.dict.DictTypeDTO;
import com.fuhouyu.sass.platform.system.dto.dict.DictTypePageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.DictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典类型 api接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 16:50
 */
@RestController
@RequestMapping("/v1/dict-type")
@Tag(name = "字典类型 web接口")
@Validated
@RequiredArgsConstructor
public class DictTypeController {

    private final DictTypeService dictTypeService;


    /**
     * 保存字典类型
     *
     * @param dictTypeDTO 字典类型dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存字典类型")
    @PreAuthorize("@auth.hasPermission('system:dict-type:add')")
    public BaseResponse<Long> save(@Validated @RequestBody DictTypeDTO dictTypeDTO) {
        return ResponseHelper.success(this.dictTypeService.save(dictTypeDTO));
    }

    /**
     * 修改字典类型
     *
     * @param dictTypeDTO 字典类型dto
     * @return void
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改字典类型")
    @PreAuthorize("@auth.hasPermission('system:dict-type:edit')")
    public BaseResponse<Void> update(@PathVariable("id") Long id,
                                     @Validated @RequestBody DictTypeDTO dictTypeDTO) {
        dictTypeDTO.setId(id);
        this.dictTypeService.edit(dictTypeDTO);
        return ResponseHelper.success();
    }

    /**
     * 通过主键id获取详情
     *
     * @param id 主键id
     * @return 详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "通过主键id获取详情")
    @PreAuthorize("@auth.hasPermission('system:dict-type:query')")
    public BaseResponse<DictTypeDTO> getById(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.dictTypeService.findById(id));
    }

    /**
     * 通过id集合删除数据
     *
     * @param ids ids
     * @return 影响行数
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除数据")
    @PreAuthorize("@auth.hasPermission('system:dict-type:delete')")
    public BaseResponse<Integer> deleteByIds(@NotEmpty(message = "删除的数据未选择") @RequestBody List<Long> ids) {
        return ResponseHelper.success(this.dictTypeService.removeByIds(ids));
    }


    /**
     * 字典类型分页查询列表
     *
     * @param pageQueryDTO 分页查询dto对象
     * @return 分页查询的dto对象
     */
    @GetMapping("/page")
    @Operation(summary = "字典类型分页查询列表")
    @PreAuthorize("@auth.hasPermission('system:dict-type:list')")
    public BaseResponse<PageResultDTO<DictTypeDTO>> pageList(@ParameterObject DictTypePageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(this.dictTypeService.pageList(pageQueryDTO));
    }


    /**
     * 检查字典类型编码是否存在
     *
     * @param dictCode 字典类型编码
     * @return true 已存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查字典类型编码是否存在")
    @Parameter(name = "dictCode", description = "字典类型编码")
    @PreAuthorize("@auth.hasPermission('system:dict-type:add')")
    public BaseResponse<Boolean> checkCodeExists(@RequestParam("dictCode") String dictCode) {
        return ResponseHelper.success(this.dictTypeService.checkDictCodeExists(dictCode));
    }

    /**
     * 查询出字典类型列表
     *
     * @return 字典类型列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询出所有的字典类型列表")
    @PreAuthorize("@auth.hasPermission('system:dict-type:list')")
    public BaseResponse<List<DictTypeDTO>> findList() {
        return ResponseHelper.success(this.dictTypeService.findList());
    }

}
