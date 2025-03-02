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

import React, {useEffect, useRef} from "react";
import videojs from "video.js";
import Player from "video.js/dist/types/player";
import {ResourceViewProps} from "@components/ResourceView/interface.tsx";
import {useResourcePreview} from "@/hooks/useResourcePreview.tsx";


const VideoView: React.FC<ResourceViewProps> = (resourceViewProps: ResourceViewProps) => {
    const videoRef = useRef<HTMLVideoElement>(null);
    const playerRef = useRef<Player | null>(null); // 明确类型为 Player | null
    const {previewUrl} = useResourcePreview();

    useEffect(() => {
        const playerUrl = previewUrl(resourceViewProps.id);
        console.log(resourceViewProps)
        if (videoRef.current) {
            // 初始化 Video.js 播放器
            playerRef.current = videojs(videoRef.current, {
                autoplay: true,
                controls: true,
                sources: [{
                    src: playerUrl,
                    type: 'video/mp4',
                }],
            });
        }

        // 组件卸载时销毁播放器
        return () => {
            if (playerRef.current) {
                playerRef.current.dispose();
            }
        };
    }, []);

    return (
        <div>
            <h1>React Video.js Example</h1>
            <div data-vjs-player="">
                <video ref={videoRef} className="video-js vjs-big-play-centered"/>
            </div>
        </div>
    );
};

export default VideoView;