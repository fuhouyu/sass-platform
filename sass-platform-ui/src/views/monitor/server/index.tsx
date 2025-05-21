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


import {FC, useEffect, useState} from "react";
import './index.scss'
import ReactECharts from 'echarts-for-react';
import {Card, Flex, List} from "antd";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {ServerMonitor as ServerMonitorModel} from "@/model/monitor.tsx";
import {useTranslation} from "react-i18next";
import {sseClient} from "@/utils/sse.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {useThemeStore} from "@/store/modules/theme.tsx";

interface MemoryData {
  timestamps: string[];
  maxMemory: number;
  usedMemory: number[];
  freeMemory: number[];
}


const getDashboardOption = (value: number | string, title: string) => {
  return {
    title: {
      text: title,
      left: 'center',
      textStyle: {
        align: 'center',
        fontSize: 18,
        fontWeight: 'bold',
      },
      top: '-2%'
    },
    series: [
      {
        type: 'gauge',
        axisLine: {
          lineStyle: {
            width: 10,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        pointer: {
          itemStyle: {
            color: 'auto'
          }
        },
        axisTick: {
          distance: -9,
          length: 4,
          lineStyle: {
            color: '#fff',
            width: 2
          }
        },
        splitLine: {
          distance: -10,
          length: 10,
          lineStyle: {
            color: '#fff',
            width: 4,
          }
        },
        axisLabel: {
          color: 'inherit',
          distance: 15,
          fontSize: 10
        },
        detail: {
          valueAnimation: true,
          formatter: '{value} %',
          color: 'inherit',
          fontSize: 15,
        },
        data: [
          {
            value: value
          }
        ]
      }
    ]
  }
};

const getJvmOLineChat = (memoryData: MemoryData,
                         title: string,
                         subTitle: string,
                         usedMemory: string,
                         freeMemory: string) => {
  return {
    title: {
      text: title,
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        textAlign: 'center'
      },
      subtext: subTitle,
      subtextStyle: {
        fontSize: 12,
        textAlign: 'center'
      },
    },
    legend: {
      data: [usedMemory, freeMemory]
    },
    xAxis: {
      type: 'category',
      data: memoryData.timestamps
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: Math.ceil(memoryData.maxMemory / 100) * 100, // 向上取整到最近的100MB
      interval: 100, // 每100MB一个刻度
      axisLabel: {
        formatter: '{value} MB'
      }
    },
    series: [
      {
        name: usedMemory,
        data: memoryData.usedMemory,
        type: 'line',
        lineStyle: {
          color: '#EE6666'
        },
        areaStyle: {
          color: 'rgba(238, 102, 102, 0.1)'
        }
      },
      {
        name: freeMemory,
        type: 'line',
        data: memoryData.freeMemory,
        lineStyle: {
          color: '#91CC75'
        },
        areaStyle: {
          color: 'rgba(145, 204, 117, 0.1)'
        }
      }
    ]
  }
};
const ServerMonitor: FC = () => {
  usePageTitle('Menu.serverMonitor');
  const [serverMonitor, setServerMonitor] = useState<ServerMonitorModel | undefined>(undefined);
  const {t} = useTranslation();
  const currentTheme = useThemeStore(state => state.theme);
  const [memoryData, setMemoryData] = useState<MemoryData>({
    timestamps: [],
    maxMemory: 0,
    usedMemory: [],
    freeMemory: []
  });

  useEffect(() => {

    sseClient.connect(BaseApiUrlConstant.SERVER_MONITOR_URL,
      {
        onMessage: (e) => {
          const data: ServerMonitorModel = JSON.parse(e.data) as ServerMonitorModel;
          setServerMonitor(data);
          if (data.jvmInfo) {
            const {timestamp, usedMemoryBytes, freeMemoryBytes, maxMemoryBytes} = data.jvmInfo;
            // 转换为 MB 单位
            const currentTime = new Date(parseInt(timestamp!)).toLocaleTimeString();

            setMemoryData(prev => {
              // 保留最近60秒的数据 (假设每秒一个数据点)
              const newTimestamps = [...prev.timestamps, currentTime!].slice(-60);
              const newUsedMemory = [...prev.usedMemory, (usedMemoryBytes && usedMemoryBytes / 1024 / 1024) ?? 0].slice(-60);
              const newFreeMemory = [...prev.freeMemory, (freeMemoryBytes && freeMemoryBytes / 1024 / 1024) ?? 0].slice(-60);

              return {
                timestamps: newTimestamps,
                maxMemory: maxMemoryBytes && (maxMemoryBytes / 1024 / 1024),
                usedMemory: newUsedMemory,
                freeMemory: newFreeMemory
              };
            });
          }
        }
      })
    return () => sseClient.disconnect();
  }, [])


  return (
    <Flex flex={1} gap={10} vertical justify={'space-between'} className={'monitor-container'}>
      <Flex flex={1} gap={10}>
        <Card title={t('Monitor.systemInfo')}
              loading={serverMonitor === undefined}
              style={{
                width: '49.5%'
              }}>
          <List>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.operationSystem')}</div>
                <div>{serverMonitor?.systemInfo?.osName}</div>
              </Flex>
            </List.Item>

            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.systemArch')}</div>
                <div>{serverMonitor?.systemInfo?.osArch}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.startTime')}</div>
                <div>{serverMonitor?.systemInfo?.startTime}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.runTime')}</div>
                <div>{serverMonitor?.systemInfo?.runTime}</div>
              </Flex>
            </List.Item>

            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.cpuCoreNum')}</div>
                <div>{serverMonitor?.cpuInfo?.coreNum}</div>
              </Flex>
            </List.Item>

            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.totalMemory')}</div>
                <div>{serverMonitor?.memoryInfo?.total}</div>
              </Flex>
            </List.Item>
          </List>
        </Card>
        <Card loading={serverMonitor === undefined} title={t('Monitor.systemMonitor')} style={{
          width: '49.5%'
        }}>
          <Flex wrap justify={'space-around'}>
            <Flex vertical justify={'center'} align={'center'}>
              <ReactECharts
                theme={currentTheme === 'dark' ? 'dark-white-font' : ''}
                option={getDashboardOption(serverMonitor?.cpuInfo?.usageRate ?? 0, t('Monitor.cpuUsageRate'))}
                style={{
                  width: 220,
                  height: 220
                }}
              />
            </Flex>

            <Flex vertical justify={'center'} align={'center'}>
              <ReactECharts
                theme={currentTheme === 'dark' ? 'dark-white-font' : ''}
                option={getDashboardOption(serverMonitor?.memoryInfo?.usageRate ?? 0, t('Monitor.memoryUsageRate'))}
                style={{
                  width: 220,
                  height: 220
                }}
              />
            </Flex>
          </Flex>
        </Card>
      </Flex>
      <Flex flex={1} gap={10}>
        <Card loading={serverMonitor === undefined} title={t('Monitor.jvmInfo')} style={{
          width: '49.5%'
        }}>
          <List>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.jvmMaxMemory')}</div>
                <div>{serverMonitor?.jvmInfo?.maxMemory}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.jvmUsedMemory')}</div>
                <div>{serverMonitor?.jvmInfo?.usedMemory}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.jvmFreeMemory')}</div>
                <div>{serverMonitor?.jvmInfo?.freeMemory}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.jdkVersion')}</div>
                <div>{serverMonitor?.jvmInfo?.jdkVersion}</div>
              </Flex>
            </List.Item>

            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.projectDir')}</div>
                <div>{serverMonitor?.jvmInfo?.projectDir}</div>
              </Flex>
            </List.Item>

            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.startTime')}</div>
                <div>{serverMonitor?.jvmInfo?.startTime}</div>
              </Flex>
            </List.Item>
            <List.Item>
              <Flex justify={'space-between'} align={'center'} style={{width: '100%'}}>
                <div>{t('Monitor.runTime')}</div>
                <div>{serverMonitor?.jvmInfo?.runTime}</div>
              </Flex>
            </List.Item>
          </List>
        </Card>
        <Card loading={serverMonitor === undefined} title={t('Monitor.jvmHeapMemory')} style={{
          width: '49.5%', overflow: 'hidden', borderRadius: 8
        }}>
          <Flex vertical justify={'center'} align={'center'} style={{width: '100%'}}>
            <ReactECharts
              theme={currentTheme === 'dark' ? 'dark-white-font' : ''}
              option={getJvmOLineChat(memoryData,
                t('Monitor.jvmMonitorTitle'),
                t('Monitor.jvmMonitorSubTitle'),
                t('Monitor.jvmUsedMemory'),
                t('Monitor.jvmFreeMemory'))}
              style={{height: '350px', width: '100%'}}
            />
          </Flex>
        </Card>
      </Flex>
    </Flex>
  );
}

export default ServerMonitor;
