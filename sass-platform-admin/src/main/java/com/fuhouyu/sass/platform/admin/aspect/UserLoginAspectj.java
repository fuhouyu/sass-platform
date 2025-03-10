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
package com.fuhouyu.sass.platform.admin.aspect;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.system.dto.cloudflare.TurnstileVerifyRequestDTO;
import com.fuhouyu.sass.platform.system.dto.cloudflare.TurnstileVerifyResponseDTO;
import com.fuhouyu.sass.platform.system.dto.user.admin.UserLoginDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.properties.CloudflareProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * <p>
 * 用户登录切面
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/2 21:15
 */
@Aspect
@Component
@Slf4j
public class UserLoginAspectj {


    private final CloudflareProperties.Turnstile turnstile;

    public UserLoginAspectj(CloudflareProperties cloudflareProperties) {
        this.turnstile = cloudflareProperties.getTurnstile();
    }


    /**
     * 用户登录切面
     * @param joinPoint 切面点
     * @param userLoginDTO 用户登录dto对象
     * @return obj
     * @throws Throwable exception
     */
    @Around("execution(* com.fuhouyu.sass.platform.admin.controller.AuthenticationController.login(..)) && args(userLoginDTO)")
    public Object userLoginAspect(ProceedingJoinPoint joinPoint, UserLoginDTO userLoginDTO) throws Throwable {
        if (!Objects.equals(userLoginDTO.getAccountType(), AccountTypeEnum.PASSWORD)) {
            return joinPoint.proceed();
        }
        // 未启用cloudflare验证直接放行
        if (!turnstile.getEnabled()) {
            LoggerUtil.warn(log, "cloudflare turnstile is disabled");
            return joinPoint.proceed();
        }
        String cloudflareTurnstileToken = userLoginDTO.getCloudflareTurnstileToken();
        if (!StringUtils.hasText(cloudflareTurnstileToken)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "请通过Cloudflare Turnstile验证");
        }
        // 验证cloudflare
        TurnstileVerifyRequestDTO request = new TurnstileVerifyRequestDTO();
        request.setResponse(cloudflareTurnstileToken);
        request.setRemoteIp(ContextHolderStrategy.getContext().getRequest().getRequestIp());
        request.setSecret(turnstile.getSecret());
        ResponseEntity<TurnstileVerifyResponseDTO> responseEntity = RestClient
                .create(turnstile.getUrl())
                .post()
                .body(JacksonUtil.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(TurnstileVerifyResponseDTO.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            LoggerUtil.error(log, "cloudflare验证错误，错误码：{}", responseEntity.getStatusCode());
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                    "cloudflare验证错误");
        }
        TurnstileVerifyResponseDTO responseDTO = responseEntity.getBody();
        if (Objects.isNull(responseDTO)) {
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR,
                    "cloudflare验证错误,返回结果为空");
        }
        if (!responseDTO.getSuccess()) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    responseDTO.getErrorCodes());
        }
        // 继续执行原方法
        return joinPoint.proceed();
    }
}
