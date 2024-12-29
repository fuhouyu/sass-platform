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
package com.fuhouyu.sass.platform.system.core.security.authority;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static com.fuhouyu.sass.platform.system.constants.CommonConstsant.USER_ADDITIONAL_INFORMATION_PERMISIONS;

/**
 * <p>
 * 权限拦截
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/27 21:04
 */
@Service("auth")
public class AuthorityFilter {

    /**
     * 判断是否存在权限
     *
     * @param permission 权限编码
     * @return true 存在 false 不存在
     */
    public boolean hasPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return true;
        }
        User user = ContextHolderStrategy.getContext().getUser();
        Collection<? extends GrantedAuthority> permissions = user.getAdditionalInformation(USER_ADDITIONAL_INFORMATION_PERMISIONS);
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        return permissions.stream().map(GrantedAuthority::getAuthority)
                .anyMatch(permission::equals);
    }

}
