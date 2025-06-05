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
package com.fuhouyu.sass.platform.system.domain.dto.site;

import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网站设置dto
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/3 22:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SiteSettingDTO", description = "网站设置的dto对象")
public class SiteConfigDTO extends BaseDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "网站名称", example = "我的企业官网")
    private String siteName;

    @Schema(description = "网站描述（用于SEO）", example = "这是一个专注于企业服务的网站")
    private String siteDescription;

    @Schema(description = "网站Logo文件ID", example = "123456789")
    private Long siteLogo;

    @Schema(description = "favicon.ico 地址", example = "https://cdn.example.com/favicon.ico")
    private String siteFavicon;

    @Schema(description = "联系邮箱", example = "contact@example.com")
    private String contactEmail;

    @Schema(description = "联系电话", example = "400-800-1234")
    private String contactPhone;

    @Schema(description = "联系地址", example = "上海市浦东新区张江高科")
    private String contactAddress;

    @Schema(description = "ICP备案号", example = "沪ICP备12345678号")
    private String icpNumber;

    @Schema(description = "备案跳转链接", example = "https://beian.miit.gov.cn")
    private String beianUrl;

    @Schema(description = "默认语言", example = "zh-CN")
    private String languageDefault;

    @Schema(description = "默认时区", example = "Asia/Shanghai")
    private String timezone;
}
