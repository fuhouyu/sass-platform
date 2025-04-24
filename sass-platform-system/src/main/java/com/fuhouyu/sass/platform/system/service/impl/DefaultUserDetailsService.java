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

import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.system.assembler.SecurityUserDetailAssembler;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.enums.response.AuthenticationResponseStatusEnum;
import com.fuhouyu.sass.platform.system.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户详情实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 23:38
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultUserDetailsService implements ExtensionUserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.loadUserByUsername(username, AccountTypeEnum.PASSWORD.name());
    }

    @Override
    public UserDetails loadUserByUsername(String account, String accountType) throws UsernameNotFoundException {
        Long tenantId = ContextHolderStrategy.getContext().getRequest().getAdditionalInformation(HttpRequestAdditionalConstant.TENANT_ADDITIONAL_INFORMATION_ID);
        AccountIdDTO accountIdDTO = new AccountIdDTO(tenantId, account, AccountTypeEnum.valueOf(accountType));
        AccountDTO accountDTO = this.accountService.findById(accountIdDTO);
        if (Objects.isNull(accountDTO)) {
            // 如果是密码，抛出异常
            if (Objects.equals(accountType, AccountTypeEnum.PASSWORD.name())) {
                throw new ServiceException(AuthenticationResponseStatusEnum.USER_NOT_IN_TENANT);
            } else {
                return null;
            }
        }
        return SecurityUserDetailAssembler.INSTANCE.toSecurityUserDetail(accountDTO);
    }
}
