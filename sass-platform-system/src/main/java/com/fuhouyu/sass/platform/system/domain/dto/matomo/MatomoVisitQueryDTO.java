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
package com.fuhouyu.sass.platform.system.domain.dto.matomo;

import com.fuhouyu.sass.platform.system.enums.MatomoPeriodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * matomo 查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 19:46
 */
@Data
@Schema(name = "MatomoVisitQueryDTO", description = "matomo 查询的dto对象")
public class MatomoVisitQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8967182681275371623L;

    @Schema(name = "period", description = "周期")
    private MatomoPeriodEnum period;

    @Schema(name = "date", description = "查询日期", example = "last30 or 2025-01-01,2025-01-30")
    private String date;


}
