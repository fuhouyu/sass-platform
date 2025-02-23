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
import com.fuhouyu.sass.platform.system.dto.resource.SaveResourceDTO;
import com.fuhouyu.sass.platform.system.dto.resource.StsTemporaryTokenDTO;
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
     * 预览资源
     *
     * @param id       资源id
     * @param request  请求对象
     * @param response 响应对象
     */
    void previewResource(Long id,
                         HttpServletRequest request,
                         HttpServletResponse response);

    /**
     * 生成sts临时token
     *
     * @return sts临时token
     */
    StsTemporaryTokenDTO generateToken();

    /**
     * 保存资源详情
     *
     * @param resourceDTO 资源dto对象
     * @return 资源id
     */
    Long saveResource(SaveResourceDTO resourceDTO);

    /**
     * 根据etag查询资源
     *
     * @param etag etag
     * @return 资源对象
     */
    ResourceDTO findResourceByEtag(String etag);
}
