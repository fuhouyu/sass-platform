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
package com.fuhouyu.sass.infrastructure.repository.handler;

import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.sass.infrastructure.repository.orm.BaseDO;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class BaseDOValueHandle implements Interceptor {

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
                && list.get(0) instanceof BaseDO) {
            List<? extends BaseDO> baseDOList = (List<? extends BaseDO>) parameter;
            this.doHandlerBaseDO(baseDOList, sqlCommandType);
        } else if (parameter instanceof BaseDO baseDO) {
            this.doHandlerBaseDO(baseDO, sqlCommandType);
        }
    }

    private void doHandlerBaseDO(BaseDO baseDO, SqlCommandType sqlCommandType) {
        if (sqlCommandType == SqlCommandType.INSERT) {
            this.onInsert(baseDO);
        } else {
            this.onUpdate(baseDO);
        }
    }

    private void doHandlerBaseDO(List<? extends BaseDO> baseDOList, SqlCommandType sqlCommandType) {
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
    private void onInsert(BaseDO baseDO) {
        LocalDateTime nowTime = LocalDateTime.now();
        String username = UserContextHolder.getContext().getObject().getUsername();
        baseDO.setCreateAt(nowTime);
        baseDO.setUpdateAt(nowTime);
        baseDO.setCreateBy(username);
        baseDO.setUpdateBy(username);
        baseDO.setIsDeleted(false);
    }

    /**
     * 修改时更新
     *
     * @param baseDO baseDO对象
     */
    private void onUpdate(BaseDO baseDO) {
        LocalDateTime nowTime = LocalDateTime.now();
        String username = UserContextHolder.getContext().getObject().getUsername();
        baseDO.setUpdateAt(nowTime);
        baseDO.setUpdateBy(username);
    }
}
