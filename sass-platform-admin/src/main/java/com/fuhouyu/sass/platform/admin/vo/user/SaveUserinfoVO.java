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
package com.fuhouyu.sass.platform.admin.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * 保存用户的详情信息
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/9 21:18
 */
@Schema(name = "SaveUserinfoVO", description = "保存用户的详情")
@Getter
@Setter
@ToString
public class SaveUserinfoVO extends UserinfoVO {

    @Serial
    private static final long serialVersionUID = 8917239612498712387L;

    @Schema(name = "password", description = "用户密码")
    private String password;
}
