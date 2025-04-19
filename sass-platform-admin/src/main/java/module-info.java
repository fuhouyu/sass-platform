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
/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/11 20:18
 */module sass.platform.admin {
    requires base.framework.cache.starter;
    requires base.framework.common;
    requires base.framework.context;
    requires base.framework.log.starter;
    requires base.framework.security.starter;
    requires base.framework.web.starter;
    requires com.github.oshi;
    requires docs.integration.sdk;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.annotation;
    requires jakarta.servlet;
    requires jakarta.validation;
    requires static lombok;
    requires org.apache.commons.io;
    requires org.aspectj.weaver;
    requires org.mybatis.spring;
    requires org.springdoc.openapi.common;
    requires reactor.core;
    requires sass.platform.common;
    requires sass.platform.system;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.oauth2.core;
    requires spring.security.web;
    requires spring.web;

    exports com.fuhouyu.sass.platform.admin;
    exports com.fuhouyu.sass.platform.admin.annotaions;
    exports com.fuhouyu.sass.platform.admin.aspect;
    exports com.fuhouyu.sass.platform.admin.configuration;
    exports com.fuhouyu.sass.platform.admin.constants;
    exports com.fuhouyu.sass.platform.admin.controller;
    exports com.fuhouyu.sass.platform.admin.filter;
    exports com.fuhouyu.sass.platform.admin.response;
    exports com.fuhouyu.sass.platform.admin.enums;

    opens com.fuhouyu.sass.platform.admin.configuration to spring.core, spring.beans, spring.context;
    opens com.fuhouyu.sass.platform.admin.response to com.fasterxml.jackson.databind;
    opens com.fuhouyu.sass.platform.admin.controller to spring.core;
    opens com.fuhouyu.sass.platform.admin.filter to spring.beans;
    opens com.fuhouyu.sass.platform.admin to spring.beans;


}