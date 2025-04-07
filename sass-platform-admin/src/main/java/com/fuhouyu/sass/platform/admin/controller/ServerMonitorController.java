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

import com.fuhouyu.sass.platform.system.domain.dto.monitor.ServerMonitorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>
 * 服务监控web控制器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/6 12:42
 */
@RestController
@RequestMapping("/v1/monitor/server")
@Tag(name = "服务监控 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ServerMonitorController {


    /**
     * 服务监控
     *
     * @return 监控
     */
    @GetMapping
    @Operation(summary = "服务监控")
    @PreAuthorize("@auth.hasAllPermission('system:server-monitor:list')")
    public Flux<ServerMonitorDTO> monitor() {
        return Flux.interval(Duration.ofSeconds(1))
                .concatMap(i -> Mono.fromCallable(ServerMonitorDTO::new));
    }

}
