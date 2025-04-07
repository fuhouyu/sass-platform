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
package com.fuhouyu.sass.platform.system.service;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 日志监控接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/7 21:56
 */
public interface LogMonitorService {

    /**
     * 读取历史日志记录
     *
     * @param logLevel 日志等级
     * @param lineNum  要读取的行数
     * @return 日志记录
     */
    List<String> readHistoryLogFile(String logLevel, int lineNum);

    /**
     * 监听日志
     *
     * @param listener 日志监听
     * @param logLevel 日志等级
     * @param destroy  销毁tailer
     */
    void listenerLog(TailerListener listener, String logLevel,
                     Consumer<Tailer> destroy);
}
