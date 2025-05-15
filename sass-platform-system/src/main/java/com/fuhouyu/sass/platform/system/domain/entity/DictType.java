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
package com.fuhouyu.sass.platform.system.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * 字典类型
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/15 16:51
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("dict_type")
public class DictType extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1283912301928301283L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 是否允许修改
     */
    private Boolean isAllowModified;

    /**
     * 状态：启用/禁用
     */
    private Boolean isEnabled;

    /**
     * 排序
     */
    private Integer displayOrder;

    /**
     * 所属租户
     */
    @TableField(fill = FieldFill.INSERT)
    private Long ownerTenantId;

    /**
     * 备注
     */
    private String remark;
}
