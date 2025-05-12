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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.common.utils.TreeConvertUtil;
import com.fuhouyu.sass.platform.system.assembler.OrganizationsAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationDTO;
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.organization.OrganizationTreeDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Organizations;
import com.fuhouyu.sass.platform.system.enums.response.OrganizationResponseStatusEnums;
import com.fuhouyu.sass.platform.system.mapper.OrganizationMapper;
import com.fuhouyu.sass.platform.system.service.OrganizationService;
import com.fuhouyu.sass.platform.system.service.UserPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * <p>
 * 组织实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 22:28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organizations> implements OrganizationService {

    private static final OrganizationsAssembler ORGANIZATIONS_ASSEMBLER = OrganizationsAssembler.INSTANCE;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final OrganizationMapper organizationMapper;

    private final UserPositionService userPositionService;

    @Override
    public long save(OrganizationDTO dto) {
        if (Boolean.TRUE.equals(this.checkOrganizationCodeExists(dto.getOrganizationCode()))) {
            throw new ServiceException(OrganizationResponseStatusEnums.ORGANIZATION_CODE_EXISTS);
        }
        long id = snowflakeIdWorker.nextId();
        Organizations entity = ORGANIZATIONS_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        entity.setIsLeaf(true);
        // TODO 这里的组织类型先做固定
        entity.setOrganizationType("UNIT");
        entity.setOrganizationCode(String.format("GO_%s", UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT)));
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        Long parentId = Optional.ofNullable(dto.getParentId()).orElse(-1L);
        this.checkParentExists(parentId);
        entity.setParentId(parentId);
        this.organizationMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(OrganizationDTO dto) {
        Organizations entity = ORGANIZATIONS_ASSEMBLER.toEntity(dto);
        this.organizationMapper.updateById(entity);
    }


    @Override
    public Integer deleteByIds(Collection<Long> ids) {
        List<Organizations> organizations = this.organizationMapper.selectByIds(ids);
        if (CollectionUtils.isEmpty(organizations)) {
            return 0;
        }

        List<Long> parentIdList = organizations.stream()
                .map(Organizations::getParentId)
                .filter(parentId -> !Objects.equals(parentId, -1L))
                .toList();
        int deleteCount = this.organizationMapper.deleteByIds(ids);
        if (!CollectionUtils.isEmpty(parentIdList)) {
            // 修改isLeaf
            this.organizationMapper.setLeafByIdList(parentIdList);
        }
        return deleteCount;
    }

    @Override
    public OrganizationDTO findById(Long id) {
        Organizations organizations = this.organizationMapper.queryById(id);
        return ORGANIZATIONS_ASSEMBLER.toDTO(organizations);
    }

    @Override
    public PageResultDTO<OrganizationDTO> pageList(OrganizationPageQueryDTO pageQuery) {
        LambdaQueryWrapper<Organizations> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Organizations::getParentId, pageQuery.getParentId());
        lambdaQueryWrapper.like(StringUtils.hasText(pageQuery.getOrganizationName()), Organizations::getOrganizationName, pageQuery.getOrganizationName());

        return PageResultDTO.buildPageResult(this.organizationMapper.selectPage(pageQuery, lambdaQueryWrapper), ORGANIZATIONS_ASSEMBLER::toDTO);
    }

    /**
     * 检查编码是否存在
     *
     * @param organizationCode 组织编码
     * @return 是否存在 true存在 false 不存在
     */
    @Override
    public Boolean checkOrganizationCodeExists(String organizationCode) {
        Integer count = this.organizationMapper.existsOrganizationCode(organizationCode);
        return count != null && count > 0;
    }

    @Override
    public List<OrganizationDTO> getOrganizationList(Long parentId) {
        return ORGANIZATIONS_ASSEMBLER.toDTO(this.organizationMapper.queryListByParentId(parentId));
    }

    @Override
    public List<OrganizationTreeDTO> getTreeList() {
        List<Organizations> organizationList = this.organizationMapper.queryList(new OrganizationPageQueryDTO());
        return TreeConvertUtil.buildTree(ORGANIZATIONS_ASSEMBLER.toTreeDTOList(organizationList));
    }

    @Override
    public Long createTenantDefaultOrganization(TenantInfoDTO tenantInfoDTO) {
        Organizations organizations = new Organizations();
        long id = snowflakeIdWorker.nextId();
        organizations.setId(id);
        organizations.setParentId(-1L);
        organizations.setOrganizationName(tenantInfoDTO.getTenantName());
        organizations.setOrganizationType("UNIT");
        organizations.setOrganizationCode(String.format("GO_%s", UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT)));
        organizations.setIsEnabled(true);
        organizations.setIsLeaf(true);
        organizations.setRemark("组织配置");
        organizations.setDisplayOrder(1);
        organizations.setOwnerTenantId(tenantInfoDTO.getId());
        this.organizationMapper.insert(organizations);
        return id;
    }


    @Override
    public void removeOrganizationByTenantIds(Collection<Long> tenantIds) {
        this.userPositionService.removeByTenantIds(tenantIds);
        this.organizationMapper.deleteByTenantIds(tenantIds);
    }

    /**
     * 检查父级权限是否存在，
     * 如果父级存在，将会修改父级的isLeaf为false
     *
     * @param parentId 父级权限
     */
    private void checkParentExists(Long parentId) {
        // 如果为-1，则是一级菜单不进行验证，否则进行校验
        if (Objects.equals(-1L, parentId)) {
            return;
        }
        Organizations parentOrganization = this.organizationMapper.queryById(parentId);
        if (Objects.isNull(parentOrganization)) {
            throw new ServiceException(OrganizationResponseStatusEnums.PARENT_ORGANIZATION_NOT_EXISTS);
        }
        // 如果当前父级为叶子节点，进行修改
        if (parentOrganization.getIsLeaf()) {
            this.organizationMapper.updateLeafById(false, parentId);
        }
    }
}
