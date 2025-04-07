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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 服务监控dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 15:14
 */
@Data
@Schema(name = "ServerMonitorDTO", description = "服务监控dto对象")
public class ServerMonitorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8971278365745123564L;

    private CpuInfoDTO cpuInfo;

    private MemoryInfoDTO memoryInfo;

    private SystemInfoDTO systemInfo;

    private JvmInfoDTO jvmInfo;


    public ServerMonitorDTO() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        this.cpuInfo = new CpuInfoDTO(hardware.getProcessor());
        this.memoryInfo = new MemoryInfoDTO(hardware.getMemory());
        this.systemInfo = new SystemInfoDTO(systemInfo.getOperatingSystem());
        this.jvmInfo = new JvmInfoDTO();
    }
}
