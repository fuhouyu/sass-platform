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
import oshi.util.FormatUtil;

import java.io.Serial;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * jvm 详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/7 09:30
 */
@Data
@Schema(name = "JvmInfoDTO", description = "jvm详情dto对象")
public class JvmInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1561239721367235123L;

    @Schema(name = "maxMemory", description = "最大内存")
    private String maxMemory;

    @Schema(name = "maxMemoryBytes", description = "最大堆内存字节")
    private Long maxMemoryBytes;

    @Schema(name = "freeMemory", description = "空闲内存")
    private String freeMemory;

    @Schema(name = "freeMemoryBytes", description = "空闲内存字节")
    private Long freeMemoryBytes;

    @Schema(name = "usedMemory", description = "已使用的内存")
    private String usedMemory;

    @Schema(name = "usedMemoryBytes", description = "已使用的内存字节")
    private Long usedMemoryBytes;

    @Schema(name = "jdkVersion", description = "jdk版本")
    private String jdkVersion;

    @Schema(name = "projectDir", description = "项目目录")
    private String projectDir;

    @Schema(name = "startTime", description = "启动时间")
    private LocalDateTime startTime;

    @Schema(name = "runTime", description = "运行时间")
    private String runTime;

    @Schema(name = "timestamp", description = "当前时间戳")
    private Long timestamp;

    public JvmInfoDTO() {
        RuntimeMXBean runTimeBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();

        this.startTime = Instant.ofEpochSecond(runTimeBean.getStartTime() / 1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        this.timestamp = System.currentTimeMillis();
        this.runTime = FormatUtil.formatElapsedSecs(runTimeBean.getUptime() / 1000);
        this.maxMemoryBytes = runtime.maxMemory();
        this.maxMemory = FormatUtil.formatBytes(this.maxMemoryBytes);
        this.freeMemoryBytes = runtime.freeMemory();
        this.freeMemory = FormatUtil.formatBytes(this.freeMemoryBytes);
        this.usedMemoryBytes = this.maxMemoryBytes - this.freeMemoryBytes;
        this.usedMemory = FormatUtil.formatBytes(this.usedMemoryBytes);
        this.jdkVersion = SystemPropsUtil.get("java.version");
        this.projectDir = SystemPropsUtil.get("user.dir");
    }
}
