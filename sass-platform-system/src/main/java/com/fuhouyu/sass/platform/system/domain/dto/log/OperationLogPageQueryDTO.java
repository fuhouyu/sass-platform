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
package com.fuhouyu.sass.platform.system.domain.dto.log;

import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * <p>
 * 操作日志分页查询对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/31 20:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "OperationLogPageQueryDTO", description = "操作日志分页查询对象")
public class OperationLogPageQueryDTO extends PageQueryDTO {

    @Serial
    private static final long serialVersionUID = 8917239879871237513L;

    @Schema(name = "systemName", description = "系统名称检索")
    private String systemName;

    @Schema(name = "moduleName", description = "模块名称检索")
    private String moduleName;

    @Schema(name = "operationType", description = "操作日志类型")
    private String operationType;

    @Schema(name = "riskType", description = "风险等级检索")
    private String riskType;

    @Schema(name = "isSuccess", description = "是否成功检索")
    private Boolean isSuccess;

    @Schema(name = "startTime", description = "开始时间")
    private LocalDate startTime;

    @Schema(name = "endTime", description = "结束时间")
    private LocalDate endTime;

    public OperationLogPageQueryDTO() {
        if (Objects.isNull(super.getSortColumn())) {
            super.setSortColumn("operation_time");
            super.setIsAsc(false);
        }

    }
}
