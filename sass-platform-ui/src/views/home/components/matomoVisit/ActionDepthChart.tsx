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

import {FC} from "react";
import {IMatomoVisitSummary} from "@/types/matomoVisitSummary";
import ReactECharts from "echarts-for-react";
import {useThemeStore} from "@/store/modules/theme.ts";
import {useTranslation} from "react-i18next";

export const ActionDepthChart: FC<{ data: IMatomoVisitSummary[] }> = ({data}) => {
    const dates = data.map(item => item.date);
    const actions = data.map(item => item.nbActions ?? 0);
    const maxActions = data.map(item => item.maxActions ?? 0);
    const avgActions = data.map(item => item.nbActionsPerVisit ?? 0);
    const currentTheme = useThemeStore(state => state.theme);
    const {t} = useTranslation();

    const option = {
        title: {
            text: t('MatomoVisit.actionDepthChart.title'),

        },
        tooltip: {trigger: 'axis'},
        legend: {
            data: [
                t('MatomoVisit.actionDepthChart.legend.actions'),
                t('MatomoVisit.actionDepthChart.legend.maxActions'),
                t('MatomoVisit.actionDepthChart.legend.avgActions'),
            ]
        },
        xAxis: {type: 'category', data: dates},
        yAxis: {type: 'value'},
        series: [
            {name: t('MatomoVisit.actionDepthChart.legend.actions'), type: 'line', areaStyle: {}, data: actions},
            {name: t('MatomoVisit.actionDepthChart.legend.maxActions'), type: 'line', areaStyle: {}, data: maxActions},
            {name: t('MatomoVisit.actionDepthChart.legend.avgActions'), type: 'line', areaStyle: {}, data: avgActions},
        ]
    };

    return <ReactECharts option={option} style={{
        width: '90%'
    }} theme={currentTheme === 'dark' ? 'dark-white-font' : ''}/>;
};

export default ActionDepthChart;
