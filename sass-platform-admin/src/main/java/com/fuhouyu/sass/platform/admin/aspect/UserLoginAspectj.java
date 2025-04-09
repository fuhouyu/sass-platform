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

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.admin.constants.AuthenticationConstant;
import com.fuhouyu.sass.platform.system.domain.dto.cloudflare.TurnstileVerifyRequestDTO;
import com.fuhouyu.sass.platform.system.domain.dto.cloudflare.TurnstileVerifyResponseDTO;
import com.fuhouyu.sass.platform.system.domain.dto.config.ParamConfigDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserLoginDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.response.AuthenticationResponseStatusEnum;
import com.fuhouyu.sass.platform.system.properties.CloudflareProperties;
import com.fuhouyu.sass.platform.system.service.ParamConfigService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final CacheService<String, Object> cacheService;

    private final CloudflareProperties.Turnstile turnstile;

    private final ParamConfigService paramConfigService;

    public UserLoginAspectj(CacheService<String, Object> cacheService,
                            CloudflareProperties cloudflareProperties,
                            ParamConfigService paramConfigService) {
        this.cacheService = cacheService;
        this.turnstile = cloudflareProperties.getTurnstile();
        this.paramConfigService = paramConfigService;
    }

    /**
     * 登录切面
     */
    @Pointcut("execution(public * com.fuhouyu.sass.platform.admin.controller.AuthenticationController.adminLogin(..)) || " +
            "execution(public * com.fuhouyu.sass.platform.admin.controller.AuthenticationController.login(..))")
    public void loginPointcut() {
    }


    @Around("loginPointcut() && args(userLoginDTO,..)")
    public Object aroundLoginMethod(ProceedingJoinPoint joinPoint, UserLoginDTO userLoginDTO) throws Throwable {
        if (!Objects.equals(userLoginDTO.getAccountType(), AccountTypeEnum.PASSWORD)) {
            return joinPoint.proceed();
        }
        Map<String, List<ParamConfigDTO>> paramConfigMap = null;
        if (userLoginDTO.getAccountType().isPassword()) {
            // 只有密码才进行检查
            List<ParamConfigDTO> paramConfigs = paramConfigService.findListByGroupKey(AuthenticationConstant.LOGIN_ERROR_GROUP_KEY);
            paramConfigMap = paramConfigs.stream()
                    .collect(Collectors.groupingBy(ParamConfigDTO::getConfigKey));
            this.checkErrorCount(userLoginDTO.getAccount(), paramConfigMap);
        }

        this.checkCloudFlare(userLoginDTO);
        try {
            Object result = joinPoint.proceed();
            // 登录成功，清除缓存
            cacheService.delete(AuthenticationConstant.CACHE_USER_LOGIN_ERROR_PREFIX + userLoginDTO.getAccount());
            return result;
        } catch (Exception ex) {
            // 登录的异常处理
            this.handleLoginError(userLoginDTO, paramConfigMap);
            return null;
        }
    }


    /**
     * cloudFlare 检查
     *
     * @param userLoginDTO 用户登录的dto对象
     */
    private void checkCloudFlare(UserLoginDTO userLoginDTO) {
        // 未启用Cloudflare验证直接放行
        if (Boolean.FALSE.equals(turnstile.getEnabled())) {
            LoggerUtil.warn(log, "cloudflare turnstile is disabled");
            return;
        }
        // Cloudflare 验证
        String cloudflareTurnstileToken = userLoginDTO.getCloudflareTurnstileToken();
        if (!StringUtils.hasText(cloudflareTurnstileToken)) {
            throw new ServiceException(AuthenticationResponseStatusEnum.CLOUDFLARE_TURNSTILE_VERIFY_FAIL);
        }

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
            throw new ServiceException(AuthenticationResponseStatusEnum.CLOUDFLARE_TURNSTILE_VERIFY_FAIL);
        }

        TurnstileVerifyResponseDTO responseDTO = responseEntity.getBody();
        if (Objects.isNull(responseDTO)) {
            throw new ServiceException(AuthenticationResponseStatusEnum.CLOUDFLARE_TURNSTILE_VERIFY_FAIL);
        }
        if (Boolean.FALSE.equals(responseDTO.getSuccess())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, responseDTO.getErrorCodes());
        }
    }


    /**
     * 处理登录错误
     *
     * @param userLoginDTO   用户登录的dto对象
     * @param paramConfigMap 参数配置map对象
     */
    private void handleLoginError(UserLoginDTO userLoginDTO,
                                  Map<String, List<ParamConfigDTO>> paramConfigMap) {
        if (!userLoginDTO.getAccountType().isPassword()) {
            return;
        }
        // 登录失败逻辑

        Object loginErrorCountCacheValue = cacheService.get(AuthenticationConstant.CACHE_USER_LOGIN_ERROR_PREFIX + userLoginDTO.getAccount());
        int loginErrorCount = Objects.nonNull(loginErrorCountCacheValue) ? ((Integer) loginErrorCountCacheValue) + 1 : 1;

        int maxLoginErrorCount = Integer.parseInt(getConfigValue(AuthenticationConstant.LOGIN_FAIL_MAX_ATTEMPTS_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_MAX_ATTEMPTS_VALUE, paramConfigMap));
        int lockTime = Integer.parseInt(getConfigValue(AuthenticationConstant.LOGIN_FAIL_LOCK_DURATION_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_LOCK_DURATION_VALUE, paramConfigMap));

        cacheService.set(AuthenticationConstant.CACHE_USER_LOGIN_ERROR_PREFIX + userLoginDTO.getAccount(), loginErrorCount,
                lockTime, TimeUnit.MINUTES);

        // 锁定提示语
        String lockHintMessage = getConfigValue(AuthenticationConstant.LOGIN_ACCOUNT_LOCKED_MESSAGE_KEY,
                AuthenticationConstant.DEFAULT_ACCOUNT_LOCKED_MESSAGE_VALUE, paramConfigMap);

        if (loginErrorCount >= maxLoginErrorCount) {
            throw new ServiceException(ResponseStatusEnum.NOT_AUTH, String.format(lockHintMessage, lockTime));
        }

        int loginFailWarningThreshold = Integer.parseInt(getConfigValue(AuthenticationConstant.LOGIN_FAIL_WARNING_THRESHOLD_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_WARNING_THRESHOLD_VALUE, paramConfigMap));

        if (loginErrorCount >= loginFailWarningThreshold) {
            String warningMessage = getConfigValue(AuthenticationConstant.LOGIN_FAIL_WARNING_MESSAGE_KEY,
                    AuthenticationConstant.DEFAULT_LOGIN_FAIL_WARNING_MESSAGE_VALUE, paramConfigMap);
            throw new ServiceException(ResponseStatusEnum.NOT_AUTH,
                    String.format(warningMessage, loginErrorCount, (maxLoginErrorCount - loginErrorCount)));
        }

        String loginErrorMessage = getConfigValue(AuthenticationConstant.LOGIN_FAIL_ERROR_MESSAGE_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_ERROR_MESSAGE_VALUE, paramConfigMap);
        throw new ServiceException(ResponseStatusEnum.NOT_AUTH, loginErrorMessage);
    }

    /**
     * 获取配置值
     *
     * @param configKey          配置key
     * @param defaultConfigValue 配置key不存在时，获取默认的值
     * @param paramConfigMap     参数配置键值映射
     * @return 配置值
     */
    private String getConfigValue(String configKey, String defaultConfigValue,
                                  Map<String, List<ParamConfigDTO>> paramConfigMap) {
        ParamConfigDTO paramConfigDTO = new ParamConfigDTO();
        paramConfigDTO.setConfigValue(defaultConfigValue);
        return paramConfigMap.getOrDefault(configKey,
                        List.of(paramConfigDTO))
                .getFirst().getConfigValue();
    }

    /**
     * 检查登录的错误次数
     *
     * @param account        账号
     * @param paramConfigMap 参数配置map
     */
    private void checkErrorCount(String account,
                                 Map<String, List<ParamConfigDTO>> paramConfigMap) {
        Object o = this.cacheService.get(AuthenticationConstant.CACHE_USER_LOGIN_ERROR_PREFIX + account);
        if (Objects.isNull(o)) {
            return;
        }
        int loginErrorCount = (Integer) o;
        // 锁定提示语
        String lockHintMessage = this.getConfigValue(AuthenticationConstant.LOGIN_ACCOUNT_LOCKED_MESSAGE_KEY,
                AuthenticationConstant.DEFAULT_ACCOUNT_LOCKED_MESSAGE_VALUE, paramConfigMap);

        int maxLoginErrorCount = Integer.parseInt(getConfigValue(AuthenticationConstant.LOGIN_FAIL_MAX_ATTEMPTS_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_MAX_ATTEMPTS_VALUE, paramConfigMap));

        int lockTime = Integer.parseInt(getConfigValue(AuthenticationConstant.LOGIN_FAIL_LOCK_DURATION_KEY,
                AuthenticationConstant.DEFAULT_LOGIN_FAIL_LOCK_DURATION_VALUE, paramConfigMap));

        // 登录错误已超过最大次数，抛出异常
        if (loginErrorCount >= maxLoginErrorCount) {
            throw new ServiceException(ResponseStatusEnum.NOT_AUTH, String.format(lockHintMessage, lockTime));
        }

    }
}
