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
package com.fuhouyu.sass.platform.system.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:25
 */
@Data
public class OperationLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 请求位置
     */
    private String requestLocation;

    /**
     * 请求方法(GET/POST/PUT/DELETE等)
     */
    private String requestMethod;

    /**
     * 请求参数(JSON格式)
     */
    private String requestParam;

    /**
     * 响应数据,isSuccess为false时，这里显示错误信息
     */
    private String responseData;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 日志内容(中文)
     */
    private String content;

    /**
     * 日志内容(英文)
     */
    private String contentEn;

    /**
     * 操作状态(true/false)
     */
    private Boolean isSuccess;

    /**
     * 操作风险类型
     */
    private String riskType;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 是否移动端
     */
    private Boolean isMobile;

    /**
     * 引擎
     */
    private String engine;

    /**
     * 平台
     */
    private String platform;

    /**
     * 耗时
     */
    private Long costTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作人
     */
    private String operationUser;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 所属的租户id
     */
    private Long ownerTenantId;
}