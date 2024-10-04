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
package com.fuhouyu.sass.common;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 基础的查询model对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:49
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery implements Serializable {

    private int pageNumber;

    private int pageSize;


}
