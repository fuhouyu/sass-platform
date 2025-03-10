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
package com.fuhouyu.sass.platform.system.dto.user;

import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户查询的vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/10 21:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(name = "UserPageQueryDTO", description = "管理员用户查询dto对象")
public class AdminUserPageQueryDTO extends PageQueryDTO {

    @Schema(name = "username", description = "关键字模糊搜索用户名")
    private String username;

    @Schema(name = "gender", description = "性别筛选项")
    private String gender;

    @Schema(name = "organizationId", description = "所属组织id")
    private Long organizationId;
}
