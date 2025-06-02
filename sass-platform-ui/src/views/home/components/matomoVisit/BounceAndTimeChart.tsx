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

export const BounceAndTimeChart: FC<{ data: IMatomoVisitSummary[] }> = ({data}) => {
    const dates = data.map(item => item.date);
    const bounceRates = data.map(item =>
        parseFloat(item.bounceRate?.replace('%', '') ?? '0')
    );
    const avgTimes = data.map(item => item.avgTimeOnSite ?? 0);
    const currentTheme = useThemeStore(state => state.theme);
    const {t} = useTranslation();

    const option = {
        tooltip: {trigger: 'axis'},
        legend: {
            data: [
                t('MatomoVisit.bounceAndTimeChart.legend.bounceRate'),
                t('MatomoVisit.bounceAndTimeChart.legend.avgTime'),
            ]
        },
        xAxis: {type: 'category', data: dates},
        yAxis: [
            {type: 'value', name: t('MatomoVisit.bounceAndTimeChart.yAxis.bounceRate')},
            {type: 'value', name: t('MatomoVisit.bounceAndTimeChart.yAxis.avgTime')}
        ],
        series: [
            {
                name: t('MatomoVisit.bounceAndTimeChart.legend.bounceRate'),
                type: 'bar',
                yAxisIndex: 0,
                data: bounceRates,
                color: '#f56c6c',
            },
            {
                name: t('MatomoVisit.bounceAndTimeChart.legend.avgTime'),
                type: 'bar',
                yAxisIndex: 1,
                data: avgTimes,
                color: '#409EFF',
            }
        ]
    };

    return <ReactECharts style={{
        width: '80%'
    }} theme={currentTheme === 'dark' ? 'dark-white-font' : ''} option={option}/>;
};

export default BounceAndTimeChart;
