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
package com.fuhouyu.sass.platform.system.annotaions;

import java.lang.annotation.*;

/**
 * <p>
 * 该注解是为了从tmp目录下的临时资源移动到对应的业务下
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/9 19:27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MoveTempResource {

    /**
     * 业务名称，将临时文件目录移动到业务目录下
     *
     * @return 业务名称
     */
    String businessName();
}
