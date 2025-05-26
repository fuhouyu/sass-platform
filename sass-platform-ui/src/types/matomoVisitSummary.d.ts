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

export interface IMatomoVisitQuery {
    // 日期
    date: string;
    // 周期
    period: string;

}

export interface IMatomoVisitSummary {
    /**日期**/
    date: string;
    /** 唯一访客数 */
    nbUniqVisitors?: number;

    /** 用户数量 */
    nbUsers?: number;

    /** 访问次数 */
    nbVisits?: number;

    /** 操作总数 */
    nbActions?: number;

    /** 转化访问次数 */
    nbVisitsConverted?: number;

    /** 跳出次数 */
    bounceCount?: number;

    /** 访问总时长（秒） */
    sumVisitLength?: number;

    /** 单次最大行为数 */
    maxActions?: number;

    /** 跳出率 */
    bounceRate?: string;

    /** 平均每次访问的操作数 */
    nbActionsPerVisit?: number;

    /** 平均访问时长（秒） */
    avgTimeOnSite?: number;
}

/**
 * 国家访问统计
 */
export interface IMatomoCountryVisit {
    /** 国家/地区名称 */
    label: string;

    /** 唯一访客数量 */
    nbUniqVisitors: number;

    /** 访问次数 */
    nbVisits: number;

    /** 行为总数（如点击、页面浏览等） */
    nbActions: number;

    /** 登录用户数量 */
    nbUsers: number;

    /** 单个访问的最大行为数 */
    maxActions: number;

    /** 访问时长总和（单位：秒） */
    sumVisitLength: number;

    /** 跳出访问数量（只访问一个页面就离开的次数） */
    bounceCount: number;

    /** 转化的访问次数 */
    nbVisitsConverted: number;

    /** 国家代码（如 cn） */
    code: string;

    /** 国家图标 URL */
    logo: string;

    /** 国家代码 Segment 表达式 */
    segment: string;

    /** 图标高度 */
    logoHeight: number;

    /** 跳出率（百分比字符串，如 40%） */
    bounceRate?: string;

    /** 平均每次访问行为数 */
    nbActionsPerVisit?: number;

    /** 平均停留时长（单位：秒） */
    avgTimeOnSite?: number;
}
