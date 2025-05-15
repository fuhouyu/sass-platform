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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuhouyu.framework.database.annotations.FieldCipher;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 19:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("applications")
public class Applications extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 89712837576142364L;


    /**
     * 客户端唯一标识
     */
    @TableId
    private String clientId;

    /**
     * 客户端密钥（加密存储）
     */
    @FieldCipher
    private String clientSecret;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * icon ID（通常指向文件表中的主键）
     */
    private Long icon;

    /**
     * 应用描述
     */
    private String remark;

    /**
     * 回调地址（多个以逗号分隔）
     */
    private String redirectUris;

    /**
     * 授权范围（多个以逗号分隔）
     */
    private String scopes;

    /**
     * 支持的授权类型（多个以逗号分隔）
     */
    private String grantTypes;

    /**
     * 访问令牌有效期（单位：秒）
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效期（单位：秒）
     */
    private Integer refreshTokenValidity;

    /**
     * 是否上架：TRUE=已上架，FALSE=未上架
     */
    private Boolean published;

    /**
     * IP 白名单（多个以逗号分隔）
     */
    private String ipWhitelist;

    /**
     * 是否启用：true 启用，false 禁用
     */
    private Boolean isEnabled;

    /**
     * 所属租户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long ownerTenantId;

}
