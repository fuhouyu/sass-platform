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
package com.fuhouyu.sass.platform.system.domain.dto.user.admin;

import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 用户详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/9 21:21
 */
@Getter
@Setter
@ToString
@Schema(name = "AdminUserDetailDTO", description = "管理员用户详情dto对象")
public class AdminUserDetailDTO extends AdminUserDTO {

    @Valid
    @Schema(name = "account", description = "用户账号信息")
    private AccountDTO account;

    @Valid
    @Schema(name = "userPosition", description = "用户职位信息")
    private UserPositionDTO userPosition;

    @Schema(name = "roleIds", description = "用户角色")
    private List<Long> roleIds;
}
