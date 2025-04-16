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

import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * ${describe}
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:25
 */
public interface OperationLogMapper {

    /**
     * 插入日志
     *
     * @param record 日志记录
     */
    void insert(OperationLog record);

    /**
     * 查询列表
     *
     * @param pageQuery 分页查询
     * @return 操作日志集合
     */
    @TenantQuery
    List<OperationLog> queryList(@Param("pageQuery") OperationLogPageQueryDTO pageQuery);

    /**
     * 获取模块列表
     *
     * @return 模块列表
     */
    List<String> getModuleList();

    /**
     * 通过id查询出操作日志
     *
     * @param id 主键id
     * @return 操作日志
     */
    @TenantQuery
    OperationLog queryById(Long id);
}