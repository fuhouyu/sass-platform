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
package com.fuhouyu.sass.platform.admin.response;

import com.fuhouyu.sass.platform.admin.enums.SseResponseTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * <p>
 * sse响应信息
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/19 11:45
 */
@Data
@Builder
public class SseResponseMessage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1681235471239876175L;

    @Schema(description = "消息类型：log/progress/error/done/custom")
    private SseResponseTypeEnum type;

    @Schema(description = "状态码，0为成功，非0为错误")
    private Integer code;

    @Schema(description = "消息内容")
    private String message;

    @Schema(description = "携带的数据内容")
    private transient T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public static <T> SseResponseMessage<T> of(SseResponseTypeEnum type, Integer code, String message, T data) {
        return SseResponseMessage.<T>builder()
                .type(type)
                .code(code)
                .message(message)
                .data(data)
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }

    public static <T> SseResponseMessage<T> success(SseResponseTypeEnum type, String message, T data) {
        return of(type, 0, message, data);
    }

    public static SseResponseMessage<Void> error(String message, int code) {
        return of(SseResponseTypeEnum.ERROR, code, message, null);
    }

    public static <T> SseResponseMessage<T> done(String message, T data) {
        return of(SseResponseTypeEnum.DONE, 0, message, data);
    }
}
