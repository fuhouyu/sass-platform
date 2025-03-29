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

import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceSignedUrlDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.StsTemporaryTokenRequestDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.StsTemporaryTokenResponseDTO;

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
     * @param resourceSignedUrlDTO 资源签名的dto
     */
    void downloadFile(Long id,
                      ResourceSignedUrlDTO resourceSignedUrlDTO);

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
     * @param preview 是否预览
     * @return 签名的url
     */
    String generateSignedUrl(Long id,
                             Boolean preview);


    /**
     * 检查资源权限
     *
     * @param id 资源id
     * @return 资源dto对象
     */
    ResourceDTO checkResourcePermission(Long id);

    /**
     * 生成一个预签名，直接从oss中下载的url
     *
     * @param id 资源id
     * @return url
     */
    String generatePresignerDownloadUrl(Long id);

}
