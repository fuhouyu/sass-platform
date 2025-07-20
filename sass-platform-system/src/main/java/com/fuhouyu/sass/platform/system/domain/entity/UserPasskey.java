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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/19 23:36
 */
@TableName("user_passkeys")
@Getter
@Setter
@ToString
public class UserPasskey extends BaseEntity {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 通行密钥名称
     */
    private String passkeyName;

    /**
     * 通行密钥 ID（Base64 编码）
     */
    private String passkeyId;

    /**
     * 公钥（Base64 编码）
     */
    private String publicKey;

    /**
     * 签名计数器
     */
    private Long signCount;

    /**
     * AAGUID（Base64 编码）
     */
    private String aaguid;

    /**
     * 客户端数据 JSON（Base64 编码）
     */
    private String clientData;

    /**
     * Attestation 对象（Base64 编码）
     */
    private String attestationObject;

    /**
     * 认证器支持的传输类型（JSON 字符串）
     */
    private String transports;

    /**
     * 最后使用时间
     */
    private LocalDateTime lastUseTime;

    @TableField(exist = false)
    private Boolean isDeleted;

    @TableField(exist = false)
    private String createdBy;

    @TableField(exist = false)
    private String updatedBy;
}
