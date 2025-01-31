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

import com.fuhouyu.sass.platform.system.assembler.UserPositionAssembler;
import com.fuhouyu.sass.platform.system.dto.user.UserPositionDTO;
import com.fuhouyu.sass.platform.system.mapper.UserPositionMapper;
import com.fuhouyu.sass.platform.system.service.UserPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 * 用户职位接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/11 12:25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserPositionServiceImpl implements UserPositionService {

    private static final UserPositionAssembler USER_POSITION_ASSEMBLER = UserPositionAssembler.INSTANCE;

    private final UserPositionMapper userPositionMapper;

    @Override
    public void saveUserPosition(Long userId, UserPositionDTO userPositionDTO) {
        userPositionDTO.setUserId(userId);
        this.userPositionMapper.insert(USER_POSITION_ASSEMBLER.toEntity(userPositionDTO));
    }

    @Override
    public void removeByUserIds(Collection<Long> userIds) {
        this.userPositionMapper.deleteByUserIds(userIds);
    }

    @Override
    public Long removeByOrganizationIdAndUserIds(Long organizationId, Collection<Long> userIds) {
        return this.userPositionMapper.deleteByOrganizationIdAndUserIds(organizationId, userIds);
    }
}
