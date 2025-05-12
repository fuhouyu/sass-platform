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
import com.fuhouyu.sass.platform.system.assembler.ApplicationsAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationDTO;
import com.fuhouyu.sass.platform.system.domain.dto.application.ApplicationPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Applications;
import com.fuhouyu.sass.platform.system.mapper.ApplicationMapper;
import com.fuhouyu.sass.platform.system.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 应用实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 20:34
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Applications> implements ApplicationService {

    private static final ApplicationsAssembler APPLICATIONS_ASSEMBLER = ApplicationsAssembler.INSTANCE;

    private final ApplicationMapper applicationMapper;

    @Override
    public String save(ApplicationDTO dto) {
        Applications applications = APPLICATIONS_ASSEMBLER.toEntity(dto);
        applicationMapper.insert(applications);
        return applications.getClientId();
    }

    @Override
    public void edit(ApplicationDTO dto) {
        this.applicationMapper.updateById(APPLICATIONS_ASSEMBLER.toEntity(dto));
    }

    @Override
    public ApplicationDTO findById(String clientId) {
        return APPLICATIONS_ASSEMBLER.toDTO(this.applicationMapper.selectById(clientId));
    }

    @Override
    public PageResultDTO<ApplicationDTO> pageList(ApplicationPageQueryDTO queryDTO) {
        LambdaQueryWrapper<Applications> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(queryDTO.getClientName()), Applications::getClientName, queryDTO.getClientName());
        return PageResultDTO.buildPageResult(this.applicationMapper.selectPage(queryDTO, lambdaQueryWrapper), APPLICATIONS_ASSEMBLER::toDTO);
    }


    @Override
    public String generateClientSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
