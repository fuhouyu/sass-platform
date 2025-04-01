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

    @Schema(name = "operationUser", description = "操作人")
    private String operationUser;

    @Schema(name = "operationTime", description = "操作时间")
    private LocalDateTime operationTime;

    @Schema(name = "errorMessage", description = "错误信息")
    private String errorMessage;

    @Schema(name = "ownerTenantId", description = "所属的租户id")
    private Long ownerTenantId;
}