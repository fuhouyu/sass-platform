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
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.dto.position.PositionDTO;
import com.fuhouyu.sass.platform.system.dto.position.PositionPageQueryDTO;
import com.fuhouyu.sass.platform.system.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 岗位控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 21:50
 */
@RestController
@RequestMapping("/v1/position")
@Tag(name = "岗位 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PositionController {

    private final PositionService positionService;


    /**
     * 通过id获取岗位详情
     *
     * @return 岗位详情dto对象
     */
    @Operation(summary = "岗位详情")
    @GetMapping("/{id}")
    @PreAuthorize("@auth.hasPermission('system:position:query')")
    public BaseResponse<PositionDTO> userinfo(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.positionService.findById(id));
    }


    /**
     * 修改当前岗位的详情
     *
     * @param positionDTO 岗位dto对象
     * @param id          主键id
     * @return restResult
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改岗位详情")
    @PreAuthorize("@auth.hasPermission('system:position:edit')")
    public BaseResponse<Void> editPosition(
            @PathVariable("id") Long id,
            @Valid @RequestBody PositionDTO positionDTO) {
        positionDTO.setId(id);
        this.positionService.edit(positionDTO);
        return ResponseHelper.success();
    }

    /**
     * 分页查询岗位列表
     *
     * @param userPageQueryDTO 岗位分页查询对象
     * @return 岗位列表集合
     */
    @GetMapping("/page")
    @Operation(summary = "获取岗位列表")
    @PreAuthorize("@auth.hasPermission('system:position:list')")
    public BaseResponse<PageResultDTO<PositionDTO>> pageList(PositionPageQueryDTO userPageQueryDTO) {
        return ResponseHelper.success(this.positionService.pageList(userPageQueryDTO));
    }

    /**
     * 通过岗位id删除岗位
     *
     * @param ids 岗位id集合
     * @return 成功响应
     */
    @Operation(summary = "通过岗位id删除岗位")
    @DeleteMapping
    @PreAuthorize("@auth.hasPermission('system:position:delete')")
    public BaseResponse<Void> removePositionList(
            @RequestBody
            @Size(min = 1, message = "需要删除的岗位不能为空")
            @NotNull(message = "需要删除的岗位不能为空") List<Long> ids) {
        this.positionService.removeByIds(ids);
        return ResponseHelper.success();
    }


    /**
     * 校验岗位编码是否存在
     *
     * @param positionCode 岗位编码
     * @return true 已存在，false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "校验岗位名是否存在，如果存在，则返回true")
    @Parameter(name = "positionCode", description = "岗位编码")
    public BaseResponse<Boolean> validPositionCodeExists(@RequestParam("positionCode") String positionCode) {
        return ResponseHelper.success(this.positionService.checkPositionExists(positionCode));
    }

    /**
     * 保存岗位信息
     *
     * @param positionDTO 岗位dto对象
     * @return 响应
     */
    @Operation(summary = "保存岗位信息")
    @PostMapping
    @PreAuthorize("@auth.hasPermission('system:position:add')")
    public BaseResponse<Long> savePosition(@RequestBody PositionDTO positionDTO) {
        return ResponseHelper.success(this.positionService.save(positionDTO));
    }


    /**
     * 获取所有的岗位列表
     *
     * @return 岗位列表
     */
    @GetMapping("/list-all")
    @Operation(summary = "获取所有的岗位列表")
    @PreAuthorize("@auth.hasPermission('system:position:list')")
    public BaseResponse<List<PositionDTO>> findPositionAllList() {
        return ResponseHelper.success(this.positionService.findPositionAllList());
    }
}
