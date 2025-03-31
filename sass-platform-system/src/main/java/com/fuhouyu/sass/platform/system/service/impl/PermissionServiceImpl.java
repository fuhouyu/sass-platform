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
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.common.utils.TreeConvertUtil;
import com.fuhouyu.sass.platform.system.assembler.PermissionAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionDTO;
import com.fuhouyu.sass.platform.system.domain.dto.permission.PermissionTreeDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Permissions;
import com.fuhouyu.sass.platform.system.mapper.PermissionMapper;
import com.fuhouyu.sass.platform.system.service.PermissionService;
import com.fuhouyu.sass.platform.system.service.RoleHasPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * permission实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:56
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private static final PermissionAssembler PERMISSION_ASSEMBLER = PermissionAssembler.INSTANCE;

    private final PermissionMapper permissionMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    private final RoleHasPermissionService roleHasPermissionService;


    @Override
    public PermissionDTO findByPermissionCode(String permissionCode) {
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryByPermissionCode(permissionCode));
    }

    @Override
    public List<PermissionTreeDTO> findPermissionListByMe() {
        User user = ContextHolderStrategy.getContext().getUser();
        Long userId = user.getId();
        List<Permissions> list = this.permissionMapper.queryUserPermissonList(userId);
        return TreeConvertUtil.buildTree(PERMISSION_ASSEMBLER.toPermissionInfoTreeDTOList(list));
    }

    @Override
    public Long save(PermissionDTO dto) {
        long id = snowflakeIdWorker.nextId();
        String permissionCode = dto.getPermissionCode();
        Permissions permissions = this.permissionMapper.queryByPermissionCode(permissionCode);
        if (Objects.nonNull(permissions)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "权限编码:%s 已存在", permissionCode);
        }

        Permissions entity = PERMISSION_ASSEMBLER.toEntity(dto);
        Long parentId = entity.getParentId();
        this.checkParentPermissionExists(parentId);
        entity.setId(id);
        entity.setIsAllowModified(true);
        entity.setIsLeaf(true);
        entity.setOwnerTenantId(ContextHolderStrategy.getContext().getUser().getTenantId());
        this.permissionMapper.insert(entity);
        return id;
    }


    @Override
    public void saveBatch(List<PermissionDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<Permissions> list = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return PERMISSION_ASSEMBLER.toEntity(dto);
        }).toList();
        this.permissionMapper.insertBatch(list);
    }

    @Override
    public void edit(PermissionDTO dto) {
        this.permissionMapper.update(PERMISSION_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.removeByIds(List.of(id));
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        List<Permissions> permissionsList = this.permissionMapper.queryByIds(ids);
        if (CollectionUtils.isEmpty(permissionsList)) {
            return 0;
        }
        List<Permissions> notAllowModifiedList = permissionsList.stream()
                .filter(p -> !p.getIsAllowModified())
                .toList();
        if (!CollectionUtils.isEmpty(notAllowModifiedList)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    String.format("以下权限: [%s] 禁止删除，请重新选择后重试！",
                            notAllowModifiedList.stream()
                                    .map(Permissions::getPermissionName)
                                    .collect(Collectors.joining(","))));
        }
        List<Long> parentIdList = permissionsList.stream()
                .map(Permissions::getParentId)
                .filter(parentId -> !Objects.equals(parentId, -1L))
                .toList();
        this.roleHasPermissionService.removeByPermissionIds(ids);
        int deleteCount = this.permissionMapper.deleteByIds(ids);
        if (!CollectionUtils.isEmpty(parentIdList)) {
            // 修改isLeaf
            this.permissionMapper.setLeafByIdList(parentIdList);
        }
        return deleteCount;
    }

    @Override
    public PermissionDTO findById(Long id) {
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<PermissionDTO>> getPageResult() {
        return p -> PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryList(p));
    }

    @Override
    public List<PermissionDTO> getPermissionList(Long parentId) {
        Long currentParentId = Optional.ofNullable(parentId).orElse(-1L);
        List<Permissions> list = this.permissionMapper.queryListByParentId(currentParentId);
        list.addAll(this.permissionMapper.queryAttachTenantPermissionListByParentId(currentParentId));
        return PERMISSION_ASSEMBLER.toDTO(list);
    }

    @Override
    public List<PermissionTreeDTO> getTreeList() {
        List<Permissions> permissionsList = this.permissionMapper.queryAll();
        return TreeConvertUtil.buildTree(PERMISSION_ASSEMBLER.toPermissionInfoTreeDTOList(permissionsList));
    }

    @Override
    public Boolean checkPermissionCodeExists(String permissionCode) {
        return Objects.nonNull(this.permissionMapper.queryByPermissionCode(permissionCode));
    }

    @Override
    public List<PermissionDTO> findByIds(Collection<Long> permissionIds) {
        return PERMISSION_ASSEMBLER.toDTO(this.permissionMapper.queryByIds(permissionIds));
    }

    @Override
    public Collection<? extends GrantedAuthority> findUserSimpleGrantedAuthorities(Long tenantId, Long userId) {
        Set<String> permissionCodeList = this.permissionMapper.queryUserPermissionCodeList(tenantId, userId);
        if (CollectionUtils.isEmpty(permissionCodeList)) {
            return Collections.emptySet();
        }
        return permissionCodeList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public void removeByTenantIds(Collection<Long> tenantIds) {
        this.permissionMapper.deleteByTenantIds(tenantIds);
    }

    /**
     * 检查父级是否存在，不存在则抛出异常
     *
     * @param parentId 父级id
     */
    private void checkParentPermissionExists(Long parentId) {
        // 如果为-1，则是一级菜单不进行验证，否则进行校验
        if (Objects.equals(-1L, parentId)) {
            return;
        }
        Permissions parentPermission = this.permissionMapper.queryById(parentId);
        if (Objects.isNull(parentPermission)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "父级权限不存在，请重新选择父级权限");
        }
        // 如果当前父级为叶子节点，进行修改
        if (parentPermission.getIsLeaf()) {
            this.permissionMapper.updateLeafById(false, parentId);
        }
    }
}
