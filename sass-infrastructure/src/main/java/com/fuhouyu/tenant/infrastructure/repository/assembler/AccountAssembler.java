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

import com.fuhouyu.tenant.domain.model.account.AccountEntity;
import com.fuhouyu.tenant.infrastructure.repository.orm.AccountDO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 账号转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 23:34
 */
@Mapper(builder = @Builder(disableBuilder = true))
public interface AccountAssembler extends BaseAssembler<AccountEntity, AccountDO> {

    AccountAssembler INSTANCE = Mappers.getMapper(AccountAssembler.class);

    @Override
    @Mappings(value = {
            @Mapping(source = "accountIdEntity.account", target = "account"),
            @Mapping(source = "accountIdEntity.accountType", target = "accountType"),
    })
    AccountDO toDO(AccountEntity source);

    @Override
    @Mappings(value = {
            @Mapping(source = "accountIdEntity.account", target = "account"),
            @Mapping(source = "accountIdEntity.accountType", target = "accountType"),
    })
    List<AccountDO> toDO(List<AccountEntity> sourceList);

    @Override
    @Mappings(value = {
            @Mapping(source = "account", target = "accountIdEntity.account"),
            @Mapping(source = "accountType", target = "accountIdEntity.accountType"),
    })
    AccountEntity toEntity(AccountDO source);

    @Override
    @Mappings(value = {
            @Mapping(source = "account", target = "accountIdEntity.account"),
            @Mapping(source = "accountType", target = "accountIdEntity.accountType"),
    })
    List<AccountEntity> toEntity(List<AccountDO> sourceList);

}
