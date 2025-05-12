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
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 资源实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resources")
public class Resources extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1671235498129908299L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源大小
     */
    private Long size;

    /**
     * eTag
     */
    private String etag;

    /**
     * 资源类型
     */
    private String mimeType;

    /**
     * 对象key
     */
    private String objectKey;

    /**
     * 版本号
     */
    private String version;

    /**
     * 资源分类
     */
    private String category;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long ownerTenantId;

    /**
     * 是否是目录
     */
    private Boolean isDirectory;
}
