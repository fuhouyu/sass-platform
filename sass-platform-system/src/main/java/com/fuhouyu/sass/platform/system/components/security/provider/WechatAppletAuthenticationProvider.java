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
package com.fuhouyu.sass.platform.system.components.security.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.system.assembler.SecurityUserDetailAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserDTO;
import com.fuhouyu.sass.platform.system.domain.dto.wechat.WechatAppletSessionDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.UserTypeEnum;
import com.fuhouyu.sass.platform.system.service.AccountService;
import com.fuhouyu.sass.platform.system.service.UserService;
import com.fuhouyu.sass.platform.system.service.WechatAppletService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 微信小程序
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 21:31
 */
@RequiredArgsConstructor
@Component
public class WechatAppletAuthenticationProvider implements AuthenticationProvider {

    private final WechatAppletService wechatAppletService;

    private final ExtensionUserDetailsService userDetailsService;

    private final UserService userService;

    private final AccountService accountService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        WechatAppletSessionDTO wechatAppletSessionDTO = this.wechatAppletService.code2Session(code);
        String openid = wechatAppletSessionDTO.getOpenid();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(openid, AccountTypeEnum.WECHAT_APPLET.name());
        if (Objects.isNull(userDetails)) {
            // 如果不存在，新增一个普通用户
            Long tenantId = ContextHolderStrategy.getContext().getRequest().getAdditionalInformation(HttpRequestAdditionalConstant.TENANT_ADDITIONAL_INFORMATION_ID);
            UserDTO userDTO = UserDTO
                    .builder()
                    .gender("UNKNOWN")
                    .isEnabled(true)
                    .build();
            userDTO.setCreatedBy(openid);
            userDTO.setUsername(this.userService.generateUsername());
            userDTO.setOwnerTenantId(tenantId);
            userDTO.setUpdatedBy(openid);
            Long userId = this.userService.save(userDTO);
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setAccount(openid);
            accountDTO.setAccountType(AccountTypeEnum.WECHAT_APPLET.name());
            accountDTO.setUserId(userId);

            accountDTO.setOwnerTenantId(tenantId);
            accountDTO.setRefAccountId(wechatAppletSessionDTO.getUnionid());
            accountDTO.setIsEnabled(true);
            accountDTO.setUserType(UserTypeEnum.NORMAL);
            accountDTO.setCreatedBy(userDTO.getUsername());
            accountDTO.setUpdatedBy(userDTO.getUsername());
            this.accountService.save(accountDTO);
            userDetails = SecurityUserDetailAssembler.INSTANCE.toSecurityUserDetail(accountDTO);
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), List.of());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAppletAuthenticationProvider.WechatAppletAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @EqualsAndHashCode(callSuper = true)
    public static class WechatAppletAuthenticationToken extends AbstractAuthenticationToken {

        private final String code;

        /**
         * 构造函数
         *
         * @param code 授权码
         */
        @JsonCreator
        public WechatAppletAuthenticationToken(
                @JsonProperty("code") String code) {
            super(List.of());
            this.code = code;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return this.code;
        }
    }
}
