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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.UserAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Users;
import com.fuhouyu.sass.platform.system.mapper.UserMapper;
import com.fuhouyu.sass.platform.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * <p>
 * 用户接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 17:46
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final UserAssembler USERS_ASSEMBLER = UserAssembler.INSTANCE;

    private static final String USERNAME_PREFIX = "sa_";

    private final UserMapper userMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public String generateUsername() {
        return USERNAME_PREFIX + UUID.randomUUID().toString().replace("-", "").substring(9);
    }

    @Override
    public void recordLoginSuccess(Long userId) {
        this.userMapper.recordLoginSuccess(userId, ContextHolderStrategy.getContext().getRequest().getRequestIp(),
                LocalDateTime.now());
    }

    @Override
    public Long save(UserDTO dto) {
        long id = snowflakeIdWorker.nextId();
        Users entity = USERS_ASSEMBLER.toEntity(dto);
        entity.setId(id);
        this.userMapper.insert(entity);
        return id;
    }

    @Override
    public void edit(UserDTO dto) {
        this.userMapper.update(USERS_ASSEMBLER.toEntity(dto));
    }

    @Override
    public int removeById(Long id) {
        return this.userMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        return this.userMapper.deleteByIds(ids);
    }

    @Override
    public UserDTO findById(Long id) {
        return USERS_ASSEMBLER.toDTO(this.userMapper.queryById(id));
    }

    @Override
    public Function<PageQueryDTO, List<UserDTO>> getPageResult() {
        return p -> USERS_ASSEMBLER.toDTO(this.userMapper.queryList(p));
    }
}
