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

import {FC, useCallback, useEffect, useState} from "react";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {matomoApi} from "@/apis/matomo.ts";
import VisitTrendChart from "@/views/home/components/matomoVisit/VisitTrendChart.tsx";
import {MatomoVisitSummary} from "@/model/matomoVisitSummary.tsx";
import {Card, DatePicker, Flex, Select, Space} from "antd";
import './index.scss'
import ActionDepthChart from "@/views/home/components/matomoVisit/ActionDepthChart.tsx";
import dayjs, {Dayjs} from "dayjs";
import {useTranslation} from "react-i18next";

const {RangePicker} = DatePicker;

const Home: FC = () => {
  usePageTitle('Menu.home');
  const [visitSummary, setVisitSummary] = useState<MatomoVisitSummary[]>([]);
  // 默认选择最近30天
  const [dateRange, setDateRange] = useState<string>('last30');
  const [customDateRange, setCustomDateRange] = useState<[Dayjs | undefined, Dayjs | undefined]>();
  const [loading, setLoading] = useState<boolean>(true);
  const {t} = useTranslation();

  const initVisitSummary = useCallback(async (date: string) => {
    const data = await matomoApi.getVisitSummary({
      date,
      period: 'DAY',
    });
    setVisitSummary(data);
    setLoading(false);
  }, []);

  useEffect(() => {
    initVisitSummary(dateRange).then();
  }, [initVisitSummary, dateRange]);

  const handleDateRangeChange = (value: string) => {
    // 更改日期范围
    setDateRange(value);
    setCustomDateRange([undefined, undefined])
  };

  return (
    <div className="visit-summary-container">
      <Space direction={'vertical'} style={{width: '100%'}}>
        <div>
          <Select
            value={dateRange}
            onChange={handleDateRangeChange}
            style={{width: 150, marginRight: 10}}
          >
            <Select.Option value="last7">{t('DateRange.presetRanges.last7Days')}</Select.Option>
            <Select.Option value="last30">{t('DateRange.presetRanges.last30Days')}</Select.Option>
            <Select.Option value="last60">{t('DateRange.presetRanges.last60Days')}</Select.Option>
            <Select.Option value="last90">{t('DateRange.presetRanges.last90Days')}</Select.Option>
          </Select>
          <RangePicker
            onChange={async (_, search) => {
              if (search[0] === '' || search[1] === '') {
                await initVisitSummary(dateRange)
              } else {
                await initVisitSummary(search.join(','));
              }
              setCustomDateRange([dayjs(search[0]), dayjs(search[1])])
            }}
            value={customDateRange}
            style={{width: 300}}
            placeholder={[t('DateRange.start'), t('DateRange.end')]}
            disabledDate={(currentDate) => {
              return currentDate.isAfter(new Date());
            }}

          />
        </div>
        <Card title={t('MatomoVisit.title')} loading={loading}>
          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
            <div>
              <span style={{fontSize: 16}}>{t('MatomoVisit.totalVisits')}</span>
              <span style={{fontSize: 20, fontWeight: 'bold', color: '#1677ff'}}>
              {visitSummary.reduce((sum, item) => sum + (item.nbVisits ?? 0), 0)}
            </span>
            </div>
            <div>
              <span style={{fontSize: 16}}>{t('MatomoVisit.totalUsers')}</span>
              <span style={{fontSize: 20, fontWeight: 'bold', color: '#1677ff'}}>
              {visitSummary.reduce((sum, item) => sum + (item.nbUsers ?? 0), 0)}
            </span>
            </div>
            <div>
              <span style={{fontSize: 16}}>{t('MatomoVisit.uniqueVisitors')}</span>
              <span style={{fontSize: 20, fontWeight: 'bold', color: '#1677ff'}}>
              {visitSummary.reduce((sum, item) => sum + (item.nbUniqVisitors ?? 0), 0)}
            </span>
            </div>
          </div>
        </Card>
        <Flex justify={'space-between'} align={'center'}>
          <Card style={{width: '49%'}} loading={loading}>
            <VisitTrendChart data={visitSummary}/>
          </Card>
          <Card style={{width: '49%'}} loading={loading}>
            <ActionDepthChart data={visitSummary}/>
          </Card>
        </Flex>
      </Space>
    </div>
  );
};

export default Home;
