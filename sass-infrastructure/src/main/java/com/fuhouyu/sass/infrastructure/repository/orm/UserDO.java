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
package com.fuhouyu.sass.infrastructure.repository.orm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户do对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/24 19:22
 */
@Getter
@Setter
@ToString
public class UserDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 性别
     */
    private String gender;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 是否删除标记
     */
    private Boolean isDeleted;
}
