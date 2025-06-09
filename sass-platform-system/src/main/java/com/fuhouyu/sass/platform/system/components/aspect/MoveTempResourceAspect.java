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
package com.fuhouyu.sass.platform.system.components.aspect;

import com.fuhouyu.sass.platform.system.annotaions.MoveTempResource;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p>
 * 目录资源移动的切面拦截处理
 * </p>
 *
 * @author fuhouyu
 * @since 2025/6/9 19:34
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MoveTempResourceAspect {

    private final ResourceService resourceService;

    /**
     * 登录切面
     */
    @Pointcut("""
            execution(public * com.fuhouyu.sass.platform.system.service..*.*(..)) &&
            (
            execution(* *save*(..)) || execution(* *add*(..)) || execution(* *insert*(..)) ||
            execution(* *update*(..)) || execution(* *edit*(..))
            )
            """)
    public void moveTempResource() {
    }


    /**
     * 移动临时资源到业务数据下
     *
     * @param joinPoint 切面
     * @return void
     * @throws Throwable 异常信息
     */
    @Around("moveTempResource()")
    public Object aroundLoginMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (Objects.isNull(arg)) {
                continue;
            }
            Class<?> clazz = arg.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                MoveTempResource moveTempResource = field.getAnnotation(MoveTempResource.class);
                if (Objects.isNull(moveTempResource)) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(arg);
                if (value instanceof Long resourceId) {
                    this.moveResource(resourceId, moveTempResource.businessName());
                }
            }
        }

        return joinPoint.proceed();
    }


    /**
     * 移动资源到业务目录下
     *
     * @param resourceId   资源id
     * @param businessName 业务名称
     */
    private void moveResource(Long resourceId, String businessName) {
        this.resourceService.editTempResource(resourceId, businessName);
    }
}
