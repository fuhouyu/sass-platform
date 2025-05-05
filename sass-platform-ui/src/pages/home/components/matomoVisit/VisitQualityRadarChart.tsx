import {useThemeStore} from "@/store/modules/theme";
import {FC} from "react";
import {MatomoVisitSummary} from "@/model/matomoVisitSummary.tsx";
import ReactECharts from "echarts-for-react";
import {useTranslation} from "react-i18next";

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
export const VisitQualityRadarChart: FC<{ data: MatomoVisitSummary[] }> = ({data}) => {
    const currentTheme = useThemeStore(state => state.theme);
    const {t} = useTranslation();

    // 指标维度定义（可根据实际调整 max 值）
    const indicator = [
        {name: t('MatomoVisit.visitQualityRadarChart.indicator.avgActions'), max: 10},
        {name: t('MatomoVisit.visitQualityRadarChart.indicator.avgVisitTime'), max: 1000},
        {name: t('MatomoVisit.visitQualityRadarChart.indicator.bounceRate'), max: 100},
        {name: t('MatomoVisit.visitQualityRadarChart.indicator.conversions'), max: 10},
    ];

    // 构建 series 数据
    const radarData = data.map(item => ({
        name: item.date,
        value: [
            item.nbActionsPerVisit ?? 0,
            item.avgTimeOnSite ?? 0,
            parseFloat((item.bounceRate ?? '0').replace('%', '')) || 0,
            item.nbVisitsConverted ?? 0,
        ],
    }));

    const option = {

        tooltip: {},
        legend: {
            data: radarData.map(item => item.name),
        },
        radar: {
            indicator,
        },
        series: [
            {
                name: t('MatomoVisit.visitQualityRadarChart.title'),
                type: 'radar',
                data: radarData,
            },
        ],
    };

    return (
        <ReactECharts
            option={option}
            style={{width: '80%'}}
            theme={currentTheme === 'dark' ? 'dark-white-font' : ''}
        />
    );
};

export default VisitQualityRadarChart;