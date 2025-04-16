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
package com.fuhouyu.sass.platform.system.domain.dto.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:25
 */
@Data
@Schema(name = "OperationLogDTO", description = "操作日志dto对象")
public class OperationLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    private Long id;

    @Schema(name = "moduleName", description = "模块名称")
    private String moduleName;

    @Schema(name = "requestUri", description = "请求地址")
    private String requestUri;

    @Schema(name = "requestIp", description = "请求ip")
    private String requestIp;

    @Schema(name = "requestLocation", description = "请求位置")
    private String requestLocation;

    @Schema(name = "requestMethod", description = "请求方法(GET/POST/PUT/DELETE等)")
    private String requestMethod;

    @Schema(name = "requestParam", description = "请求参数(JSON格式)")
    private String requestParam;

    @Schema(name = "responseData", description = "响应数据,isSuccess为false时，这里显示错误信息")
    private String responseData;

    @Schema(name = "operationType", description = "操作类型")
    private String operationType;

    @Schema(name = "content", description = "日志内容(中文)")
    private String content;

    @Schema(name = "contentEn", description = "日志内容(英文)")
    private String contentEn;

    @Schema(name = "isSuccess", description = "操作状态(true/false)")
    private Boolean isSuccess;

    @Schema(name = "riskType", description = "操作风险类型")
    private String riskType;

    @Schema(name = "systemName", description = "系统名称")
    private String systemName;

    /**
     * 操作系统
     */
    @Schema(name = "os", description = "操作系统")
    private String os;

    @Schema(name = "browser", description = "浏览器")
    private String browser;

    @Schema(name = "browserVersion", description = "浏览器版本")
    private String browserVersion;

    @Schema(name = "isMobile", description = "isMobile")
    private Boolean isMobile;

    @Schema(name = "engine", description = "engine")
    private String engine;

    @Schema(name = "platform", description = "平台")
    private String platform;

    @Schema(name = "costTime", description = "耗时")
    private Long costTime;

    @Schema(name = "operationUser", description = "操作人")
    private String operationUser;

    @Schema(name = "operationTime", description = "操作时间")
    private LocalDateTime operationTime;

    @Schema(name = "errorMessage", description = "错误信息")
    private String errorMessage;

    @Schema(name = "ownerTenantId", description = "所属的租户id")
    private Long ownerTenantId;
}