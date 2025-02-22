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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.assembler.TenantSpaceAssembler;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.entity.TenantSpace;
import com.fuhouyu.sass.platform.system.mapper.TenantSpaceMapper;
import com.fuhouyu.sass.platform.system.service.TenantSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 租户空间实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:58
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TenantSpaceSpaceServiceImpl implements TenantSpaceService {

    private static final TenantSpaceAssembler TENANT_SPACE_ASSEMBLER = TenantSpaceAssembler.INSTANCE;

    private final TenantSpaceMapper tenantSpaceMapper;

    @Override
    public TenantSpaceDTO findByTenantId(Long tenantId) {
        return TENANT_SPACE_ASSEMBLER.toDTO(this.tenantSpaceMapper.queryById(tenantId));
    }

    @Override
    public TenantSpaceDTO checkExists(Long tenantId) {
        TenantSpace tenantSpace = this.tenantSpaceMapper.queryById(tenantId);
        if (Objects.isNull(tenantSpace)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前租户空间不存在");
        }
        return TENANT_SPACE_ASSEMBLER.toDTO(tenantSpace);
    }
}
