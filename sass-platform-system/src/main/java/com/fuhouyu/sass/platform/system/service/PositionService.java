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
package com.fuhouyu.sass.platform.system.service;

import com.fuhouyu.sass.platform.system.dto.position.PositionDTO;

import java.util.List;

/**
 * <p>
 * 职位接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:29
 */
public interface PositionService extends BaseService<Long, PositionDTO> {

    /**
     * 检查职位是否存在
     *
     * @param positionCode 职位编码
     * @return 职位信息
     */
    Boolean checkPositionExists(String positionCode);

    /**
     * 查询岗位列表集合
     *
     * @return 岗位列表集合
     */
    List<PositionDTO> findPositionAllList();

}
