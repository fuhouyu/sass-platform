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

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 每日访问汇总数据
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 19:36
 */
@Data
@Schema(name = "MatomoVisitSummaryDTO", description = "Matomo 每日访问汇总数据")
public class MatomoVisitSummaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1287612386576152375L;

    @JsonAlias("nb_visits")
    @Schema(description = "访问次数")
    private Integer nbVisits;

    @JsonAlias("nb_uniq_visitors")
    @Schema(description = "唯一访客数量")
    private Integer nbUniqVisitors;

    @JsonAlias("nb_users")
    @Schema(description = "登录用户数量")
    private Integer nbUsers;

    @JsonAlias("nb_actions")
    @Schema(description = "行为总数（如点击、页面浏览等）")
    private Integer nbActions;

    @JsonAlias("nb_visits_converted")
    @Schema(description = "转化的访问次数")
    private Integer nbVisitsConverted;

    @JsonAlias("bounce_count")
    @Schema(description = "跳出访问数量（只访问一个页面就离开的次数）")
    private Integer bounceCount;

    @JsonAlias("sum_visit_length")
    @Schema(description = "访问时长总和（单位：秒）")
    private Integer sumVisitLength;

    @JsonAlias("max_actions")
    @Schema(description = "单个访问的最大行为数")
    private Integer maxActions;

    @JsonAlias("bounce_rate")
    @Schema(description = "跳出率（例如 0%）")
    private String bounceRate;

    @JsonAlias("nb_actions_per_visit")
    @Schema(description = "平均每次访问行为数")
    private Double nbActionsPerVisit;

    @JsonAlias("avg_time_on_site")
    @Schema(description = "平均停留时长（单位：秒）")
    private Integer avgTimeOnSite;

}