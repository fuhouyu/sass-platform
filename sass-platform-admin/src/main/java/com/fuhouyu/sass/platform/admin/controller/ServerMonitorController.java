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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.sass.platform.admin.annotaions.NoAuth;
import com.fuhouyu.sass.platform.system.domain.dto.monitor.ServerMonitorDTO;
import com.fuhouyu.sass.platform.system.service.LogMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 服务监控web控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 12:42
 */
@RestController
@RequestMapping("/v1/monitor")
@Tag(name = "服务监控 web接口")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ServerMonitorController {

    private SystemInfo systemInfo;


    private final LogMonitorService logMonitorService;


    @PostConstruct
    public void init() {
        this.systemInfo = new SystemInfo();
    }

    /**
     * 服务监控
     *
     * @return 监控
     */
    @GetMapping("/server")
    @Operation(summary = "服务监控")
    @PreAuthorize("@auth.hasAllPermission('system:server-monitor:list')")
    public Flux<ServerMonitorDTO> monitor() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ServerMonitorDTO(systemInfo));
    }

    @GetMapping(value = "/log", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "服务日志")
    @NoAuth
    @Parameter(name = "logLevel", description = "日志级别")
    public Flux<String> log(@RequestParam(value = "logLevel", required = false, defaultValue = "info") String logLevel) {
        List<String> lastLines = this.logMonitorService.readHistoryLogFile(logLevel, 100);
        return Flux.create(sink -> {
            lastLines.forEach(sink::next);
            TailerListener listener = new TailerListenerAdapter() {
                @Override
                public void handle(String line) {
                    sink.next(line);
                }

                @Override
                public void handle(Exception ex) {
                    sink.error(ex);
                }
            };
            this.logMonitorService.listenerLog(listener, logLevel, tailer -> sink.onCancel(tailer::close));

        }, FluxSink.OverflowStrategy.BUFFER);

    }


}
