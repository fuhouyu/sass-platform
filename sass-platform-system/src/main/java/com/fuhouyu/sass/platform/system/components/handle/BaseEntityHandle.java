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
package com.fuhouyu.sass.platform.system.components.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.system.domain.entity.BaseEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 基础do对象值处理
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/24 18:25
 */
@Component
public class BaseEntityHandle implements MetaObjectHandler {



    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime nowTime = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, nowTime);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, nowTime);
        this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);
        this.strictInsertFill(metaObject, "ownerTenantId", Long.class, ContextHolderStrategy.getContext().getUser().getTenantId());
        this.strictInsertFill(metaObject, "createdBy", String.class, getUsername(metaObject, "createdBy"));
        this.strictInsertFill(metaObject, "updatedBy", String.class, getUsername(metaObject, "updatedBy"));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime nowTime = LocalDateTime.now();
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, nowTime);
        this.strictUpdateFill(metaObject, "updatedBy", String.class, getUsername(metaObject, "updatedBy"));
    }


    /**
     * 获取用户名
     * @param metaObject metaObject
     * @param fieldName fieldName
     * @return 用户名
     */
    private String getUsername(MetaObject metaObject, String fieldName) {
        String username = ContextHolderStrategy.getContext().getUser().getUsername();
        String fieldValue = metaObject.findProperty(fieldName, true);
        return Objects.isNull(fieldValue) ? username : fieldValue;

    }
}
