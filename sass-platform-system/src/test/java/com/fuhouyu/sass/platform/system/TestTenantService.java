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
package com.fuhouyu.sass.platform.system;

import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.dto.tenant.TenantDTO;
import com.fuhouyu.sass.platform.system.service.TenantService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * <p>
 * 租户存储层测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:41
 */

class TestTenantService extends TestBaseService {

    @Autowired
    private TenantService tenantService;


    @Test
    void testTenant() {
        // 保存
        TenantDTO tenantDTO = this.generateTenantModel();
        tenantService.save(tenantDTO);
        // 根据id查询
        TenantDTO tenantModelQueryById = this.tenantService.findById(tenantDTO.getId());
        this.compare(tenantDTO, tenantModelQueryById);
        // 根据编码查询
        TenantDTO queryByTenantCode = this.tenantService.findByTenantCode(tenantDTO.getTenantCode());
        this.compare(tenantDTO, queryByTenantCode);
        // 修改
        tenantDTO.setTenantName("update_tenant");
        this.tenantService.edit(tenantDTO);
        // 批量查询
        PageQueryDTO pageQueryDTO = new PageQueryDTO(1, 10);
        PageInfo<TenantDTO> list = this.tenantService.pageList(pageQueryDTO);
        for (TenantDTO model : list.getList()) {
            this.compare(tenantDTO, model);
        }
        // 删除
        int count = this.tenantService.removeById(tenantDTO.getId());
        Assert.isTrue(count > 0, "数据删除失败");

    }

    private void compare(TenantDTO source, TenantDTO target) {
        Assert.isTrue(Objects.equals(source.getTenantCode(), target.getTenantCode()), "租户编码不一致");
        Assert.isTrue(Objects.equals(source.getTenantName(), target.getTenantName()), "租户名称不一致");
        Assert.isTrue(Objects.equals(source.getRemark(), target.getRemark()), "租户备注不一致");
        Assert.isTrue(Objects.equals(source.getContactPerson(), target.getContactPerson()), "租户联系人不一致");
        Assert.isTrue(Objects.equals(source.getContactNumber(), target.getContactNumber()), "租户联系人方式不一致");
    }


    private TenantDTO generateTenantModel() {
        TenantDTO tenantDTO = new TenantDTO();
        tenantDTO.setTenantCode(super.getUUIDStr(8));
        tenantDTO.setTenantName(super.getUUIDStr(8));
        tenantDTO.setTenantType(super.getUUIDStr(8));
        tenantDTO.setRemark(super.getUUIDStr(8));
        tenantDTO.setIcon(super.getUUIDStr(8));
        tenantDTO.setContactPerson(super.getUUIDStr(8));
        tenantDTO.setContactNumber(super.getUUIDStr(8));
        return tenantDTO;
    }
}
