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


/**
 * 系统信息
 */
export interface SystemInfo {
    /**
     * os 名称
     */
    osName?: string;
    /**
     * 系统架构
     */
    osArch?: string;
    /**
     * 系统启动时间
     */
    startTime?: string;
    /**
     * 运行时间
     */
    runTime?: string;
}

/**
 * cpu info
 */
export interface CpuInfo {

    /**
     * 核心数
     */
    coreNum?: number;

    /**
     * 使用率
     */
    usageRate?: number;
}

/**
 * 内存详情
 */
export interface MemoryInfo {
    /**
     * 总内存
     */
    total?: number;

    /**
     * 已使用内存
     */
    used?: number;

    /**
     * 可用内存
     */
    available?: number;

    /**
     * 使用率
     */
    usageRate?: number;
}


/**
 * jvm info
 */
export interface JvmInfo {

    /**
     * 最大内存
     */
    maxMemory?: string;

    /**
     * 最大堆字节
     */
    maxMemoryBytes: number;

    /**
     * 空闲内存
     */
    freeMemory?: string;

    /**
     * 空闲内存字节
     */
    freeMemoryBytes?: number;

    /**
     * 已使用的内存
     */
    usedMemory?: string;

    /**
     * 已使用的内存字节
     */
    usedMemoryBytes?: number;

    /**
     * jdk版本
     */
    jdkVersion?: string;

    /**
     * 项目地址
     */
    projectDir?: string;

    /**
     * 启动时间
     */
    startTime?: string;

    /**
     * 运行时间
     */
    runTime?: string;

    /**
     * 运行时间时间戳
     */
    timestamp?: string;


}


/**
 * 服务监控
 */
export interface ServerMonitor {

    // cpu
    cpuInfo?: CpuInfo;

    // 内存
    memoryInfo?: MemoryInfo;

    // 系统信息
    systemInfo?: SystemInfo;

    // jvm 信息
    jvmInfo?: JvmInfo;
}