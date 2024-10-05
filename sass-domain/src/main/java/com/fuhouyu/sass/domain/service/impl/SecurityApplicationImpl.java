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
package com.fuhouyu.sass.domain.service.impl;

import com.fuhouyu.framework.security.core.AbstractApplicationManager;
import com.fuhouyu.framework.security.model.dto.ApplicationDTO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 11:00
 */
@Service
public class SecurityApplicationImpl extends AbstractApplicationManager {

    @Override
    public ApplicationDTO queryApplication(String applicationId) {
        // 先直接返回
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setClientId("test");
        applicationDTO.setClientSecret("password");
        applicationDTO.setAccessTokenExpireTime(6000);
        applicationDTO.setRefreshTokenExpireTime(6000);
        return applicationDTO;
    }
}
