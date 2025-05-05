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


import React, {useCallback, useEffect, useState} from "react";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {matomoApi} from "@/apis/matomo.tsx";
import VisitTrendChart from "@/pages/home/components/visitSummary/VisitTrendChart.tsx";
import {MatomoVisitSummary} from "@/model/matomoVisitSummary.tsx";
import {Card} from "antd";
import BounceAndTimeChart from "@/pages/home/components/visitSummary/BounceAndTimeChart.tsx";

export const Home: React.FC = () => {
    usePageTitle('Menu.home');

    const [visitSummary, setVisitSummary] = useState<MatomoVisitSummary[]>([]);

    const initVisitSummary = useCallback(async () => {
        const res = await matomoApi.getVisitSummary({
            date: 'last30',
            period: 'DAY',
        });
        setVisitSummary(res);
    }, [])
    useEffect(() => {
        initVisitSummary().then();
    }, [])
    return (
        <>
            <Card title={'访问趋势'}>
                <VisitTrendChart data={visitSummary}/>
            </Card>

            <Card title={'访问趋势'}>
                <BounceAndTimeChart data={visitSummary}/>
            </Card>
        </>
    )
}