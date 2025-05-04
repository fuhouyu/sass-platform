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
 * module-info
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/11 19:03
 */

module sass.platform.system {

    requires static lombok;
    requires transitive sass.platform.common;
    requires base.framework.cache.starter;
    requires base.framework.common;
    requires base.framework.context;
    requires base.framework.database;
    requires base.framework.log.starter;
    requires base.framework.s3.starter;
    requires base.framework.security.starter;
    requires cn.hutool.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.github.oshi;
    requires docs.integration.sdk;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.annotation;
    requires jakarta.servlet;
    requires jakarta.validation;
    requires java.compiler;
    requires java.management;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.hibernate.validator;
    requires org.mapstruct;
    requires org.mybatis;
    requires pagehelper;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.utils;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.oauth2.core;
    requires spring.tx;
    requires spring.web;
    requires cn.hutool.http;
    requires transmittable.thread.local;

    exports com.fuhouyu.sass.platform.system.assembler;
    exports com.fuhouyu.sass.platform.system.constants;
    exports com.fuhouyu.sass.platform.system.components.config;
    exports com.fuhouyu.sass.platform.system.components.handle;
    exports com.fuhouyu.sass.platform.system.components.office;
    exports com.fuhouyu.sass.platform.system.components.security;
    exports com.fuhouyu.sass.platform.system.components.security.authority;
    exports com.fuhouyu.sass.platform.system.components.security.provider;
    exports com.fuhouyu.sass.platform.system.utils;
    exports com.fuhouyu.sass.platform.system.domain.dto;
    exports com.fuhouyu.sass.platform.system.domain.dto.account;
    exports com.fuhouyu.sass.platform.system.domain.dto.application;
    exports com.fuhouyu.sass.platform.system.domain.dto.cloudflare;
    exports com.fuhouyu.sass.platform.system.domain.dto.config;
    exports com.fuhouyu.sass.platform.system.domain.dto.dict;
    exports com.fuhouyu.sass.platform.system.domain.dto.log;
    exports com.fuhouyu.sass.platform.system.domain.dto.monitor;
    exports com.fuhouyu.sass.platform.system.domain.dto.office;
    exports com.fuhouyu.sass.platform.system.domain.dto.organization;
    exports com.fuhouyu.sass.platform.system.domain.dto.page;
    exports com.fuhouyu.sass.platform.system.domain.dto.permission;
    exports com.fuhouyu.sass.platform.system.domain.dto.resource;
    exports com.fuhouyu.sass.platform.system.domain.dto.role;
    exports com.fuhouyu.sass.platform.system.domain.dto.tenant;
    exports com.fuhouyu.sass.platform.system.domain.dto.user;
    exports com.fuhouyu.sass.platform.system.domain.dto.wechat;
    exports com.fuhouyu.sass.platform.system.domain.dto.welink;
    exports com.fuhouyu.sass.platform.system.domain.dto.matomo;
    exports com.fuhouyu.sass.platform.system.domain.entity;
    exports com.fuhouyu.sass.platform.system.enums;
    exports com.fuhouyu.sass.platform.system.enums.response;
    exports com.fuhouyu.sass.platform.system.components.listener;
    exports com.fuhouyu.sass.platform.system.mapper;
    exports com.fuhouyu.sass.platform.system.components.properties;
    exports com.fuhouyu.sass.platform.system.service;
    exports com.fuhouyu.sass.platform.system.service.impl;
    exports com.fuhouyu.sass.platform.system.domain.dto.user.admin;

    opens com.fuhouyu.sass.platform.system.components.office to spring.core;
    opens com.fuhouyu.sass.platform.system.service.impl to spring.core, spring.beans, spring.context;
    opens com.fuhouyu.sass.platform.system.components.security.provider to spring.core;
    opens com.fuhouyu.sass.platform.system.utils to spring.core;

    opens com.fuhouyu.sass.platform.system.domain.dto to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.organization to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.tenant to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.page to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.role to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.monitor to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.welink to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.office to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.config to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.cloudflare to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.user to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.user.admin to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.wechat to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.log to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.application to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.account to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.resource to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.permission to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.domain.dto.dict to org.hibernate.validator, com.esotericsoftware.kryo.kryo5, spring.core;
    opens com.fuhouyu.sass.platform.system.components.security to com.esotericsoftware.kryo.kryo5;
    opens com.fuhouyu.sass.platform.system.domain.dto.matomo to com.fasterxml.jackson.databind;

    opens com.fuhouyu.sass.platform.system.domain.entity to spring.core;

}