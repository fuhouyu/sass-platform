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

import com.fuhouyu.sass.platform.system.domain.dto.resource.*;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * <p>
 * 资源接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/16 12:40
 */
public interface ResourceService extends BaseService<Long, ResourceDTO> {


    /**
     * 下载资源
     *
     * @param id                   资源id
     * @param  preview 是否预览: true 预览， false 下载
     */
    void downloadFile(Long id,
                      Boolean preview);

    /**
     * 生成sts临时token
     *
     * @param requestDTO 请求dto对象
     * @return sts临时token
     */
    StsTemporaryTokenResponseDTO generateToken(StsTemporaryTokenRequestDTO requestDTO);

    /**
     * 根据etag查询资源
     *
     * @param etag etag
     * @return 资源对象
     */
    ResourceDTO findResourceByEtag(String etag);

    /**
     * 生成资源签名的url
     *
     * @param id 主键id
     * @param singedUrlRequestDTO 签名请求的url
     * @return 签名的url
     */
    String generateSignedUrl(Long id,
                             SingedUrlRequestDTO singedUrlRequestDTO);


    /**
     * 检查资源权限
     *
     * @param id 资源id
     * @return 资源dto对象
     */
    ResourceDetailDTO checkResourcePermission(Long id);

    /**
     * 资源总数
     *
     * @return 资源总数
     */
    Integer countObjects();

    /**
     * 读取文件到字节数组
     *
     * @param id 主键id
     * @param inputStreamConsumer 字节流
     */
    void readFileToByteArray(Long id, Consumer<InputStream> inputStreamConsumer);

    /**
     * 根据id查询资源详情
     *
     * @param id 主键id
     * @return 资源详情
     */
    ResourceDetailDTO findDetailById(Long id);
}
