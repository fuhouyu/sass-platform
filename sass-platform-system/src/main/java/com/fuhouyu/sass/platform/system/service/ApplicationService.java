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
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationDTO;
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Applications;

/**
 * <p>
 * 应用接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 20:32
 */
public interface ApplicationService extends IService<Applications> {


    /**
     * 保存应用信息
     *
     * @param applicationDTO 应用信息
     * @return 应用id
     */
    String save(ApplicationDTO applicationDTO);

    /**
     * 编辑应用信息
     *
     * @param applicationDTO 应用信息
     */
    void edit(ApplicationDTO applicationDTO);

    /**
     * 查询应用信息
     *
     * @param clientId 应用id
     */
    ApplicationDTO findById(String clientId);

    /**
     * 分页查询应用信息
     *
     * @param queryDTO 查询的dto对象
     * @return 分页查询结果
     */
    PageResultDTO<ApplicationDTO> pageList(ApplicationPageQueryDTO queryDTO);

    /**
     * 生成客户端密钥
     *
     * @return 客户端密钥
     */
    String generateClientSecret();

}
