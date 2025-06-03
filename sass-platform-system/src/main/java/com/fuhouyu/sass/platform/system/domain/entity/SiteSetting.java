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
package com.fuhouyu.sass.platform.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 网站设置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SiteSetting extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1541692736541253877L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 网站名称
     */
    private String siteName;

    /**
     * 网站描述（用于 SEO）
     */
    private String siteDescription;

    /**
     * 网站Logo文件ID（通常对应文件表的ID）
     */
    private Long siteLogo;

    /**
     * 浏览器小图标 favicon.ico 地址
     */
    private String siteFavicon;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * ICP备案号
     */
    private String icpNumber;

    /**
     * 备案跳转链接（通常是工信部网站）
     */
    private String beianUrl;

    /**
     * 默认语言（如 zh-CN、en-US）
     */
    private String languageDefault;

    /**
     * 默认时区（如 Asia/Shanghai）
     */
    private String timezone;

    /**
     * isDeleted 忽略
     */
    @TableField(exist = false)
    private Boolean isDeleted;

}
