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
package com.fuhouyu.sass.platform.system.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * 租户do对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 22:25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TenantInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1623531235412389142L;


    /**
     * 主键id
     */
    private Long id;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户类型
     */
    private String tenantType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户图标
     */
    private String icon;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 状态：true 启用
     */
    private Boolean isEnabled;

}
