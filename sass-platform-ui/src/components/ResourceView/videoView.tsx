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

import React, {useEffect} from "react";
import './index.scss';
import Player from "xgplayer";
import Mp4Plugin from "xgplayer-mp4";
import 'xgplayer/dist/index.min.css';


const VideoView = ({url}: { url: string }) => {

    useEffect(() => {
        const player = new Player({
            id: 'mse',
            url: url,
            autoplay: true,
            width: '100%',
            height: '90%',
            controls: true,
            plugins: [Mp4Plugin],
            mp4plugin: {
                maxBufferLength: 30,
                minBufferLength: 10,
            }
        });
        return () => player.destroy();
    }, [url]);

    return (
        <div id={'mse'} className={'video-container'}/>
    );
};


export default VideoView;