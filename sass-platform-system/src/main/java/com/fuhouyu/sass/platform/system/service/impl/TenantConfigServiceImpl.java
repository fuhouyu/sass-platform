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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.TenantConfigAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantConfigDTO;
import com.fuhouyu.sass.platform.system.entity.TenantConfig;
import com.fuhouyu.sass.platform.system.mapper.TenantConfigMapper;
import com.fuhouyu.sass.platform.system.service.TenantConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 租户配置实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/19 21:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantConfigServiceImpl implements TenantConfigService {

    private static final TenantConfigAssembler TENANT_CONFIG_ASSEMBLER = TenantConfigAssembler.INSTANCE;

    private final TenantConfigMapper tenantConfigMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Long save(TenantConfigDTO dto) {
        long id = snowflakeIdWorker.nextId();
        TenantConfig entity = TENANT_CONFIG_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        tenantConfigMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(TenantConfigDTO dto) {
        TenantConfig entity = TENANT_CONFIG_ASSEMBLER.toEntity(dto);
        this.tenantConfigMapper.update(entity);
    }

    @Override
    public int removeById(Long id) {
        return this.tenantConfigMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.tenantConfigMapper.deleteByIds(ids);
    }

    @Override
    public TenantConfigDTO findById(Long id) {
        return TENANT_CONFIG_ASSEMBLER.toDTO(this.tenantConfigMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<TenantConfigDTO>> getPageResult() {
        return (p) -> TENANT_CONFIG_ASSEMBLER.toDTO(this.tenantConfigMapper.queryList(p));
    }
}
