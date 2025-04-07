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
package com.fuhouyu.sass.platform.system.domain.dto.monitor;

import cn.hutool.core.util.SystemPropsUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * 系统详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 16:22
 */
@Data
@Schema(name = "SystemInfoDTO", description = "系统详情dto对象")
public class SystemInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 987123576512325177L;

    @Schema(name = "osName", description = "操作系统")
    private String osName;

    @Schema(name = "osArch", description = "系统架构")
    private String osArch;

    @Schema(name = "startTime", description = "系统启动时间")
    private LocalDateTime startTime;

    @Schema(name = "runTime", description = "运行时间")
    private String runTime;


    public SystemInfoDTO(OperatingSystem operatingSystem) {
        this.osName = SystemPropsUtil.get("os.name");
        this.osArch = SystemPropsUtil.get("os.arch");
        this.startTime = Instant.ofEpochSecond(operatingSystem.getSystemBootTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        this.runTime = FormatUtil.formatElapsedSecs(operatingSystem.getSystemUptime());
    }

}
