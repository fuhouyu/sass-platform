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

import {MatomoVisitSummary} from "@/model/matomoVisitSummary.tsx";
import {FC} from "react";
import ReactECharts from "echarts-for-react";
import {useThemeStore} from "@/store/modules/theme.tsx";
import {useTranslation} from "react-i18next";

const VisitTrendChart: FC<{ data: MatomoVisitSummary[] }> = ({data}) => {
    const dates = data.map(item => item.date);
    const nbVisits = data.map(item => item.nbVisits ?? 0);
    const nbUniqVisitors = data.map(item => item.nbUniqVisitors ?? 0);
    const nbUsers = data.map(item => item.nbUsers ?? 0);
    const currentTheme = useThemeStore(state => state.theme);
    const {t} = useTranslation();
    const option = {
        title: {
            text: t('MatomoVisit.visitTrendChart.title')
        },
        tooltip: {trigger: 'axis'},
        legend: {
            data: [
                t('MatomoVisit.visitTrendChart.legend.nbVisits'),
                t('MatomoVisit.visitTrendChart.legend.nbUniqVisitors'),
                t('MatomoVisit.visitTrendChart.legend.nbUsers')
            ]
        },
        xAxis: {type: 'category', data: dates},
        yAxis: {type: 'value'},
        series: [
            {name: t('MatomoVisit.visitTrendChart.legend.nbVisits'), type: 'line', data: nbVisits},
            {name: t('MatomoVisit.visitTrendChart.legend.nbUniqVisitors'), type: 'line', data: nbUniqVisitors},
            {name: t('MatomoVisit.visitTrendChart.legend.nbUsers'), type: 'line', data: nbUsers},
        ]
    };

    return <ReactECharts style={{
        width: '90%'
    }} theme={currentTheme === 'dark' ? 'dark-white-font' : ''} option={option}/>;
};

export default VisitTrendChart;