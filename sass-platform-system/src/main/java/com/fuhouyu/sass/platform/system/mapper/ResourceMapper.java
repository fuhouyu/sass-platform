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
package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourcePageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 资源mapper对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:27
 */
public interface ResourceMapper extends BaseMapper<Long, Resources> {


    /**
     * 根据etag查询资源
     *
     * @param etag etag
     * @return 资源对象
     */
    @TenantQuery
    Resources queryByEtag(@Param("etag") String etag);

    /**
     * 批量查询
     *
     * @param pageQuery 分页查询对象
     * @return 批量查询
     */
    @TenantQuery
    List<Resources> queryList(@Param("pageQuery") ResourcePageQueryDTO pageQuery);

    /**
     * 通过对象key查询
     *
     * @param objectKey 对象key
     * @return 资源
     */
    @TenantQuery
    Resources queryByObjectKey(@Param("objectKey") String objectKey);

    /**
     * 查询出所有的匹配前缀
     *
     * @param prefixList 前缀集合
     * @return 资源集合
     */
    @TenantQuery
    List<Resources> queryByPrefixList(@Param("prefixList") Collection<String> prefixList);

    /**
     * 资源总数
     *
     * @return 资源总数
     */
    @TenantQuery
    Integer countObjects();
}
