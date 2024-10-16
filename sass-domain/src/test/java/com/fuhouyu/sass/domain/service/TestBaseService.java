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
package com.fuhouyu.sass.domain.service;

import com.fuhouyu.sass.domain.repository.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>
 * 测试基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 19:50
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBootApplication
class TestBaseService {

    @MockBean
    protected AccountRepository accountRepository;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected TenantRepository tenantRepository;

    @MockBean
    protected RoleRepository roleRepository;
    @MockBean
    private PermissionRepository permissionRepository;

}
