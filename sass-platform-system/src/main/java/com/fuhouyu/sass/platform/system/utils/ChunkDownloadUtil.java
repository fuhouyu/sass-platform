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
package com.fuhouyu.sass.platform.system.utils;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>
 * 分片下载工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/27 18:22
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ChunkDownloadUtil {

    /**
     * 分片大小，5MB
     */
    private static final long CHUNK_SIZE = 5L * 1024 * 1024;


    /**
     * 分片下载文件
     *
     * @param fileUrl      文件下载地址
     * @param chunkHandler 每一块下载完的回调处理器 (startByte, InputStream)
     */
    public static void downloadFile(String fileUrl, ChunkHandler chunkHandler) {
        URI uri;
        try {
            uri = new URI(fileUrl);
        } catch (URISyntaxException e) {
            LoggerUtil.error(log, "文件下载url: {}, 转换URI失败", fileUrl, e);
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "[%s] URL 格式不正确", fileUrl);
        }
        RestClient restClient = RestClient.create();
        long start = 0;
        int partNumber = 1;
        boolean finished = false;

        while (!finished) {
            long end = start + CHUNK_SIZE - 1;
            String range = String.format("bytes=%d-%d", start, end);
            ResponseEntity<byte[]> entity = restClient.method(HttpMethod.GET)
                    .uri(uri)
                    .header(HttpHeaders.RANGE, range)
                    .retrieve()
                    .toEntity(byte[].class);
            byte[] chunkBytes = entity.getBody();
            if (chunkBytes != null) {
                // 获取实际下载的大小
                int actualLength = chunkBytes.length;

                if (actualLength == 0) {
                    LoggerUtil.error(log, "文件下载url: {}, 返回空数据, 起始:{}，结束:{}", fileUrl, start, end);
                    throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "文件下载失败，返回空数据");
                }
                // 处理这一片
                try {
                    chunkHandler.handleChunk(partNumber, chunkBytes, actualLength);
                } catch (Exception e) {
                    LoggerUtil.error(log, "文件下载url: {}, 处理文件流失败, 起始:{}，结束:{}", fileUrl, start, end, e);
                    throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "处理文件流失败:{}", e.getMessage());
                }

                // 判断是否最后一片
                if (actualLength < CHUNK_SIZE) {
                    finished = true;
                } else {
                    start += CHUNK_SIZE;
                    partNumber++;
                }
            } else {
                LoggerUtil.error(log, "文件下载url: {}, 文件下载失败，流为null, 起始:{}，结束:{}", fileUrl, start, end);
                throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "文件下载失败");
            }

        }

    }

    /**
     * 分片处理器接口
     */
    public interface ChunkHandler {

        /**
         * 处理每一块下载完的回调
         *
         * @param partNumber 分片编号
         * @param bytes      当前字节数组，默认5mb
         * @param chunkSize  分片大小
         * @throws Exception 处理异常
         */
        void handleChunk(int partNumber, byte[] bytes, long chunkSize) throws Exception;
    }
}
