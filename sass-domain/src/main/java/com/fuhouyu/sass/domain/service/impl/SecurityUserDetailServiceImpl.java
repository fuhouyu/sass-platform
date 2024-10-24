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
package com.fuhouyu.sass.domain.service.impl;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.sass.domain.assembler.SecurityUserDetailAssembler;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.account.AccountIdEntity;
import com.fuhouyu.sass.domain.model.user.SecurityUserDetailEntity;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.domain.repository.AccountRepository;
import com.fuhouyu.sass.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * oauth用户详情接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 10:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityUserDetailServiceImpl implements UserDetailsService {

    private static final SecurityUserDetailAssembler USER_DETAIL_ASSEMBLER = SecurityUserDetailAssembler.INSTANCE;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountIdEntity accountIdEntity = AccountIdEntity.fullAccountToAccountIdEntity(username);
        AccountEntity accountEntity = this.accountRepository.findById(accountIdEntity);
        if (Objects.isNull(accountEntity)) {
            LoggerUtil.warn(log, "{} login failed,未找到对应账号", username);
            throw new UsernameNotFoundException(
                    String.format("%s 用户未找到", accountIdEntity.getAccount()));
        }
        UserEntity userEntity = this.userRepository.findById(accountEntity.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException(String.format("%s 账号关联的用户不存在", accountIdEntity.getAccount()));
        }
        SecurityUserDetailEntity authUserDetailEntity = USER_DETAIL_ASSEMBLER.toAuthUserDetailEntity(userEntity);
        authUserDetailEntity.setAccount(accountEntity);
        return authUserDetailEntity;
    }
}
