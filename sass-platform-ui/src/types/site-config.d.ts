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
/**
 * 网站设置的 DTO 对象
 */
export interface ISiteConfig {
  /** 主键ID */
  id?: number;

  /** 网站名称 */
  siteName?: string;

  /** 网站描述（用于SEO） */
  siteDescription?: string;

  /** 网站Logo文件ID */
  siteLogo?: number;

  /** favicon.ico 地址 */
  siteFavicon?: string;

  /** 联系邮箱 */
  contactEmail?: string;

  /** 联系电话 */
  contactPhone?: string;

  /** 联系地址 */
  contactAddress?: string;

  /** ICP备案号 */
  icpNumber?: string;

  /** 备案跳转链接 */
  beianUrl?: string;

  /** 默认语言 */
  languageDefault?: string;

  /** 默认时区 */
  timezone?: string;
}

