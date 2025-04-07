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


import React, {FC, useEffect, useRef, useState} from "react";
import {sseClient} from "@/utils/sse.tsx";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {Card, List, Segmented, Space, Spin, Typography} from "antd";
import './index.scss'
import {MessageEvent} from "event-source-polyfill";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {useTranslation} from "react-i18next";

const {Text} = Typography;

export const LogMonitor: FC = () => {
    const [logs, setLogs] = useState<string[]>([]);
    const [loading, setLoading] = useState(true);
    const listRef = useRef<HTMLDivElement>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const defaultLogLevel = querySearchParams().logLevel ? querySearchParams().logLevel : 'INFO'
    const {t} = useTranslation();

    const onMessage = (e: MessageEvent) => {
        const data = JSON.parse(e.data);
        if (data === 'heartbeat') {
            return;
        }
        setLogs(prev => [...prev, data]);
        setLoading(false);
    }
    useEffect(() => {
        sseClient.connect(`${BaseApiUrlConstant.LOG_MONITOR}?logLevel=${defaultLogLevel}`, {
            onMessage: e => onMessage(e),
        })

    }, [])
    useEffect(() => {
        // 滚动到底部
        if (listRef.current) {
            listRef.current.scrollTop = listRef.current.scrollHeight;
        }
    }, [logs]);

    return (
        <Card title={t('LogMonitor.title')}
              extra={<Segmented
                  defaultValue={defaultLogLevel}
                  options={['INFO', 'ERROR']}
                  onChange={(value: string) => {
                      setLogs([])
                      updateSearchParams({logLevel: value})
                      sseClient.connect(`${BaseApiUrlConstant.LOG_MONITOR}?logLevel=${value}`, {
                          onMessage: (e) => onMessage(e),
                      });
                  }}
              />}
              className={'log-container'}>


            <Space size={'large'}/>
            <div style={{position: 'relative', height: '600px'}}>
                <Spin spinning={loading}
                      style={{position: 'absolute', top: 180, left: '50%', transform: 'translateX(-50%)', zIndex: 2}}/>
                <div
                    className={'log-content'}
                    ref={listRef}

                >
                    <List
                        dataSource={logs}
                        renderItem={(item, index) => {
                            const color = defaultLogLevel === 'INFO' ? '#00ff00' : 'red'
                            return <List.Item key={index}>
                                <Text
                                    style={{
                                        color: color
                                    }}
                                    className={'log-text'}>{item}</Text>
                            </List.Item>
                        }}
                    />
                </div>
            </div>

        </Card>
    )
}