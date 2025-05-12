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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.system.components.security.UserAccountAuthenticationToken;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.LoginUserDetailDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.OnlineUserPageQueryDTO;
import com.fuhouyu.sass.platform.system.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 在线用户实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/23 20:30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OnlineUserServiceImpl implements OnlineUserService {

    private final TokenStore tokenStore;

    @Override
    public PageResultDTO<LoginUserDetailDTO> onlineUserList(OnlineUserPageQueryDTO onlineUserPageQueryDTO) {
        Set<String> keys = this.tokenStore.getTokens();
        if (CollectionUtils.isEmpty(keys)) {
            return new PageResultDTO<>(0L, 0L, 0L);
        }
        LoggerUtil.info(log, "当前在线用户数量:{}", keys.size());
        int pageSize = Math.toIntExact(onlineUserPageQueryDTO.getPageSize());
        List<LoginUserDetailDTO> loginUserDetailList = new ArrayList<>(pageSize);
        for (String key : keys) {

            UserAccountAuthenticationToken authentication = (UserAccountAuthenticationToken) this.tokenStore.readAuthentication(key);
            LoginUserDetailDTO loginUserDetails = authentication.getLoginUserDetails();
            if (!checkLoginUserDetailDTO(loginUserDetails, onlineUserPageQueryDTO)) {
                continue;
            }
            loginUserDetails.setAccessToken(key);
            loginUserDetailList.add(loginUserDetails);
        }
        loginUserDetailList.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        int pageNum = Math.toIntExact(onlineUserPageQueryDTO.getPageNum());
        List<LoginUserDetailDTO> records = CollUtil.page(ObjectUtil.defaultIfNull(pageNum, 1) - 1, pageSize, loginUserDetailList);
        return new PageResultDTO<>(onlineUserPageQueryDTO.getPageNum(), onlineUserPageQueryDTO.getPageSize(),
                (long) keys.size(), records);
    }

    @Override
    public void forceLogout(List<String> sessionIds) {
        for (String sessionId : sessionIds) {
            this.tokenStore.removeAuth2Token(sessionId);
        }
    }

    /**
     * 校验登录用户详情
     * @param loginUserDetails 登录用户详情
     * @param onlineUserPageQueryDTO 在线用户分页查询
     * @return true 校验通过 false 校验不通过
     */
    private boolean checkLoginUserDetailDTO(LoginUserDetailDTO loginUserDetails, OnlineUserPageQueryDTO onlineUserPageQueryDTO) {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        if (!Objects.equals(loginUserDetails.getLoginTenantId(), tenantId)) {
            return false;
        }
        LocalDateTime startTime = onlineUserPageQueryDTO.getStartTime();
        if (Objects.nonNull(startTime) && startTime.isBefore(loginUserDetails.getLoginTime())) {
            return false;
        }
        LocalDateTime endTime = onlineUserPageQueryDTO.getEndTime();
        if (Objects.nonNull(endTime) && endTime.isAfter(loginUserDetails.getLoginTime())) {
            return false;
        }
        String account = onlineUserPageQueryDTO.getLoginAccount();
        return !StringUtils.hasText(account) || loginUserDetails.getLoginAccount().contains(account);
    }
}
