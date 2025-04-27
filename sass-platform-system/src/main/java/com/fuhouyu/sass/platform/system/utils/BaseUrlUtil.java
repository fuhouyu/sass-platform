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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.request.Request;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * <p>
 * url 工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/26 20:40
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseUrlUtil {

    /**
     * 从 HttpServletRequest 对象中获取应用的 baseUrl。
     * 例如：http://localhost:8080 或 https://example.com
     *
     * @return 应用的 baseUrl，如果 request 为 null 则返回 null。
     */
    public static String getBaseUrl() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        if (request == null) {
            return null;
        }
        HttpServletRequest httpServletRequest = request.getHttpServletRequest();
        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) {
            baseUrl.append(":").append(serverPort);
        }
        return baseUrl.toString();
    }

    /**
     * 从 HttpServletRequest 对象中获取带有 contextPath 的 baseUrl。
     * 例如：http://localhost:8080/your-app 或 https://example.com/your-app
     *
     * @return 带有 contextPath 的 baseUrl，如果 request 为 null 则返回 null。
     */
    public static String getBaseUrlWithContextPath() {
        String baseUrl = getBaseUrl();
        if (Objects.isNull(baseUrl)) {
            return null;
        }
        HttpServletRequest httpServletRequest = ContextHolderStrategy.getContext().getRequest().getHttpServletRequest();
        String contextPath = httpServletRequest.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && !"/".equals(contextPath)) {
            baseUrl += contextPath;
        }
        return baseUrl;
    }
}
