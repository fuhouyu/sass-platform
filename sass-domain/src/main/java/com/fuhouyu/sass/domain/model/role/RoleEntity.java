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
package com.fuhouyu.sass.domain.model.role;

import com.fuhouyu.sass.domain.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;


/**
 * <p>
 * 角色实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 20:50
 */
@Getter
@Setter
@ToString
public class RoleEntity extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6123532123415612311L;

    private Long id;

    private String roleName;

    private String roleCode;

    private Integer displayOrder;

    private String dataScope;

    private Boolean isSystemd;

    private Boolean isEnabled;

    private Boolean isAllowModified;

    @Override
    public Long getIdentifierId() {
        return this.id;
    }

}
