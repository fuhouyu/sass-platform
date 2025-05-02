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
import {useEffect, useState} from 'react';
import './index.scss'
import {resourceApi} from "@/apis/resource.tsx";
import {Progress} from 'antd';
import {useTranslation} from "react-i18next";

export const ImageView = ({id}: { id: string }) => {
    const [viewUrl, setViewUrl] = useState<string>();
    const [loading, setLoading] = useState(true);
    const [percent, setPercent] = useState(0);
    const {t} = useTranslation();
    // 模拟进度条前进（可替换为真实加载进度）
    useEffect(() => {
        if (loading) {
            let p = 0;
            const timer = setInterval(() => {
                p += 10;
                if (p >= 90) {
                    clearInterval(timer);
                }
                setPercent(p);
            }, 700);
            return () => clearInterval(timer);
        }
    }, [loading]);

    const handleLoaded = () => {
        setPercent(100);
        setTimeout(() => setLoading(false), 300); // 等进度条显示完再渲染页面
    };

    useEffect(() => {
        resourceApi.downloadFile(id, true, {
            responseType: 'blob' // 必须设置
        })
            .then(response => {
                const blob = new Blob([response as Blob], {type: 'image/*'});
                const url = URL.createObjectURL(blob);
                handleLoaded();
                setViewUrl(url);
            })
    }, [id]);
    return (loading ? (<div style={{padding: '8px', textAlign: 'center'}}>
        <Progress percent={percent} showInfo={false} strokeColor="#1890ff" size="small"/>
        <div style={{marginTop: 8, color: '#999'}}>{t('Common.resourceLoading')}</div>
    </div>) : <img
        width={'100%'}
        className={'image-view'}
        src={viewUrl}
        alt=''/>)
}