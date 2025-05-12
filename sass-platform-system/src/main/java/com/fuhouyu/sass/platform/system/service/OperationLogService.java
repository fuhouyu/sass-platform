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

import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogDTO;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;

import java.util.List;

/**
 * <p>
 * 操作日志接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:42
 */
public interface OperationLogService {

    /**
     * 分页查询日志
     *
     * @param operationLogPageQueryDTO 日志查询的dto对象
     * @return 查询的分页对象
     */
    PageResultDTO<OperationLogDTO> pageList(OperationLogPageQueryDTO operationLogPageQueryDTO);

    /**
     * 获取模块列表
     *
     * @return 模块列表
     */
    List<String> getModuleList();

    /**
     * 通过id查询日志详情
     *
     * @param id 主键id
     * @return 操作日志详情
     */
    OperationLogDTO findById(Long id);
}
