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
package com.fuhouyu.sass.platform.common.exception;

import com.fuhouyu.framework.common.response.BaseResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 服务异常
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 19:53
 */
@ToString
@Getter
@Setter
public class ServiceException extends RuntimeException {

    private final int status;

    private final String message;

    private final transient BaseResponseCode responseStatus;

    /**
     * 构造函数
     *
     * @param responseStatus 响应状态
     */
    public ServiceException(BaseResponseCode responseStatus) {
        this.responseStatus = responseStatus;
        this.status = responseStatus.getCode();
        this.message = responseStatus.getMessage();
    }

    /**
     * 构造函数
     *
     * @param responseStatus 响应状态
     * @param errorMessage   错误信息
     */
    public ServiceException(BaseResponseCode responseStatus, String errorMessage) {
        this.status = responseStatus.getCode();
        this.message = errorMessage;
        this.responseStatus = responseStatus;
    }

    /**
     * 构造函数
     *
     * @param responseStatus 响应状态
     * @param formatMessage  格式化消息
     * @param args           参数
     */
    public ServiceException(BaseResponseCode responseStatus, String formatMessage, Object... args) {
        this.status = responseStatus.getCode();
        this.message = String.format(formatMessage, args);
        this.responseStatus = responseStatus;
    }
}
