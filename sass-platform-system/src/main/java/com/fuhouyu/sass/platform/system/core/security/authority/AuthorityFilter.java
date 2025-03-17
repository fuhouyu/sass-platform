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
package com.fuhouyu.sass.platform.system.core.security.authority;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.fuhouyu.sass.platform.system.constants.CommonConstant.USER_ADDITIONAL_INFORMATION_PERMISSIONS;

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
     * 判断是否存在权限，存在任一一个权限就返回true
     *
     * @param permissions 权限编码
     * @return true/false
     */
    public boolean hasAnyPermission(String... permissions) {
        return this.doCheckPermissions(userPermissionSet -> Arrays.stream(permissions).anyMatch(userPermissionSet::contains), permissions);
    }


    /**
     * 判断是否存在所有权限
     *
     * @param permissions 权限
     * @return true/false
     */
    public boolean hasAllPermission(String... permissions) {
        return this.doCheckPermissions(userPermissionSet -> Arrays.stream(permissions).allMatch(userPermissionSet::contains), permissions);

    }

    /**
     * 检查权限逻辑
     *
     * @param checkPermissionLogic 权限逻辑
     * @param permissions          权限数组
     * @return true false
     */
    private boolean doCheckPermissions(Predicate<Set<String>> checkPermissionLogic, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        User user = ContextHolderStrategy.getContext().getUser();
        Collection<? extends GrantedAuthority> userPermissions = user.getAdditionalInformation(USER_ADDITIONAL_INFORMATION_PERMISSIONS);
        if (CollectionUtils.isEmpty(userPermissions)) {
            return false;
        }
        Set<String> userPermissionSet = userPermissions.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return checkPermissionLogic.test(userPermissionSet);
    }
}
