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
package com.fuhouyu.sass.interfaces.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户详情修改命令
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 09:18
 */
@Schema(name = "UserinfoEditCommand", description = "用户详情修改操作")
@Data
public class UserinfoEditCommand implements Serializable {

    @Schema(name = "id", description = "用户id，如果修改当前自己用户详情，该值不需要传递")
    private Long id;

    @Schema(name = "realName", description = "用户真实姓名")
    private String realName;

    @Schema(name = "nickname", description = "昵称")
    private String nickname;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "gender", description = "性别")
    private String gender;

}
