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
package com.fuhouyu.sass.platform.system.enums;

import com.fuhouyu.framework.security.core.provider.refreshtoken.RefreshAuthenticationProvider;
import com.fuhouyu.sass.platform.system.core.security.provider.WeLinkAuthenticationProvider;
import com.fuhouyu.sass.platform.system.dto.user.UserLoginDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * <p>
 * 账号类型枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/9 21:27
 */
public enum AccountTypeEnum {

    PASSWORD {
        @Override
        public AbstractAuthenticationToken getAuthenticationToken(UserLoginDTO userLoginDTO) {
            return new UsernamePasswordAuthenticationToken(userLoginDTO.getAccount(), userLoginDTO.getCredentials());
        }
    },

    REFRESH_TOKEN {
        @Override
        public AbstractAuthenticationToken getAuthenticationToken(UserLoginDTO userLoginDTO) {
            return new RefreshAuthenticationProvider.RefreshAuthenticationToken(userLoginDTO.getAccount());
        }
    },

    WELINK {
        @Override
        public AbstractAuthenticationToken getAuthenticationToken(UserLoginDTO userLoginDTO) {
            return new WeLinkAuthenticationProvider.WeLinkAuthenticationToken(userLoginDTO.getAccount());
        }
    }
    ;

    public abstract AbstractAuthenticationToken getAuthenticationToken(UserLoginDTO userLoginDTO);

}
