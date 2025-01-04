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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.OrganizationsAssembler;
import com.fuhouyu.sass.platform.system.dto.organization.OrganizationDTO;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.entity.Organizations;
import com.fuhouyu.sass.platform.system.mapper.OrganizationMapper;
import com.fuhouyu.sass.platform.system.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
public class OrganizationServiceImpl implements OrganizationService {

    private static final OrganizationsAssembler ORGANIZATIONS_ASSEMBLER = OrganizationsAssembler.INSTANCE;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final OrganizationMapper organizationMapper;

    @Override
    public Long save(OrganizationDTO dto) {
        if (this.checkOrganizationCodeExists(dto.getOrganizationCode())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "组织编码: %s 已存在，请重新输入", dto.getOrganizationCode());
        }
        long id = snowflakeIdWorker.nextId();
        Organizations entity = ORGANIZATIONS_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        entity.setIsLeaf(true);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        this.checkParentExists(dto.getParentId());
        this.organizationMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(OrganizationDTO dto) {
        Organizations entity = ORGANIZATIONS_ASSEMBLER.toEntity(dto);
        this.organizationMapper.update(entity);
    }

    @Override
    public int removeById(Long id) {
        return 0;
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        List<Organizations> organizations = this.organizationMapper.queryByIds(ids);
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
    public Function<PageQueryDTO, List<OrganizationDTO>> getPageResult() {
        return p -> ORGANIZATIONS_ASSEMBLER.toDTO(this.organizationMapper.queryList(p));
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
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "父级组织不存在，请重新选择");
        }
        // 如果当前父级为叶子节点，进行修改
        if (parentOrganization.getIsLeaf()) {
            this.organizationMapper.updateLeafById(false, parentId);
        }
    }
}
