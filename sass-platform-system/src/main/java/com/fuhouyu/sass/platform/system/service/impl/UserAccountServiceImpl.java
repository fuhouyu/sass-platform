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
package com.fuhouyu.sass.platform.system.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.security.core.provider.refreshtoken.RefreshAuthenticationProvider;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.system.assembler.TokenAssembler;
import com.fuhouyu.sass.platform.system.components.security.UserAccountAuthenticationToken;
import com.fuhouyu.sass.platform.system.constants.CacheConstant;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.ThirdPartyBindPlatformDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.UserAccountDetails;
import com.fuhouyu.sass.platform.system.domain.dto.user.LoginUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserLoginDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.response.AuthenticationResponseStatusEnum;
import com.fuhouyu.sass.platform.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 账号接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 18:12
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private static final TokenAssembler TOKEN_ASSEMBLER = TokenAssembler.INSTANCE;

    private final AccountService accountService;

    private final AdminUserService adminUserService;

    private final TokenStore tokenStore;

    private final AuthenticationManager authenticationManager;

    private final CacheService<String, Object> cacheService;

    private final UserService userService;

    private final PermissionService permissionService;


    @Override
    public UserTokenDTO adminLogin(UserLoginDTO userLoginDTO) {
        UserTokenDTO userTokenDTO = this.authentication(userLoginDTO, authentication -> {
            UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
            // 管理员用户
            AdminUserDTO userDetails = this.adminUserService.findById(userAccountDetails.getUserId());
            return UserAccountAuthenticationToken.authenticated(authentication.getPrincipal(),
                    authentication.getCredentials(), userDetails, this.getLoginUserDetailDTO(userLoginDTO),
                    this.permissionService.findUserSimpleGrantedAuthorities(userLoginDTO.getTenantId(), userDetails.getId()));
        });
        this.adminUserService.recordLoginSuccess(userTokenDTO.getUserId());
        return userTokenDTO;
    }

    @Override
    public UserTokenDTO login(UserLoginDTO userLoginDTO) {
        UserTokenDTO userTokenDTO = this.authentication(userLoginDTO, authentication -> {
            UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
            UserDTO userDetails = this.userService.findById(userAccountDetails.getUserId());
            return UserAccountAuthenticationToken.authenticated(authentication.getPrincipal(),
                    authentication.getCredentials(), userDetails, this.getLoginUserDetailDTO(userLoginDTO));
        });
        this.userService.recordLoginSuccess(userTokenDTO.getUserId());
        return userTokenDTO;
    }

    @Override
    public void logout() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        String authorization = request.getAuthorization();
        String token = authorization.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
        this.tokenStore.removeAuth2Token(token);
    }

    @Override
    public UserTokenDTO refreshToken(String refreshToken) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new RefreshAuthenticationProvider.RefreshAuthenticationToken(refreshToken));
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 refreshToken 方式登录失败: {} ",
                    refreshToken, e.getMessage(), e);
            throw e;
        }
        return this.doLogin(authentication, null);
    }

    @Override
    public UserTokenDTO loginBindThirdParty(ThirdPartyBindPlatformDTO thirdPartyBindPlatformDTO) {
        String thirdPartyUserId = (String) this.cacheService.get(CacheConstant.USER_BIND_TOKEN + thirdPartyBindPlatformDTO.getTemporaryToken());
        if (Objects.isNull(thirdPartyUserId)) {
            throw new ServiceException(AuthenticationResponseStatusEnum.THIRD_PARTY_ACCOUNT_BIND_EXPIRE);
        }
        AccountDTO account = this.accountService.findById(new AccountIdDTO(thirdPartyBindPlatformDTO.getTenantId(), thirdPartyUserId, AccountTypeEnum.WELINK));
        if (Objects.nonNull(account)) {
            throw new ServiceException(AuthenticationResponseStatusEnum.THIRD_PARTY_ACCOUNT_BINDING);
        }

        UserTokenDTO userTokenDTO = this.login(thirdPartyBindPlatformDTO);
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        // 保存第三方账号信息
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccount(thirdPartyUserId);
        // 目前只有weLink
        accountDTO.setAccountType(AccountTypeEnum.WELINK.name());
        accountDTO.setUserId(userId);
        accountDTO.setRefAccountId(thirdPartyUserId);
        accountDTO.setIsEnabled(true);
        this.accountService.saveAccounts(accountDTO);
        cacheService.delete(CacheConstant.USER_BIND_TOKEN + thirdPartyBindPlatformDTO.getTemporaryToken());
        return userTokenDTO;

    }

    /**
     * 用户认证
     *
     * @param userLoginDTO           用户登录的dto对象
     * @param authenticationCallback 回调
     * @return 用户token dto对象
     */
    private UserTokenDTO authentication(UserLoginDTO userLoginDTO,
                                        Function<Authentication, UserAccountAuthenticationToken> authenticationCallback) {
        Request request = ContextHolderStrategy.getContext().getRequest();
        request.putAdditionalInformation(HttpRequestAdditionalConstant.TENANT_ADDITIONAL_INFORMATION_ID, userLoginDTO.getTenantId());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(userLoginDTO.getAccountType().getAuthenticationToken(userLoginDTO));
        } catch (DisabledException e) {
            // 账号被禁用
            throw new ServiceException(AuthenticationResponseStatusEnum.USER_ACCOUNT_DISABLED);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 {} 方式登录失败: {} ",
                    userLoginDTO.getAccount(), userLoginDTO.getAccountType(), e.getMessage(), e);
            throw e;

        }
        return this.doLogin(authentication, authenticationCallback);
    }

    /**
     * 用户登录
     *
     * @param authentication         authentication
     * @param authenticationCallback callback
     * @return 用户账号详情
     */
    private UserTokenDTO doLogin(Authentication authentication,
                                 Function<Authentication, UserAccountAuthenticationToken> authenticationCallback) {

        UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
        userAccountDetails.eraseCredentials();
        if (Objects.nonNull(authenticationCallback)) {
            authentication = authenticationCallback.apply(authentication);
        }
        UserTokenDTO userTokenDTO = TOKEN_ASSEMBLER.toUserTokenDTO(tokenStore.createToken(authentication));
        userTokenDTO.setUserId(userAccountDetails.getUserId());
        return userTokenDTO;
    }

    /**
     * 获取登录的详情信息
     *
     * @param userLoginDTO 登录dto
     * @return 登录的用户详情dto对象
     */
    private LoginUserDetailDTO getLoginUserDetailDTO(UserLoginDTO userLoginDTO) {
        Request request = ContextHolderStrategy.getContext().getRequest();
        LoginUserDetailDTO loginUserDetailDTO = new LoginUserDetailDTO();
        String location = request.getAdditionalInformation(HttpRequestAdditionalConstant.IP_LOCATION_ADDITIONAL_INFORMATION);
        UserAgent userAgent = UserAgentUtil.parse(request.getUserAgent());
        loginUserDetailDTO.setLoginTime(LocalDateTime.now());
        loginUserDetailDTO.setLoginIp(request.getRequestIp());
        loginUserDetailDTO.setLoginLocation(location);
        loginUserDetailDTO.setLoginAccount(userLoginDTO.getAccount());
        loginUserDetailDTO.setLoginType(userLoginDTO.getAccountType().name());
        loginUserDetailDTO.setLoginTenantId(userLoginDTO.getTenantId());
        loginUserDetailDTO.setOs(userAgent.getOs().getName());
        loginUserDetailDTO.setBrowser(userAgent.getBrowser().getName());
        loginUserDetailDTO.setBrowserVersion(userAgent.getBrowser().getVersion(request.getUserAgent()));
        loginUserDetailDTO.setEngine(userAgent.getEngine().getName());
        loginUserDetailDTO.setPlatform(userAgent.getPlatform().getName());

        return loginUserDetailDTO;
    }

}
