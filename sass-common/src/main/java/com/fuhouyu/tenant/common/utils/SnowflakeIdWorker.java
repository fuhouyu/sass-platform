/*
 * Copyright 2024-2024 the original author or authors.
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

package com.fuhouyu.tenant.common.utils;

/**
 * <p>
 * 雪花算法
 * </p>
 *
 * @author admin
 * @since 2024/9/20 18:48
 */
public class SnowflakeIdWorker {

    // 初始时间戳(纪年)，可用雪花算法服务上线时间戳的值
    private static final long INIT_EPOCH = 1694263918335L;
    // 时间位取&
    private static final long TIME_BIT = 0b1111111111111111111111111111111111111111110000000000000000000000L;
    // dataCenterId占用的位数
    private static final long DATA_CENTER_ID_BITS = 5L;
    // dataCenterId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    // workId占用的位数
    private static final long WORKER_ID_BITS = 5L;
    // workId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 最后12位，代表每毫秒内可产生最大序列号，即 2^12 - 1 = 4095
    private static final long SEQUENCE_BITS = 12L;
    // 掩码（最低12位为1，高位都为0），主要用于与自增后的序列号进行位与，如果值为0，则代表自增后的序列号超过了4095
    // 0000000000000000000000000000000000000000000000000000111111111111
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    // workId位需要左移的位数 12
    private static final long WORK_ID_SHIFT = SEQUENCE_BITS;
    // dataCenterId位需要左移的位数 12+5
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳需要左移的位数 12+5+5
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    // dataCenterId
    private final long dataCenterId;
    // workId
    private final long workerId;
    // 记录最后使用的毫秒时间戳，主要用于判断是否同一毫秒，以及用于服务器时钟回拨判断
    private long lastTimeMillis = -1L;
    // 同一毫秒内的最新序号，最大值可为 2^12 - 1 = 4095
    private long sequence;

    /**
     * 无参构造
     */
    public SnowflakeIdWorker() {
        this(1, 1);
    }


    /**
     * 有参构造
     *
     * @param dataCenterId 数据中心id
     * @param workerId     工作id
     */
    public SnowflakeIdWorker(long dataCenterId, long workerId) {
        // 检查dataCenterId的合法值
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("dataCenterId 值必须大于 0 并且小于 %d", MAX_DATA_CENTER_ID));
        }
        // 检查workId的合法值
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(String.format("workId 值必须大于 0 并且小于 %d", MAX_WORKER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }


    /**
     * 通过雪花算法生成下一个id，注意这里使用synchronized同步
     *
     * @return 唯一id
     */
    public synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();
        // 当前时间小于上一次生成id使用的时间，可能出现服务器时钟回拨问题
        if (currentTimeMillis < lastTimeMillis) {
            throw new RuntimeException(
                    String.format("可能出现服务器时钟回拨问题，请检查服务器时间。当前服务器时间戳：%d，上一次使用时间戳：%d", currentTimeMillis,
                            lastTimeMillis));
        }
        if (currentTimeMillis == lastTimeMillis) {
            // 还是在同一毫秒内，则将序列号递增1，序列号最大值为4095
            // 序列号的最大值是4095，使用掩码（最低12位为1，高位都为0）进行位与运行后如果值为0，则自增后的序列号超过了4095
            // 那么就使用新的时间戳
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimeMillis = getNextMillis(lastTimeMillis);
            }
        } else { // 不在同一毫秒内，则序列号重新从0开始，序列号最大值为4095
            sequence = 0;
        }
        // 记录最后一次使用的毫秒时间戳
        lastTimeMillis = currentTimeMillis;
        // 核心算法，将不同部分的数值移动到指定的位置，然后进行或运行
        // <<：左移运算符, 1 << 2 即将二进制的 1 扩大 2^2 倍
        // |：位或运算符, 是把某两个数中, 只要其中一个的某一位为1, 则结果的该位就为1
        // 优先级：<< > |
        return
                // 时间戳部分
                ((currentTimeMillis - INIT_EPOCH) << TIMESTAMP_SHIFT)
                        // 数据中心部分
                        | (dataCenterId << DATA_CENTER_ID_SHIFT)
                        // 机器表示部分
                        | (workerId << WORK_ID_SHIFT)
                        // 序列号部分
                        | sequence;
    }

    /**
     * 获取指定时间戳的接下来的时间戳，也可以说是下一毫秒
     *
     * @param lastTimeMillis 指定毫秒时间戳
     * @return 时间戳
     */
    private long getNextMillis(long lastTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimeMillis) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }

}