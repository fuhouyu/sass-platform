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
package com.fuhouyu.tenant.domain.repository;

import com.fuhouyu.tenant.domain.model.user.UserEntity;

/**
 * <p>
 * 用户存储层接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 19:10
 */
public interface UserRepository extends BaseRepository<UserEntity, Long> {


}
