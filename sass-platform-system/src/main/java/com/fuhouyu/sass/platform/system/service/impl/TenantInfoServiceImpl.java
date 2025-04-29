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

import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.TenantInfoAssembler;
import com.fuhouyu.sass.platform.system.components.listener.TenantEvent;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.*;
import com.fuhouyu.sass.platform.system.domain.entity.TenantInfo;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.TenantEventEnum;
import com.fuhouyu.sass.platform.system.enums.response.TenantResponseStatusEnum;
import com.fuhouyu.sass.platform.system.mapper.TenantInfoMapper;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * 租户领域模型
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:55
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantInfoServiceImpl implements TenantInfoService {

    private static final TenantInfoAssembler TENANTS_ASSEMBLER = TenantInfoAssembler.INSTANCE;

    private final TenantInfoMapper tenantInfoMapper;

    private final RoleService roleService;

    private final TenantHasPermissionService tenantHasPermissionService;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final PermissionService permissionService;

    private final OrganizationService organizationService;

    private final TenantSpaceService tenantSpaceService;

    private final AdminUserService adminUserService;

    private final AccountService accountService;

    private final ParamConfigService paramConfigService;

    @Override
    public Long save(TenantInfoDTO tenantInfoDTO) {
        TenantInfo existsTenant = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.nonNull(existsTenant)) {
            throw new ServiceException(TenantResponseStatusEnum.TENANT_CODE_ALREADY_EXISTS);
        }
        long id = snowflakeIdWorker.nextId();
        TenantInfo entity = TENANTS_ASSEMBLER.toEntity(tenantInfoDTO);
        entity.setId(id);
        tenantInfoMapper.insert(entity);
        tenantInfoDTO.setId(id);
        return id;
    }

    @Override
    public void edit(TenantInfoDTO tenantInfoDTO) {
        TenantInfo tenantInfo = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.isNull(tenantInfo)) {
            throw new ServiceException(TenantResponseStatusEnum.TENANT_NOT_EXISTS);
        }
        this.tenantInfoMapper.update(TENANTS_ASSEMBLER.toEntity(tenantInfoDTO));
    }

    @Override
    public int removeById(Long id) {
        int count = this.tenantInfoMapper.deleteById(id);
        this.doRemoveTenantAttach(List.of(id));
        return count;
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        if (ids.contains(tenantId)) {
            throw new ServiceException(TenantResponseStatusEnum.TENANT_NO_PERMISSION);
        }
        this.doRemoveTenantAttach(ids);
        int count = this.tenantInfoMapper.deleteByIds(ids);
        this.tenantSpaceService.removeSpaceByTenantIds(ids);
        return count;

    }

    @Override
    public TenantInfoDTO findById(Long id) {
        TenantInfo tenantInfo = this.tenantInfoMapper.queryById(id);
        if (Objects.isNull(tenantInfo)) {
            return null;
        }
        TenantInfoDetailDTO tenantInfoDetail = TENANTS_ASSEMBLER.toTenantInfoDetail(tenantInfo);
        tenantInfoDetail.setPermissionIds(this.tenantHasPermissionService.findPermissionIdByTenantId(id));
        return tenantInfoDetail;
    }

    @Override
    public Function<PageQueryDTO, List<TenantInfoDTO>> getPageResult() {
        return p -> TENANTS_ASSEMBLER.toDTO(this.tenantInfoMapper.queryList(p));
    }

    @Override
    public TenantInfoDTO findByTenantCode(String tenantCode) {
        return TENANTS_ASSEMBLER.toDTO(this.tenantInfoMapper.queryByTenantCode(tenantCode));
    }

    @Override
    public TenantInfoDetailDTO findDetailById(Long id) {
        TenantInfo tenantInfo = this.tenantInfoMapper.queryById(id);
        TenantInfoDetailDTO tenantInfoDetail = TENANTS_ASSEMBLER.toTenantInfoDetail(tenantInfo);
        tenantInfoDetail.setPermissionIds(this.tenantHasPermissionService.findPermissionIdByTenantId(id));
        return tenantInfoDetail;
    }

    @Override
    public Long saveTenantDetail(SaveOrEditTenantInfoDTO tenantInfoDTO) {
        Long id = this.save(tenantInfoDTO);
        TenantSpaceDTO tenantSpaceDTO = tenantInfoDTO.getTenantSpace();
        tenantSpaceDTO.setTenantId(id);
        this.tenantSpaceService.saveTenantSpace(tenantSpaceDTO);
        this.applicationEventPublisher.publishEvent(new TenantEvent(tenantInfoDTO, TenantEventEnum.CREATE));
        return id;
    }

    @Override
    public void editDetail(SaveOrEditTenantInfoDTO tenantInfoDTO) {
        this.edit(tenantInfoDTO);
        TenantSpaceDTO tenantSpace = tenantInfoDTO.getTenantSpace();
        tenantSpace.setTenantId(tenantInfoDTO.getId());
        this.tenantSpaceService.editTenantSpace(tenantSpace);
        this.applicationEventPublisher.publishEvent(new TenantEvent(tenantInfoDTO, TenantEventEnum.UPDATE));
    }


    @Override
    public List<BasicTenantDTO> findTenantList() {
        List<TenantInfo> list = this.tenantInfoMapper.queryEffectiveTenantList(LocalDate.now());
        return list.stream().map(res -> BasicTenantDTO.builder()
                .id(res.getId())
                .tenantName(res.getTenantName())
                .tenantCode(res.getTenantCode())
                .icon(res.getIcon()).build()).toList();
    }

    @Override
    public void resetPassword(Long id) {
        TenantInfo tenantInfo = this.tenantInfoMapper.queryById(id);
        if (Objects.isNull(tenantInfo)) {
            throw new ServiceException(TenantResponseStatusEnum.TENANT_NOT_EXISTS);
        }
        String contactPerson = tenantInfo.getContactPerson();
        AccountDTO accountDTO = this.accountService.findById(new AccountIdDTO(id, contactPerson, AccountTypeEnum.PASSWORD));
        List<ParamConfigDTO> paramConfigList = this.paramConfigService.findListByGroupKey("TENANT");
        Optional<ParamConfigDTO> optional = paramConfigList.stream()
                .filter(config -> Objects.equals(config.getConfigKey(), "DEFAULT_PASSWORD"))
                .findAny();
        if (optional.isEmpty()) {
            accountDTO.setCredentials("Aa123123..");
        } else {
            accountDTO.setCredentials(optional.get().getConfigValue());
        }
        accountDTO.encodeCredentials();
        this.accountService.edit(accountDTO);
    }

    /**
     * 删除租户关联的信息
     *
     * @param tenantIds 租户id集合
     */
    private void doRemoveTenantAttach(Collection<Long> tenantIds) {
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        this.tenantHasPermissionService.removeTenantPermissions(tenantIds);
        this.roleService.removeByTenantIds(tenantIds);
        this.permissionService.removeByTenantIds(tenantIds);
        this.organizationService.removeOrganizationByTenantIds(tenantIds);
        this.adminUserService.removeByTenantIds(tenantIds);
    }
}
