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
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.admin.enums.SseResponseTypeEnum;
import com.fuhouyu.sass.platform.admin.response.SseResponseMessage;
import com.fuhouyu.sass.platform.system.domain.dto.ValidGroups;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.*;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
@LogModule("资源模块")
public class ResourceController {

    private final ResourceService resourceService;


    /**
     * 生成stsToken
     *
     * @param requestDTO 生成请求的dto对象
     * @return stsToken
     */
    @PostMapping("/sts-token")
    @Operation(summary = "生成临时的stsToken")
    public BaseResponse<StsTemporaryTokenResponseDTO> generateStsToken(@Valid @RequestBody StsTemporaryTokenRequestDTO requestDTO) {
        return ResponseHelper.success(this.resourceService.generateToken(requestDTO));
    }


    /**
     * 下载文件
     *
     * @param id      资源id
     * @param preview 是否预览
     */
    @GetMapping("/{id}/download")
    @Operation(summary = "下载资源文件（需要登录）")
    @NoAuth
    public void downloadWithToken(@PathVariable("id") Long id,
                                  @RequestParam("preview") Boolean preview) {
        this.resourceService.downloadFile(id, preview);
    }

    /**
     * 下载文件
     *
     * @param id                   资源id
     * @param resourceSignedUrlDTO 资源签名url dto对象
     */
    @GetMapping("/{id}/download-signed")
    @Operation(summary = "下载资源文件")
    @NoAuth
    public void downloadFile(@PathVariable("id") Long id,
                             ResourceSignedUrlDTO resourceSignedUrlDTO) {
        this.resourceService.downloadFile(id, resourceSignedUrlDTO);
    }


    /**
     * 分享资源
     *
     * @param id                  资源id
     * @param singedUrlRequestDTO 签名请求的url
     * @return 分享链接
     */
    @GetMapping("/{id}/share")
    @Operation(summary = "分享资源")
    public BaseResponse<String> share(@PathVariable("id") Long id,
                                      SingedUrlRequestDTO singedUrlRequestDTO) {
        // 分享的链接默认下载
        singedUrlRequestDTO.setPreview(false);
        return ResponseHelper.success(this.resourceService.generateSignedUrl(id, singedUrlRequestDTO));
    }

    /**
     * 保存资源信息
     *
     * @param resourceDTO 资源dto对象
     * @return 主键id
     */
    @Operation(summary = "保存资源信息")
    @PostMapping
    @LogRecord(operationType = OperationTypeEnum.CREATE, riskType = RiskTypeEnum.MIDDLE_LEVEL)
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
    @PreAuthorize("@auth.hasAnyPermission('tenant:resource:delete')")
    @LogRecord(operationType = OperationTypeEnum.DELETE, riskType = RiskTypeEnum.HIGH_LEVEL)
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
    @PreAuthorize("@auth.hasAnyPermission('tenant:resource:list')")
    public BaseResponse<PageResultDTO<ResourceDTO>> listResourceByTenantId(ResourcePageQueryDTO pageQueryDTO) {
        return ResponseHelper.success(this.resourceService.pageList(pageQueryDTO));
    }


    /**
     * 生成签名url
     *
     * @param id                  主键id
     * @param singedUrlRequestDTO 签名请求的dto对象
     * @return 签名url
     */
    @Operation(summary = "生成签名url")
    @GetMapping("/generate/{id}/signed-url")
    public BaseResponse<String> generateSignedUrl(@PathVariable("id") Long id,
                                                  SingedUrlRequestDTO singedUrlRequestDTO) {
        return ResponseHelper.success(this.resourceService.generateSignedUrl(id, singedUrlRequestDTO));
    }


    /**
     * 统计有多少个object
     *
     * @return 总数
     */
    @GetMapping("/count")
    @Operation(summary = "资源总数")
    @PreAuthorize("@auth.hasAnyPermission('tenant:resource:list')")
    public BaseResponse<Integer> countObjects() {
        return ResponseHelper.success(this.resourceService.countObjects());
    }


    /**
     * 修改公开/私有资源
     *
     * @param id       主键id
     * @param isPublic true / false
     * @return void
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "设置资源的公开/私有访问")
    @Parameter(name = "public", description = "true 公开 false 私用")
    @PreAuthorize("@auth.hasAnyPermission('system:role:edit')")
    @LogRecord(operationType = OperationTypeEnum.UPDATE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> editStatus(@PathVariable("id") Long id,
                                         @RequestParam("public") Boolean isPublic) {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(id);
        resourceDTO.setIsPublic(isPublic);
        this.resourceService.edit(resourceDTO);
        return ResponseHelper.success();
    }


    /**
     * 读取文件到字节数组
     *
     * @param id 主键id
     * @return 读取文件到字节数组
     */
    @GetMapping(value = "/{id}/bytes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "读取文件到字节数组 sse")
    public Flux<SseResponseMessage<String>> readFileToByteArray(@PathVariable("id") Long id) {
        return Flux.create(sink -> this.resourceService.readFileToByteArray(id, inputStream -> {
            byte[] buffer = new byte[1024 * 16]; // 4KB 块大小
            int bytesRead;
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                    SseResponseMessage<String> message =
                            SseResponseMessage.success(SseResponseTypeEnum.MESSAGE, "content part", chunk);
                    sink.next(message);
                }

            } catch (IOException e) {
                sink.error(e);
            }
            sink.next(SseResponseMessage.done("read complete", ""));
            sink.complete();
        }), FluxSink.OverflowStrategy.BUFFER);
    }
}
