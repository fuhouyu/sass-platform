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
package com.fuhouyu.sass.infrastructure.repository.impl;

import com.fuhouyu.sass.common.PageQuery;
import com.fuhouyu.sass.common.PageResult;
import com.fuhouyu.sass.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.domain.repository.UserRepository;
import com.fuhouyu.sass.infrastructure.repository.assembler.UserAssembler;
import com.fuhouyu.sass.infrastructure.repository.mapper.UserMapper;
import com.fuhouyu.sass.infrastructure.repository.orm.UserDO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户存储层实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/28 16:26
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final UserAssembler USER_ASSEMBLER = UserAssembler.INSTANCE;
    private final UserMapper userMapper;
    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public UserEntity findByUsername(String username) {
        UserDO userDO = this.userMapper.queryByUsername(username);
        return USER_ASSEMBLER.toEntity(userDO);
    }

    @Override
    public UserEntity findById(Long id) {
        UserDO userDO = this.userMapper.queryById(id);
        return USER_ASSEMBLER.toEntity(userDO);
    }

    @Override
    public int removeById(Long id) {
        return this.userMapper.deleteById(id);
    }

    @Override
    public void save(UserEntity entity) {
        UserDO userDO = USER_ASSEMBLER.toDO(entity);
        userDO.setId(snowflakeIdWorker.nextId());
        this.userMapper.insert(userDO);
    }

    @Override
    public void edit(UserEntity entity) {
        UserDO userDO = USER_ASSEMBLER.toDO(entity);
        this.userMapper.update(userDO);
    }

    @Override
    public <P extends PageQuery> PageResult<UserEntity> pageList(P pageable) {
        try (Page<Object> result = PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize())) {
            List<UserDO> userDOList = this.userMapper.queryList(pageable);
            List<UserEntity> modelList = USER_ASSEMBLER.toEntity(userDOList);
            return new PageResult<>(pageable.getPageNumber(), pageable.getPageSize(), result.getTotal(), modelList);
        }
    }
}
