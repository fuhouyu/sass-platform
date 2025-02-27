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
package com.fuhouyu.sass.platform.system.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 临时token生成的请求
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/27 20:27
 */
@Data
@Schema(name = "StsTemporaryTokenRequestDTO", description = "临时token生成的请求类")
public class StsTemporaryTokenRequestDTO implements Serializable {

    @Schema(name = "prefix", description = "前缀名称")
    private String prefix;

    @Schema(name = "fileNames", description = "文件名称集合")
    @NotEmpty(message = "文件名称未输入")
    private List<String> fileNames;
}
