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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.dto.ValidGroups;
import com.fuhouyu.sass.platform.system.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.dto.resource.*;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 资源控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/16 12:40
 */
@RestController
@RequestMapping("/v1/resource")
@Tag(name = "资源 web接口")
@Validated
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;


    /**
     * 生成stsToken
     * @param requestDTO 生成请求的dto对象
     * @return stsToken
     */
    @PostMapping("/sts-token")
    @Operation(summary = "生成临时的stsToken")
    public BaseResponse<StsTemporaryTokenResponseDTO> generateStsToken(@Valid @RequestBody StsTemporaryTokenRequestDTO requestDTO) {
        return ResponseHelper.success(this.resourceService.generateToken(requestDTO));
    }


    /**
     * 读取文件
     *
     * @param id       资源id
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/preview/{id}")
    @Operation(summary = "读取资源文件")
    @NoAuth
    public void preview(@PathVariable("id") Long id,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        this.resourceService.downloadFile(id, true, request, response);
    }


    /**
     * 下载文件
     *
     * @param id       资源id
     * @param resourceSignedUrlDTO 签名dto对象
     */
    @GetMapping("/download/{id}")
    @Operation(summary = "下载资源文件")
    @NoAuth
    public void downloadFile(@PathVariable("id") Long id,
                             ResourceSignedUrlDTO resourceSignedUrlDTO) {
        this.resourceService.downloadFile(id, resourceSignedUrlDTO);
    }

    /**
     * 保存资源信息
     *
     * @param resourceDTO 资源dto对象
     * @return 主键id
     */
    @Operation(summary = "保存资源信息")
    @PostMapping
    public BaseResponse<Long> saveResource(@RequestBody @Validated({ValidGroups.SaveGroup.class}) ResourceDTO resourceDTO) {
        return ResponseHelper.success(this.resourceService.save(resourceDTO));
    }


    /**
     * 通过资源id删除资源
     *
     * @param ids 资源id集合
     * @return 成功响应
     */
    @Operation(summary = "通过资源id删除资源")
    @DeleteMapping
    @PreAuthorize("@auth.hasAnyPermission('tenant-space:delete')")
    public BaseResponse<Void> removeResourceList(
            @RequestBody
            @Size(min = 1, message = "需要删除的资源不能为空")
            @NotNull(message = "需要删除的资源不能为空") List<Long> ids) {
        this.resourceService.removeByIds(ids);
        return ResponseHelper.success();
    }


    /**
     * 通过etag获取资源信息
     *
     * @param etag etag
     * @return 资源信息
     */
    @Operation(summary = "通过etag获取资源信息")
    @GetMapping
    @Parameter(name = "etag", description = "etag")
    public BaseResponse<ResourceDTO> getResourceByEtag(@RequestParam("etag") String etag) {
        return ResponseHelper.success(this.resourceService.findResourceByEtag(etag));
    }


    /**
     * 分页查询资源信息，需要租户空间权限
     *
     * @param pageQueryDTO 分页查询dto对象
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询资源信息，需要租户空间权限")
    @PreAuthorize("@auth.hasAnyPermission('tenant-space:resource-list')")
    public BaseResponse<PageResultDTO<ResourceDTO>> listResourceByTenantId(ResourcePageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(this.resourceService.pageList(pageQueryDTO));
    }


    /**
     * 生成签名url
     *
     * @param id 主键id
     * @return 签名url
     */
    @Operation(summary = "生成签名url")
    @GetMapping("/generate/signed-url/{id}")
    public BaseResponse<String> generateSignedUrl(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.resourceService.generateSignedUrl(id));
    }
}
