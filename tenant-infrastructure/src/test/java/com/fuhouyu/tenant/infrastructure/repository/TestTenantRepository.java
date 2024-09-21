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

import com.fuhouyu.tenant.common.utils.SnowflakeIdWorker;
import com.fuhouyu.tenant.domain.model.BasePageQueryModel;
import com.fuhouyu.tenant.domain.model.PageResultModel;
import com.fuhouyu.tenant.domain.model.TenantModel;
import com.fuhouyu.tenant.domain.repository.TenantRepository;
import com.fuhouyu.tenant.infrastructure.repository.impl.TenantRepositoryImpl;
import com.fuhouyu.tenant.infrastructure.repository.mapper.TenantMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 租户存储层测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:41
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {
        TenantRepositoryImpl.class,

})
@SpringBootApplication
@MapperScan(basePackageClasses = TenantMapper.class)
@TestPropertySource(locations = {"classpath:application.yaml"})
class TestTenantRepository {

    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker();

    @Autowired
    private TenantRepository tenantRepository;


    @Test
    void testTenant() {
        // 保存
        TenantModel tenantModel = this.generateTenantModel(1);
        tenantModel = tenantRepository.insert(tenantModel);
        // 根据id查询
        TenantModel tenantModelQueryById = this.tenantRepository.queryById(tenantModel.getId());
        this.compare(tenantModel, tenantModelQueryById);
        // 根据编码查询
        TenantModel queryByTenantCode = this.tenantRepository.queryByTenantCode(tenantModel.getTenantCode());
        this.compare(tenantModel, queryByTenantCode);
        // 修改
        tenantModel.setTenantName("update_tenant");
        TenantModel update = this.tenantRepository.update(tenantModel);
        this.compare(tenantModel, update);
        // 批量查询
        BasePageQueryModel basePageQueryModel = new BasePageQueryModel(1, 10);
        PageResultModel<TenantModel> list = this.tenantRepository.pageList(basePageQueryModel);
        for (TenantModel model : list.getList()) {
            this.compare(tenantModel, model);
        }
        // 删除
        int count = this.tenantRepository.deleteById(tenantModel.getId());
        Assert.isTrue(count > 0, "数据删除失败");

    }


    private void compare(TenantModel tenantModel, TenantModel tenantModelQueryById) {
        Assert.isTrue(Objects.equals(tenantModel.getTenantCode(), tenantModelQueryById.getTenantCode()), "租户编码不一致");
        Assert.isTrue(Objects.equals(tenantModel.getTenantName(), tenantModelQueryById.getTenantName()), "租户名称不一致");
        Assert.isTrue(Objects.equals(tenantModel.getRemark(), tenantModelQueryById.getRemark()), "租户备注不一致");
        Assert.isTrue(Objects.equals(tenantModel.getContactPerson(), tenantModelQueryById.getContactPerson()), "租户联系人不一致");
        Assert.isTrue(Objects.equals(tenantModel.getContactNumber(), tenantModelQueryById.getContactNumber()), "租户联系人方式不一致");
    }


    private TenantModel generateTenantModel(int number) {
        return TenantModel.builder()
                .id(SNOWFLAKE_ID_WORKER.nextId())
                .tenantType("company")
                .tenantCode(getRandomString(32))
                .tenantName("test_tenant " + number)
                .contactNumber(getRandomString(6))
                .contactPerson(getRandomString(10))
                .remark(UUID.randomUUID().toString()).build();
    }

    private String getRandomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
