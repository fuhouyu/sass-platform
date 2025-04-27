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
package com.fuhouyu.sass.platform.system.service;

import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeCallbackDTO;

/**
 * <p>
 * office相关接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/26 22:53
 */
public interface OnlyOfficeService {

    /**
     * 保存文件
     *
     * @param id                    文件id
     * @param token                 会话id
     * @param onlyOfficeCallbackDTO office回调dto对象
     */
    void saveFile(Long id, String token,
                  OnlyOfficeCallbackDTO onlyOfficeCallbackDTO);
}
