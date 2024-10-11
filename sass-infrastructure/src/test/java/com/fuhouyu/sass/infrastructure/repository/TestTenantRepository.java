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
package com.fuhouyu.sass.infrastructure.repository;

import com.fuhouyu.sass.domain.model.page.PageQueryValue;
import com.fuhouyu.sass.domain.model.page.PageResultEntity;
import com.fuhouyu.sass.domain.model.tenant.TenantEntity;
import com.fuhouyu.sass.domain.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

class TestTenantRepository extends TestBaseRepository {

    @Autowired
    private TenantRepository tenantRepository;



    @Test
    void testTenant() {
        // 保存
        TenantEntity tenantModel = this.generateTenantModel();
        tenantRepository.save(tenantModel);
        // 根据id查询
        TenantEntity tenantModelQueryById = this.tenantRepository.findById(tenantModel.getId());
        this.compare(tenantModel, tenantModelQueryById);
        // 根据编码查询
        TenantEntity queryByTenantCode = this.tenantRepository.findByTenantCode(tenantModel.getTenantCode());
        this.compare(tenantModel, queryByTenantCode);
        // 修改
        tenantModel.setTenantName("update_tenant");
        this.tenantRepository.edit(tenantModel);
        // 批量查询
        PageQueryValue pageQueryValue = new PageQueryValue(1, 10);
        PageResultEntity<TenantEntity> list = this.tenantRepository.pageList(pageQueryValue);
        for (TenantEntity model : list.getList()) {
            this.compare(tenantModel, model);
        }
        // 删除
        int count = this.tenantRepository.removeById(tenantModel.getId());
        Assert.isTrue(count > 0, "数据删除失败");

    }

    private void compare(TenantEntity tenantModel, TenantEntity tenantModelQueryById) {
        Assert.isTrue(Objects.equals(tenantModel.getTenantCode(), tenantModelQueryById.getTenantCode()), "租户编码不一致");
        Assert.isTrue(Objects.equals(tenantModel.getTenantName(), tenantModelQueryById.getTenantName()), "租户名称不一致");
        Assert.isTrue(Objects.equals(tenantModel.getRemark(), tenantModelQueryById.getRemark()), "租户备注不一致");
        Assert.isTrue(Objects.equals(tenantModel.getContactPerson(), tenantModelQueryById.getContactPerson()), "租户联系人不一致");
        Assert.isTrue(Objects.equals(tenantModel.getContactNumber(), tenantModelQueryById.getContactNumber()), "租户联系人方式不一致");
    }


    private TenantEntity generateTenantModel() {
        return TenantEntity.builder()
                .id(super.nextSnowflakeId())
                .tenantType("company")
                .tenantCode(getRandomString(32))
                .tenantName("test_tenant " + 1)
                .contactNumber(getRandomString(6))
                .contactPerson(getRandomString(10))
                .remark(UUID.randomUUID().toString()).build();
    }

    private String getRandomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
