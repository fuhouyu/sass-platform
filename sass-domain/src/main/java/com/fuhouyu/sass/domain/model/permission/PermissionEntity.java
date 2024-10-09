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
package com.fuhouyu.sass.domain.model.permission;

import com.fuhouyu.sass.domain.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * 权限实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:12
 */
@Getter
@Setter
@ToString
public class PermissionEntity extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 5412364211236412389L;

    private Long id;

    private Long parentId;

    private String permissionName;

    private String permissionCode;

    private String permissionType;

    private Integer displayOrder;

    private String icon;

    private String routePath;

    private String componentPath;

    private String urlParams;

    private Boolean isFrame;

    private Boolean isAllowModified;

    private Boolean isSystemd;

    private Boolean isVisible;


    @Override
    public Long getIdentifierId() {
        return this.id;
    }

    @Override
    public boolean sameIdentityAs(Long other) {
        return this.id.equals(other);
    }
}
