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
package com.fuhouyu.sass.platform.system.core.handle;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.system.domain.entity.BaseEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
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
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        }
)
@Component
public class BaseEntityHandle implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (parameter instanceof Map<?, ?> map) {
            Set<Object> set = new HashSet<>(map.size());
            map.forEach((k, v) -> {
                set.add(v);
            });
            set.forEach(v -> this.doHandlerParam(v, sqlCommandType));
        } else {
            this.doHandlerParam(parameter, sqlCommandType);
        }
        return invocation.proceed();
    }

    @SuppressWarnings("unchecked")
    private void doHandlerParam(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof List<?> list
                && list.getFirst() instanceof BaseEntity) {
            List<? extends BaseEntity> baseDOList = (List<? extends BaseEntity>) parameter;
            this.doHandlerBaseDO(baseDOList, sqlCommandType);
        } else if (parameter instanceof BaseEntity baseDO) {
            this.doHandlerBaseDO(baseDO, sqlCommandType);
        }
    }

    private void doHandlerBaseDO(BaseEntity baseDO, SqlCommandType sqlCommandType) {
        if (sqlCommandType == SqlCommandType.INSERT) {
            this.onInsert(baseDO);
        } else {
            this.onUpdate(baseDO);
        }
    }

    private void doHandlerBaseDO(List<? extends BaseEntity> baseDOList, SqlCommandType sqlCommandType) {
        if (sqlCommandType == SqlCommandType.INSERT) {
            baseDOList.forEach(this::onInsert);
        } else {
            baseDOList.forEach(this::onUpdate);
        }
    }



    /**
     * 创建时更新
     *
     * @param baseDO baseDO对象
     */
    private void onInsert(BaseEntity baseDO) {
        LocalDateTime nowTime = LocalDateTime.now();
        baseDO.setCreatedAt(nowTime);
        baseDO.setUpdatedAt(nowTime);
        baseDO.setCreatedBy(Objects.isNull(baseDO.getCreatedBy()) ?
                ContextHolderStrategy.getContext().getUser().getUsername()
                : baseDO.getCreatedBy()
        );
        baseDO.setUpdatedBy(Objects.isNull(baseDO.getUpdatedBy()) ?
                ContextHolderStrategy.getContext().getUser().getUsername()
                : baseDO.getUpdatedBy());
        baseDO.setIsDeleted(false);
    }

    /**
     * 修改时更新
     *
     * @param baseDO baseDO对象
     */
    private void onUpdate(BaseEntity baseDO) {
        LocalDateTime nowTime = LocalDateTime.now();
        baseDO.setUpdatedAt(nowTime);
        baseDO.setUpdatedBy(Objects.isNull(baseDO.getUpdatedBy()) ?
                ContextHolderStrategy.getContext().getUser().getUsername()
                : baseDO.getUpdatedBy());
    }
}
