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
import {ResourceLoading} from "@components/ResourceView/loading/ResourceLoading.tsx";
import {resourceApi} from "@/apis/resource.ts";

export const ImageView = ({id}: { id: string }) => {
    const [viewUrl, setViewUrl] = useState<string>();
    const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    resourceApi.downloadFile(id, true, {
      responseType: 'blob' // 必须设置
    })
      .then(response => {
        console.log('请求url')
        const blob = new Blob([response as Blob], {type: 'image/*'});
        const url = URL.createObjectURL(blob);
        setViewUrl(url);
        setLoading(false);
      })
  }, [id]);
    if (loading) {
        return <ResourceLoading/>
    }
    return (<img
        width={'100%'}
        height={'100%'}
        className={'image-view'}
        src={viewUrl}
        alt=''/>);
}
