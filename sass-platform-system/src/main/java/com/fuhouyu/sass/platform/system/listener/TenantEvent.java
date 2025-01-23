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
package com.fuhouyu.sass.platform.system.listener;

import com.fuhouyu.sass.platform.system.dto.tenant.TenantInfoDTO;
import com.fuhouyu.sass.platform.system.enums.TenantEventEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * 租户事件
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/17 21:03
 */
@Getter
public class TenantEvent extends ApplicationEvent {

    private final TenantEventEnum tenantEventEnum;

    /**
     * 租户事件
     *
     * @param tenantInfoDTO dto对象
     */
    public TenantEvent(TenantInfoDTO tenantInfoDTO, TenantEventEnum tenantEventEnum) {
        super(tenantInfoDTO);
        this.tenantEventEnum = tenantEventEnum;
    }

    @Override
    public TenantInfoDTO getSource() {
        return (TenantInfoDTO) super.getSource();
    }
}
