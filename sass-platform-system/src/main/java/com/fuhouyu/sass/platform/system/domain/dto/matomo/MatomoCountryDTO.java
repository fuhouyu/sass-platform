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
 * matomo 国家访问dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/5 13:36
 */
@Data
@Schema(name = "MatomoCountryDTO", description = "Matomo 国家访问dto对象")
public class MatomoCountryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 54123786182357123L;

    @Schema(name = "date", description = "日期")
    private String date;

    @Schema(description = "国家/地区名称")
    private String label;

    @JsonAlias("nb_uniq_visitors")
    @Schema(description = "唯一访客数量")
    private Integer nbUniqVisitors;

    @JsonAlias("nb_visits")
    @Schema(description = "访问次数")
    private Integer nbVisits;

    @JsonAlias("nb_actions")
    @Schema(description = "行为总数（如点击、页面浏览等）")
    private Integer nbActions;

    @JsonAlias("nb_users")
    @Schema(description = "登录用户数量")
    private Integer nbUsers;

    @JsonAlias("max_actions")
    @Schema(description = "单个访问的最大行为数")
    private Integer maxActions;

    @JsonAlias("sum_visit_length")
    @Schema(description = "访问时长总和（单位：秒）")
    private Integer sumVisitLength;

    @JsonAlias("bounce_count")
    @Schema(description = "跳出访问数量（只访问一个页面就离开的次数）")
    private Integer bounceCount;

    @JsonAlias("nb_visits_converted")
    @Schema(description = "转化的访问次数")
    private Integer nbVisitsConverted;

    @JsonAlias("code")
    @Schema(description = "国家代码（如 cn）")
    private String code;

    @JsonAlias("logo")
    @Schema(description = "国家图标 URL")
    private String logo;

    @JsonAlias("segment")
    @Schema(description = "国家代码 Segment 表达式")
    private String segment;

    @JsonAlias("logoHeight")
    @Schema(description = "图标高度")
    private Integer logoHeight;

    @JsonAlias("bounce_rate")
    @Schema(description = "跳出率（百分比字符串，如 40%）")
    private String bounceRate;

    @JsonAlias("nb_actions_per_visit")
    @Schema(description = "平均每次访问行为数")
    private Double nbActionsPerVisit;

    @JsonAlias("avg_time_on_site")
    @Schema(description = "平均停留时长（单位：秒）")
    private Integer avgTimeOnSite;
}

