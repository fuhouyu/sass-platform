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
package com.fuhouyu.sass.platform.system.domain.dto.user;

import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 在线用户的分页查询dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/25 21:46
 */
@Schema(name = "OnlineUserPageQueryDTO", description = "在线用户分页查询的dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineUserPageQueryDTO extends PageQueryDTO {

    @Schema(name = "loginAccount", description = "账号模糊查询")
    private String loginAccount;

    @Schema(name = "startTime", description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(name = "endTime", description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
