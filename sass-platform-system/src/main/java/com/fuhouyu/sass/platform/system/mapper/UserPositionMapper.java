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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.sass.platform.system.entity.UserPositions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户职位mapper
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/11 18:21
 */
public interface UserPositionMapper {

    /**
     * 插入用户职位
     *
     * @param userPositions 用户职位对象
     */
    void insert(UserPositions userPositions);

    /**
     * 批量插入用户职位
     *
     * @param list 用户职位列表
     */
    void insertBatch(@Param("list") List<UserPositions> list);

}
