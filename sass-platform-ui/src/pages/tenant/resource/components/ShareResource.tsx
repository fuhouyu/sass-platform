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
import React, {useEffect, useState} from 'react';
import {Button, Flex, Input, InputNumber, message, Space, Typography} from 'antd';
import {CopyOutlined, LinkOutlined} from '@ant-design/icons';
import dayjs from 'dayjs';
import {useTranslation} from "react-i18next";
import './index.scss';
import {resourceApi} from "@/apis/resource.tsx";

const {Text} = Typography;

export const ShareResource = ({id}: { id?: string }) => {
    const [days, setDays] = useState(0);
    const [hours, setHours] = useState(12);
    const [minutes, setMinutes] = useState(0);
    const [expireSeconds, setExpireSeconds] = useState(0);
    const [shareUrl, setShareUrl] = useState('');
    const [expireAt, setExpireAt] = useState('');
    const {t} = useTranslation();

    useEffect(() => {
        const seconds = days * 86400 + hours * 3600 + minutes * 60;
        setExpireSeconds(seconds);

        const expireTime = dayjs().add(seconds, 'second');
        setExpireAt(expireTime.format('YYYY-MM-DD HH:mm:ss'));
    }, [days, hours, minutes]);

    useEffect(() => {
        if (expireSeconds <= 0 || !id) {
            return
        }
        resourceApi.share(id, expireSeconds).then((res) => {
            setShareUrl(res);
        });
    }, [expireSeconds, id]);

    const handleCopy = () => {
        navigator.clipboard.writeText(shareUrl).then(() => {
            message.success(t('Common.copySuccess')).then();
        });
    };

    return (<Flex vertical>
        <Text className={'share-tips'}>
            {t('Resource.shareTips')}
        </Text>
        <h2>{t('Resource.shareActiveFor')}</h2>
        <Space direction="horizontal" className={'expire-time-container'}>
            <InputNumber min={0} max={0} value={days} onChange={(v) => setDays(v ?? 0)}/>{t('Time.days')}
            <InputNumber min={0} max={12} value={hours} onChange={(v) => setHours(v ?? 0)}/>{t('Time.hours')}
            <InputNumber min={0} max={60} value={minutes} onChange={(v) => setMinutes(v ?? 0)}/>{t('Time.minutes')}
        </Space>

        {shareUrl && (
            <Space direction="vertical">
                <Text strong> <LinkOutlined/> <span>{t('Resource.shareExpireAt')}</span><span
                    className={'expire-time-text'}>{expireAt}</span></Text>
                <Input
                    readOnly
                    value={shareUrl}
                    disabled
                    suffix={<Button icon={<CopyOutlined/>} onClick={handleCopy}></Button>}
                />
            </Space>
        )}
    </Flex>)
}