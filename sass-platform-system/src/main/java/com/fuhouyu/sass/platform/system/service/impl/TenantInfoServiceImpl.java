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
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.token.OAuth2Token;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.TenantInfoAssembler;
import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDetailDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantSpaceDTO;
import com.fuhouyu.sass.platform.system.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.entity.TenantInfo;
import com.fuhouyu.sass.platform.system.enums.TenantEventEnum;
import com.fuhouyu.sass.platform.system.listener.TenantEvent;
import com.fuhouyu.sass.platform.system.mapper.TenantInfoMapper;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    private final TenantHasUserService tenantHasUserService;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TokenStore tokenStore;

    private final PermissionService permissionService;

    private final OrganizationService organizationService;

    private final TenantSpaceService tenantSpaceService;

    @Override
    public Long save(TenantInfoDTO tenantInfoDTO) {
        TenantInfo existsTenant = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.nonNull(existsTenant)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "租户编码:%s 已存在", existsTenant.getTenantCode());
        }
        long id = snowflakeIdWorker.nextId();
        TenantInfo entity = TENANTS_ASSEMBLER.toEntity(tenantInfoDTO);
        entity.setId(id);
        tenantInfoMapper.insert(entity);

        tenantInfoDTO.setId(id);
        this.applicationEventPublisher.publishEvent(new TenantEvent(tenantInfoDTO, TenantEventEnum.CREATE));
        return id;
    }

    @Override
    public void edit(TenantInfoDTO tenantInfoDTO) {
        TenantInfo tenantInfo = tenantInfoMapper.queryByTenantCode(tenantInfoDTO.getTenantCode());
        if (Objects.isNull(tenantInfo)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "租户: %s 不存在", tenantInfoDTO.getTenantCode());
        }
        this.tenantInfoMapper.update(TENANTS_ASSEMBLER.toEntity(tenantInfoDTO));
        this.applicationEventPublisher.publishEvent(new TenantEvent(tenantInfoDTO, TenantEventEnum.UPDATE));
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
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前登录的租户不允许删除操作！");
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
        TenantInfoDTO result = TENANTS_ASSEMBLER.toDTO(tenantInfo);
        result.setPermissionIds(this.tenantHasPermissionService.findPermissionIdByTenantId(id));
        return result;
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
    public List<TenantInfoDTO> findTenantByUserId(Long userId) {
        return TENANTS_ASSEMBLER.toDTO(this.tenantInfoMapper.queryByUserId(userId));
    }

    @Override
    public void switchTenant(Long id) {
        this.checkUserTenantExists(id);
        // 切换租户
        String userToken = ContextHolderStrategy
                .getContext()
                .getRequest()
                .getAuthorization()
                .replace(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
        Collection<? extends GrantedAuthority> simpleGrantedAuthorities = this.permissionService.findUserSimpleGrantedAuthorities(id, ContextHolderStrategy.getContext().getUser().getId());

        Authentication authentication = tokenStore.readAuthentication(userToken);
        AdminUserDTO userDetailsDTO = (AdminUserDTO) authentication.getDetails();
        userDetailsDTO.setTenantId(id);

        OAuth2Token auth2Token = tokenStore.readAuth2Token(userToken);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), simpleGrantedAuthorities);
        usernamePasswordAuthenticationToken.setDetails(userDetailsDTO);
        this.tokenStore.storeAuth2Token(auth2Token, usernamePasswordAuthenticationToken);
        this.tokenStore.storeRefreshToken(auth2Token.getRefreshToken(), usernamePasswordAuthenticationToken);
    }

    @Override
    public TenantInfoDetailDTO findDetailById(Long id) {
        TenantInfoDetailDTO tenantInfoDetailDTO = this.tenantInfoMapper.queryDetailById(id);
        tenantInfoDetailDTO.setPermissionIds(this.tenantHasPermissionService.findPermissionIdByTenantId(id));
        return tenantInfoDetailDTO;
    }

    @Override
    public Long saveTenantDetail(TenantInfoDetailDTO tenantInfoDTO) {
        Long id = this.save(tenantInfoDTO);
        TenantSpaceDTO tenantSpaceDTO = tenantInfoDTO.getTenantSpace();
        tenantSpaceDTO.setTenantId(id);
        this.tenantSpaceService.saveTenantSpace(tenantSpaceDTO);
        return id;
    }

    @Override
    public void editDetail(TenantInfoDetailDTO tenantInfoDTO) {
        this.edit(tenantInfoDTO);
        TenantSpaceDTO tenantSpace = tenantInfoDTO.getTenantSpace();
        tenantSpace.setTenantId(tenantInfoDTO.getId());
        this.tenantSpaceService.editTenantSpace(tenantSpace);
    }

    /**
     * 检查用户当前是否可以访问该租户，不能访问则抛出异常
     *
     * @param id 主键id
     */
    private void checkUserTenantExists(Long id) {
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        Integer count = this.tenantInfoMapper.existsUserTenant(userId, id);
        if (Objects.isNull(count) || count == 0) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "用户当前无可访问该租户的权限");
        }
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
        this.tenantHasUserService.removeByTenantIds(tenantIds);
        this.organizationService.removeOrganizationByTenantIds(tenantIds);
    }
}
