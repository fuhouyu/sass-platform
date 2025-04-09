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
package com.fuhouyu.sass.platform.system.service.impl;

import cn.hutool.core.util.SystemPropsUtil;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.sass.platform.system.enums.response.LogMonitorResponseStatusEnum;
import com.fuhouyu.sass.platform.system.service.LogMonitorService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * <p>
 * 日志监控实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/7 21:57
 */
@Service
@Slf4j
public class LogMonitorServiceImpl implements LogMonitorService {

    private static final ExecutorService VIRTUAL_THREAD_POOL =
            Executors.newVirtualThreadPerTaskExecutor();
    @Value("${spring.application.name}")
    private String applicationName;
    private Path logFilePath;

    @PostConstruct
    public void init() {
        String filePath = SystemPropsUtil.get("base.framework.logs.path");
        this.logFilePath = Objects.isNull(filePath) ? (Paths.get(SystemPropsUtil.get("user.dir"), "logs", applicationName)) :
                Paths.get(filePath);
    }

    @Override
    public List<String> readHistoryLogFile(String logLevel, int lineNum) {
        File file = logFilePath.resolve(String.format("%s.log", logLevel.toLowerCase(Locale.ROOT))).toFile();
        List<String> result = new LinkedList<>();

        try (ReversedLinesFileReader reader = ReversedLinesFileReader.builder()
                .setBufferSize(4096)
                .setFile(file)
                .setCharset(StandardCharsets.UTF_8).get()) {
            String line;
            while ((line = reader.readLine()) != null && result.size() < lineNum) {
                result.addFirst(line);
            }
        } catch (IOException e) {
            throw new ServiceException(LogMonitorResponseStatusEnum.LOG_FILE_NOT_FOUND);
        }
        return result;
    }

    @Override
    public void listenerLog(TailerListener listener, String logLevel, Consumer<Tailer> destroy) {
        Tailer tailer = Tailer.builder()
                .setFile(logFilePath.resolve(String.format("%s.log", logLevel.toLowerCase(Locale.ROOT))).toFile())
                .setTailerListener(listener)
                .setDelayDuration(Duration.ofMillis(1000))
                .setTailFromEnd(true)
                .get();
        VIRTUAL_THREAD_POOL.submit(tailer);
        destroy.accept(tailer);
    }
}
