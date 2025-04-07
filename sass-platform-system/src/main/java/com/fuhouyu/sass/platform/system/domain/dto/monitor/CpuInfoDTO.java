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
import oshi.hardware.CentralProcessor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * cpu dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 15:16
 */
@Data
@Schema(name = "CpuInfoDTO", description = "cpu详情")
public class CpuInfoDTO implements Serializable {

    private static final int DEFAULT_DELAY = 1000;

    @Serial
    private static final long serialVersionUID = 961283758712536423L;


    @Schema(name = "coreNum", description = "核心数")
    private int coreNum;

    @Schema(name = "usageRate", description = "使用率")
    private double usageRate;

    public CpuInfoDTO(CentralProcessor processor) {
        this.coreNum = processor.getLogicalProcessorCount();

        // 默认等待一毫秒
        this.usageRate = BigDecimal.valueOf(processor.getSystemCpuLoad(DEFAULT_DELAY) * 100)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
