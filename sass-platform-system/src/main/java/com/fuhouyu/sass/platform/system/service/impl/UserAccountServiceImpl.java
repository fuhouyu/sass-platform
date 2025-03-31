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

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.function.Callback;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.security.core.provider.refreshtoken.RefreshAuthenticationProvider;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.system.assembler.TokenAssembler;
import com.fuhouyu.sass.platform.system.constants.CacheConstant;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.ThirdPartyBindPlatformDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.UserAccountDetails;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.AdminUserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserLoginDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.admin.UserTokenDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.AdminUserService;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import com.fuhouyu.sass.platform.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

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


    @Override
    public UserTokenDTO adminLogin(UserLoginDTO userLoginDTO) {
        return this.authentication(userLoginDTO, authentication -> {
            UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
            // 管理员用户
            AdminUserDTO adminUserDTO = this.adminUserService.findById(userAccountDetails.getUserId());
            ((UsernamePasswordAuthenticationToken) authentication)
                    .setDetails(adminUserDTO);
            this.userService.recordLoginSuccess(userAccountDetails.getUserId());
        });
    }

    @Override
    public UserTokenDTO login(UserLoginDTO userLoginDTO) {
        return this.authentication(userLoginDTO, authentication -> {
            UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
            UserDTO userDTO = this.userService.findById(userAccountDetails.getUserId());
            ((UsernamePasswordAuthenticationToken) authentication)
                    .setDetails(userDTO);
            this.userService.recordLoginSuccess(userAccountDetails.getUserId());
        });
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
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 refreshToken 方式登录失败: {} ",
                    refreshToken, e.getMessage(), e);
            throw new ServiceException(
                    ResponseStatusEnum.SERVER_ERROR,
                    "登录失败");
        }
        return this.doLogin(authentication, null);
    }

    @Override
    public UserTokenDTO loginBindThirdParty(ThirdPartyBindPlatformDTO thirdPartyBindPlatformDTO) {
        String thirdPartyUserId = (String) this.cacheService.get(CacheConstant.USER_BIND_TOKEN + thirdPartyBindPlatformDTO.getTemporaryToken());
        if (Objects.isNull(thirdPartyUserId)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "用户绑定信息已过期");
        }
        AccountDTO account = this.accountService.findById(new AccountIdDTO(thirdPartyUserId, AccountTypeEnum.WELINK));
        if (Objects.nonNull(account)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前第三方账号已绑定");
        }

        UserTokenDTO userTokenDTO = this.login(thirdPartyBindPlatformDTO);
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        // 保存第三方账号信息
        AccountDTO accountDTO = new AccountDTO();
        AccountDTO weLinkAccount = this.accountService.findAccountByUserIdAndType(userId, AccountTypeEnum.WELINK);
        if (Objects.nonNull(weLinkAccount)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前账号已绑定WeLink账号，请解绑后重试");
        }
        accountDTO.setAccount(thirdPartyUserId);
        // 目前只有weLink
        accountDTO.setAccountType(AccountTypeEnum.WELINK.name());
        accountDTO.setUserId(userId);
        accountDTO.setRefAccountId(thirdPartyUserId);
        accountDTO.setIsEnabled(true);
        this.accountService.save(accountDTO);
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
                                        Callback<Authentication> authenticationCallback) {
        Request request = ContextHolderStrategy.getContext().getRequest();
        request.putAdditionalInformation(HttpRequestAdditionalConstant.TENANT_ADDITIONAL_INFORMATION_ID, userLoginDTO.getTenantId());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(userLoginDTO.getAccountType().getAuthenticationToken(userLoginDTO));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 {} 方式登录失败: {} ",
                    userLoginDTO.getAccount(), userLoginDTO.getAccountType(), e.getMessage(), e);
            if (Objects.equals(userLoginDTO.getAccountType(), AccountTypeEnum.PASSWORD)) {
                throw new ServiceException(
                        ResponseStatusEnum.INVALID_PARAM,
                        "用户名或密码错误");
            }
            throw new ServiceException(
                    ResponseStatusEnum.SERVER_ERROR,
                    "登录失败");

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
                                 Callback<Authentication> authenticationCallback) {

        UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
        userAccountDetails.eraseCredentials();
        if (Objects.nonNull(authenticationCallback)) {
            authenticationCallback.call(authentication);
        }
        // 设置上下文信息
        UserEntity userEntity = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(authentication.getDetails(),
                UserEntity.class));
        DefaultListableContextFactory context = (DefaultListableContextFactory) ContextHolderStrategy.getContext();
        context.setUser(userEntity);
        return TOKEN_ASSEMBLER.toUserTokenDTO(tokenStore.createToken(authentication));
    }

}
