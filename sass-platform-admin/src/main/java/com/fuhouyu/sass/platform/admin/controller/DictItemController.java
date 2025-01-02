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
import com.fuhouyu.sass.platform.system.dto.dict.DictItemDTO;
import com.fuhouyu.sass.platform.system.dto.dict.DictItemPageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.service.DictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典项 web 控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 18:50
 */
@RestController
@RequestMapping("/v1/dict-item")
@Tag(name = "字典项 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DictItemController {

    private final DictItemService dictItemService;


    /**
     * 保存字典项
     *
     * @param dictItemDTO 字典项dto对象
     * @return 主键id
     */
    @PostMapping
    @Operation(summary = "保存字典项")
    @PreAuthorize("@auth.hasPermission('system:dict-item:add')")
    public BaseResponse<Long> save(@Valid @RequestBody DictItemDTO dictItemDTO) {
        return ResponseHelper.success(this.dictItemService.save(dictItemDTO));
    }

    /**
     * 修改字典项
     *
     * @param dictItemDTO 字典项dto
     * @return void
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改字典项")
    @PreAuthorize("@auth.hasPermission('system:dict-item:edit')")
    public BaseResponse<Void> update(@PathVariable("id") Long id,
                                     @Valid @RequestBody DictItemDTO dictItemDTO) {
        dictItemDTO.setId(id);
        this.dictItemService.edit(dictItemDTO);
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
    @PreAuthorize("@auth.hasPermission('system:dict-item:query')")
    public BaseResponse<DictItemDTO> getById(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.dictItemService.findById(id));
    }

    /**
     * 通过id集合删除数据
     *
     * @param ids ids
     * @return 影响行数
     */
    @DeleteMapping
    @Operation(summary = "通过id集合删除数据")
    @PreAuthorize("@auth.hasPermission('system:dict-item:delete')")
    public BaseResponse<Integer> deleteByIds(@NotEmpty(message = "删除的数据未选择") @RequestBody List<Long> ids) {
        return ResponseHelper.success(this.dictItemService.removeByIds(ids));
    }


    /**
     * 字典项分页查询列表
     *
     * @param pageQueryDTO 分页查询dto对象
     * @return 分页查询的dto对象
     */
    @GetMapping("/page")
    @Operation(summary = "字典项分页查询列表")
    @PreAuthorize("@auth.hasPermission('system:dict-item:list')")
    public BaseResponse<PageResultDTO<DictItemDTO>> pageList(@ParameterObject DictItemPageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(this.dictItemService.pageList(pageQueryDTO));
    }


    /**
     * 检查字典项编码是否存在
     *
     * @param dictCode 字典编码
     * @param itemCode 字典项编码
     * @return true 已存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查字典项编码是否存在")
    @Parameter(name = "dictCode", description = "字典编码")
    @Parameter(name = "itemCode", description = "字典项编码")
    @PreAuthorize("@auth.hasPermission('system:dict-item:add')")
    public BaseResponse<Boolean> checkCodeExists(@RequestParam("dictCode") String dictCode,
                                                 @RequestParam("itemCode") String itemCode) {
        return ResponseHelper.success(this.dictItemService.checkItemCodeExists(dictCode, itemCode));
    }


    /**
     * 通过字典编码获取字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表
     */
    @GetMapping("/list")
    @Operation(summary = "通过字典编码获取字典项列表")
    @Parameter(name = "dictCode", description = "字典编码")
    public BaseResponse<List<DictItemDTO>> findDictItemList(@RequestParam("dictCode") String dictCode) {
        return ResponseHelper.success(this.dictItemService.findDictItemList(dictCode));
    }
}
