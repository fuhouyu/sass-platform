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
package com.fuhouyu.sass.platform.system.components.handle;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

/**
 * <p>
 * 异常处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/28 17:20
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandle {


    /**
     * 授权异常
     *
     * @param request 请求
     * @param e       异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public BaseResponse<Void> exceptionHandle(ServletWebRequest request, Exception e) {
        LoggerUtil.error(log, "请求地址:{}, 异常信息:{}", request.getRequest().getRequestURI(), e.getMessage());
        return ResponseHelper.failed(ResponseStatusEnum.NOT_AUTH);
    }

    /**
     * 异步请求不可达
     *
     * @param request 请求
     * @param e       异常信息
     */
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void exceptionHandle(ServletWebRequest request, AsyncRequestNotUsableException e) {
        LoggerUtil.warn(log, "请求地址:{}, AsyncRequestNotUsableException 异常信息:{}", request.getRequest().getRequestURI(), e.getMessage());
    }
}
