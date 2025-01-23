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

export interface RegexInterface {
    regex: RegExp
    message: string
}

/**
 * 密码正则
 */
const PASSWORD_REGEX: RegexInterface = {
    regex: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,20}$/,
    message: "密码必须包含大写字母、小写字母、数字和特殊字符，长度为8到20个字符。",
}

/**
 * 用户名正则
 */
const USERNAME_REGEX: RegexInterface = {
    regex: /^[a-zA-Z][a-zA-Z0-9_]{2,19}$/,
    message: "用户名格式不正确，必须以字母开头，并使用3到20个字符，仅包含字母、数字和下划线。"
}


export {
    USERNAME_REGEX,
    PASSWORD_REGEX,
}