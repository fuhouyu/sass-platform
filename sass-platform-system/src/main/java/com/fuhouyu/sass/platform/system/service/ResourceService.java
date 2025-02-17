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

import com.fuhouyu.sass.platform.system.dto.resource.ResourceDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourcePresignedUrlRequestDTO;
import com.fuhouyu.sass.platform.system.dto.resource.ResourcePresignedUrlResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
     * 生成资源上传的url
     *
     * @param resourcePresignedUrlRequestDTO 资源预签名dto对象
     * @return 响应dto对象
     */
    ResourcePresignedUrlResponseDTO generateResourcePresignedUrl(ResourcePresignedUrlRequestDTO resourcePresignedUrlRequestDTO);

    /**
     * 预览资源
     *
     * @param id       资源id
     * @param request  请求对象
     * @param response 响应对象
     */
    void previewResource(Long id,
                         HttpServletRequest request,
                         HttpServletResponse response);
}
