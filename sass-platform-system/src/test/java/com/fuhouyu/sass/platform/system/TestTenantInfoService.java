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
import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.service.TenantInfoService;
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

class TestTenantInfoService extends TestBaseService {

    @Autowired
    private TenantInfoService tenantInfoService;


    @Test
    void testTenant() {
        // 保存
        TenantInfoDTO tenantInfoDTO = this.generateTenantModel();
        tenantInfoService.save(tenantInfoDTO);
        // 根据id查询
        TenantInfoDTO tenantModelQueryById = this.tenantInfoService.findById(tenantInfoDTO.getId());
        this.compare(tenantInfoDTO, tenantModelQueryById);
        // 根据编码查询
        TenantInfoDTO queryByTenantCode = this.tenantInfoService.findByTenantCode(tenantInfoDTO.getTenantCode());
        this.compare(tenantInfoDTO, queryByTenantCode);
        // 修改
        tenantInfoDTO.setTenantName("update_tenant");
        this.tenantInfoService.edit(tenantInfoDTO);
        // 批量查询
        PageQueryDTO pageQueryDTO = new PageQueryDTO(1, 10);
        PageInfo<TenantInfoDTO> list = this.tenantInfoService.pageList(pageQueryDTO);
        for (TenantInfoDTO model : list.getList()) {
            this.compare(tenantInfoDTO, model);
        }
        // 删除
        int count = this.tenantInfoService.removeById(tenantInfoDTO.getId());
        Assert.isTrue(count > 0, "数据删除失败");

    }

    private void compare(TenantInfoDTO source, TenantInfoDTO target) {
        Assert.isTrue(Objects.equals(source.getTenantCode(), target.getTenantCode()), "租户编码不一致");
        Assert.isTrue(Objects.equals(source.getTenantName(), target.getTenantName()), "租户名称不一致");
        Assert.isTrue(Objects.equals(source.getRemark(), target.getRemark()), "租户备注不一致");
        Assert.isTrue(Objects.equals(source.getContactPerson(), target.getContactPerson()), "租户联系人不一致");
        Assert.isTrue(Objects.equals(source.getContactInfo(), target.getContactInfo()), "租户联系人方式不一致");
    }


    private TenantInfoDTO generateTenantModel() {
        TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
        tenantInfoDTO.setTenantCode(super.getUUIDStr(8));
        tenantInfoDTO.setTenantName(super.getUUIDStr(8));
        tenantInfoDTO.setTenantType(super.getUUIDStr(8));
        tenantInfoDTO.setRemark(super.getUUIDStr(8));
        tenantInfoDTO.setIcon(super.getUUIDStr(8));
        tenantInfoDTO.setContactPerson(super.getUUIDStr(8));
        tenantInfoDTO.setContactInfo(super.getUUIDStr(8));
        return tenantInfoDTO;
    }
}
