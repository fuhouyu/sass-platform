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
import {AnyObject} from "antd/es/_util/type";
import 'video.js/dist/video-js.css';
import './index.scss';
import {Flex} from "antd";

const VideoView = ({options}: { options: AnyObject }) => {
    const videoRef = useRef<HTMLVideoElement>(null);
    const playerRef = useRef<Player | null>(null);

    useEffect(() => {
        if (videoRef.current && !playerRef.current) {
            // 初始化播放器一次
            playerRef.current = videojs(videoRef.current, options);
            playerRef.current.addClass('vjs-lime');

        }
        return () => {
            if (playerRef.current) {
                playerRef.current.dispose();
                playerRef.current = null;
            }
        };
    }, []);

    useEffect(() => {
        if (playerRef.current) {
            // 当 options 改变，仅更新 src
            playerRef.current.autoplay(options.autoplay ?? false);
            playerRef.current.src(options.sources ?? []);
        }
    }, [options.autoplay, options.sources]);

    return (
        <Flex justify="center" align="center" className="view-container">
            <video
                ref={videoRef}
                className="video-js view-container vjs-big-play-centered"
            />
        </Flex>
    );
};

export default VideoView;