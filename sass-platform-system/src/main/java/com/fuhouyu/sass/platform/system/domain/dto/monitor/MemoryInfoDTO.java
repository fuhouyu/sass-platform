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
import oshi.hardware.GlobalMemory;
import oshi.util.FormatUtil;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 内存详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 15:23
 */
@Data
@Schema(name = "MemoryInfoDTO", description = "内存详情的dto对象")
public class MemoryInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 98787512364614523L;

    private static final BigDecimal GB = BigDecimal.valueOf(1024 * 1024 * 1024);

    @Schema(name = "total", description = "总内存")
    private String total;

    @Schema(name = "used", description = "已使用的内存")
    private String used;

    @Schema(name = "available", description = "空余内存")
    private String available;

    @Schema(name = "usageRate", description = "内存使用率")
    private double usageRate;


    public MemoryInfoDTO(GlobalMemory memory) {
        // 总内存，字节单位
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        long usedMemory = totalMemory - availableMemory;
        this.total = FormatUtil.formatBytes(totalMemory);
        this.used = FormatUtil.formatBytes(usedMemory);
        this.available = FormatUtil.formatBytes(availableMemory);
        this.usageRate =
                BigDecimal.valueOf(usedMemory).divide(BigDecimal.valueOf(totalMemory), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();


    }
}
