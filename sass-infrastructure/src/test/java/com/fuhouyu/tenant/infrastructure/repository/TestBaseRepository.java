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
package com.fuhouyu.tenant.infrastructure.repository;

import com.fuhouyu.framework.context.Context;
import com.fuhouyu.framework.context.user.DefaultUserDetail;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.tenant.common.utils.SnowflakeIdWorker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

/**
 * <p>
 * 所有测试的存储层的基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/24 20:42
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest
@SpringBootApplication
@MapperScan(basePackages = "com.fuhouyu.tenant.infrastructure.repository.mapper")
@TestPropertySource(locations = {"classpath:application.yaml"})
class TestBaseRepository {

    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker();

    @BeforeAll
    static void setUp() {
        Context<User> emptyContext = UserContextHolder.createEmptyContext();
        DefaultUserDetail defaultUserDetail = new DefaultUserDetail();
        defaultUserDetail.setUsername("admin");
        emptyContext.setObject(defaultUserDetail);
    }

    protected Long nextSnowflakeId() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    protected String getUUIDStr(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
