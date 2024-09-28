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
package com.fuhouyu.tenant.infrastructure.repository.assembler;

import com.fuhouyu.tenant.domain.model.tenant.TenantEntity;
import com.fuhouyu.tenant.infrastructure.repository.orm.TenantDO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 租户model与do的互换转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/21 16:29
 */
@Mapper(builder = @Builder(disableBuilder = true))
public interface TenantAssembler extends BaseAssembler<TenantEntity, TenantDO> {

    TenantAssembler INSTANCE = Mappers.getMapper(TenantAssembler.class);

}
