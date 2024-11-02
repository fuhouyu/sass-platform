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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.sass.platform.common.enums.ResponseCodeEnum;
import com.fuhouyu.sass.platform.common.exception.ServiceException;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.assembler.UsersAssembler;
import com.fuhouyu.sass.platform.system.dto.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.UserDTO;
import com.fuhouyu.sass.platform.system.entity.Users;
import com.fuhouyu.sass.platform.system.mapper.UserMapper;
import com.fuhouyu.sass.platform.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 用户接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:19
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final UsersAssembler USERS_ASSEMBLER = UsersAssembler.INSTANCE;
    private final UserMapper userMapper;
    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public void save(UserDTO userDTO) {
        userDTO.setId(snowflakeIdWorker.nextId());
        this.userMapper.insert(USERS_ASSEMBLER.toEntity(userDTO));
    }

    @Override
    public UserDTO findByUsername(String username) {
        return USERS_ASSEMBLER.toDTO(this.userMapper.queryByUsername(username));
    }

    @Override
    public void recordLoginSuccess(Long userId) {
        Users users = new Users();
        users.setId(userId);
        users.setLoginIp(ContextHolderStrategy.getContext().getRequest().getRequestIp());
        users.setLoginDate(LocalDateTime.now());
        this.userMapper.update(users);
    }

    @Override
    public UserDTO findById(Long userId) {
        Users users = this.userMapper.queryById(userId);
        return USERS_ASSEMBLER.toDTO(users);
    }

    @Override
    public void edit(UserDTO userDTO) {
        this.userMapper.update(USERS_ASSEMBLER.toEntity(userDTO));
    }

    @Override
    public void saveBatch(List<UserDTO> dtoList) {
        List<Users> list = dtoList.stream().map(dto -> {
            dto.setId(snowflakeIdWorker.nextId());
            return USERS_ASSEMBLER.toEntity(dto);
        }).toList();
        this.userMapper.insertBatch(list);
    }

    @Override
    public int removeById(Long id) {
        User user = ContextHolderStrategy.getContext().getUser();
        if (id.equals(user.getId())) {
            throw new ServiceException(ResponseCodeEnum.INVALID_PARAM,
                    "不允许操作当前登录账号: %s", user.getUsername());
        }
        return this.userMapper.deleteById(id);
    }

    @Override
    public int removeByIds(Collection<Long> ids) {
        User user = ContextHolderStrategy.getContext().getUser();
        if (ids.contains(user.getId())) {
            throw new ServiceException(ResponseCodeEnum.INVALID_PARAM,
                    "不允许操作当前登录账号: %s", user.getUsername());
        }
        return this.userMapper.deleteByIds(ids);
    }

    @Override
    public Function<PageQueryDTO, List<UserDTO>> getPageResult() {
        return (pageQuery) -> USERS_ASSEMBLER.toDTO(this.userMapper.queryList(pageQuery));
    }
}
