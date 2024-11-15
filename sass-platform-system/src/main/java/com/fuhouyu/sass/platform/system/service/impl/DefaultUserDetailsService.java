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
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.sass.platform.system.assembler.SecurityUserDetailAssembler;
import com.fuhouyu.sass.platform.system.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.entity.AccountIdDTO;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
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
        AccountIdDTO accountIdDTO = new AccountIdDTO(account, accountType);
        AccountDTO accountDTO = this.accountService.findById(accountIdDTO);
        if (Objects.isNull(accountDTO)) {
            LoggerUtil.warn(log, "account: {}, accountType:{} 登录失败,未找到对应账号", account, accountType);
            throw new ServiceException(ResponseStatusEnum.NOT_AUTH, "用户名或密码错误");
        }
        return SecurityUserDetailAssembler.INSTANCE.toSecurityUserDetail(accountDTO);
    }
}
