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

DROP TABLE IF EXISTS tenants;
-- 租户表
CREATE TABLE tenants
(
    id             BIGINT PRIMARY KEY    NOT NULL,
    tenant_code    VARCHAR(64)           NOT NULL,
    tenant_name    VARCHAR(64)           NOT NULL,
    tenant_type    VARCHAR(12)           NOT NULL,
    remark         VARCHAR(256),
    icon           VARCHAR(256),
    contact_person VARCHAR(20)           NOT NULL,
    contact_number VARCHAR(20)           NOT NULL,
    is_deleted     boolean DEFAULT FALSE NOT NULL,
    create_at      timestamp             NOT NULL,
    create_by      VARCHAR(32)           NOT NULL,
    update_at      timestamp             NOT NULL,
    update_by      VARCHAR(32)           NOT NULL,
    UNIQUE (tenant_code)
);


COMMENT ON TABLE tenants IS '租户表';
COMMENT ON COLUMN tenants.id IS '主键id';
COMMENT ON COLUMN tenants.tenant_code IS '租户编码';
COMMENT ON COLUMN tenants.tenant_name IS '租户名称';
COMMENT ON COLUMN tenants.tenant_type IS '租户类型字典项';
COMMENT ON COLUMN tenants.remark IS '描述';
COMMENT ON COLUMN tenants.icon IS '租户图标';
COMMENT ON COLUMN tenants.contact_person IS '联系人';
COMMENT ON COLUMN tenants.contact_number IS '联系电话';
COMMENT ON COLUMN tenants.is_deleted IS '删除标记: false 未删除';
COMMENT ON COLUMN tenants.create_at IS '创建时间';
COMMENT ON COLUMN tenants.create_by IS '创建人';
COMMENT ON COLUMN tenants.update_at IS '更新时间';
COMMENT ON COLUMN tenants.update_by IS '更新人';


DROP TABLE IF EXISTS users;
-- 用户表
CREATE TABLE users
(
    id              BIGINT PRIMARY KEY NOT NULL,
    username        VARCHAR(64)        NOT NULL,
    real_name       VARCHAR(64),
    nickname        VARCHAR(64),
    email           VARCHAR(64),
    gender          VARCHAR(8),
    avatar          VARCHAR(32),
    login_date      timestamp          NOT NULL,
    login_ip        VARCHAR(64)        NOT NULL,
    owner_tenant_id VARCHAR(64)        NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE,
    is_deleted      BOOLEAN DEFAULT FALSE,
    create_at       timestamp          NOT NULL,
    create_by       VARCHAR(32)        NOT NULL,
    update_at       timestamp          NOT NULL,
    update_by       VARCHAR(32)        NOT NULL,
    UNIQUE (username)
);


COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户主键id';
COMMENT ON COLUMN users.username IS '用户名称';
COMMENT ON COLUMN users.real_name IS '真实姓名';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.email IS '邮箱地址';
COMMENT ON COLUMN users.gender IS '性别';
COMMENT ON COLUMN users.avatar IS '头像地址';
COMMENT ON COLUMN users.login_date IS '登录日期';
COMMENT ON COLUMN users.login_ip IS '登录ip';
COMMENT ON COLUMN users.is_enabled IS '是否启用：true 启用';
COMMENT ON COLUMN users.is_deleted IS '删除标记：false 未删除';
COMMENT ON COLUMN users.owner_tenant_id IS '当前账号所属的租户id';
COMMENT ON COLUMN users.create_at IS '创建时间';
COMMENT ON COLUMN users.create_by IS '创建者';
COMMENT ON COLUMN users.update_at IS '更新时间';
COMMENT ON COLUMN users.update_by IS '更新者';

DROP TABLE IF EXISTS tenant_has_user;
-- 租户和用户关系表
CREATE TABLE tenant_has_user
(
    tenant_id BIGINT      NOT NULL,
    user_id   BIGINT      NOT NULL,
    create_at timestamp   NOT NULL,
    create_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (tenant_id, user_id)
);

COMMENT ON TABLE tenant_has_user IS '租户和用户的关联关系表';
COMMENT ON COLUMN tenant_has_user.tenant_id IS '租户id';
COMMENT ON COLUMN tenant_has_user.user_id IS '用户id';
COMMENT ON COLUMN tenant_has_user.create_at IS '创建时间';
COMMENT ON COLUMN tenant_has_user.create_by IS '创建人';


-- 角色表
DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id                BIGINT PRIMARY KEY NOT NULL,
    role_name         VARCHAR(64)        NOT NULL,
    role_code         VARCHAR(64)        NOT NULL,
    display_order     INT                NOT NULL DEFAULT 0,
    data_scope        VARCHAR(32)        NOT NULL,
    is_enabled        BOOLEAN                     DEFAULT TRUE,
    is_deleted        BOOLEAN                     DEFAULT FALSE,
    is_allow_modified BOOLEAN                     DEFAULT TRUE,
    owner_tenant_id   VARCHAR(64)        NOT NULL,
    create_at         timestamp          NOT NULL,
    create_by         VARCHAR(32)        NOT NULL,
    update_at         timestamp          NOT NULL,
    update_by         VARCHAR(32)        NOT NULL,
    UNIQUE (role_code)
);

COMMENT ON TABLE roles IS '角色表';
COMMENT ON COLUMN roles.id IS '主键id';
COMMENT ON COLUMN roles.role_name IS '角色名称';
COMMENT ON COLUMN roles.role_code IS '角色编码';
COMMENT ON COLUMN roles.display_order IS '显示顺序';
COMMENT ON COLUMN roles.data_scope IS '数据权限，字典项';
COMMENT ON COLUMN roles.is_enabled IS '启用/禁用';
COMMENT ON COLUMN roles.is_deleted IS '删除标记';
COMMENT ON COLUMN roles.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN roles.is_deleted IS '删除标记：false 未删除';
COMMENT ON COLUMN roles.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN roles.create_at IS '创建时间';
COMMENT ON COLUMN roles.create_by IS '创建者';
COMMENT ON COLUMN roles.update_at IS '更新时间';
COMMENT ON COLUMN roles.update_by IS '更新者';

-- 用户角色表
DROP TABLE IF EXISTS user_has_role;
CREATE TABLE user_has_role
(
    user_id   BIGINT      NOT NULL,
    role_id   BIGINT      NOT NULL,
    create_at timestamp   NOT NULL,
    create_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE user_has_role IS '用户和角色的关联关系表';
COMMENT ON COLUMN user_has_role.user_id IS '用户id';
COMMENT ON COLUMN user_has_role.role_id IS '角色id';
COMMENT ON COLUMN user_has_role.create_at IS '创建时间';
COMMENT ON COLUMN user_has_role.create_by IS '创建人';


DROP TABLE IF EXISTS tenant_has_role;
-- 租户角色关联关系表
CREATE TABLE tenant_has_role
(
    tenant_id BIGINT      NOT NULL,
    role_id   BIGINT      NOT NULL,
    create_at timestamp   NOT NULL,
    create_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (tenant_id, role_id)
);

COMMENT ON TABLE tenant_has_role IS '租户和用户的关联关系表';
COMMENT ON COLUMN tenant_has_role.tenant_id IS '租户id';
COMMENT ON COLUMN tenant_has_role.role_id IS '角色id';
COMMENT ON COLUMN tenant_has_role.create_at IS '创建时间';
COMMENT ON COLUMN tenant_has_role.create_by IS '创建人';

DROP TABLE IF EXISTS permissions;
-- 权限表
CREATE TABLE permissions
(
    id              BIGINT PRIMARY KEY    NOT NULL,
    parent_id       BIGINT                NOT NULL,
    permission_name VARCHAR(64)           NOT NULL,
    permission_code VARCHAR(64)           NOT NULL,
    display_order   INT     DEFAULT 0,
    icon            VARCHAR(64),
    route_path      VARCHAR(32),
    component_path  VARCHAR(32),
    url_params      VARCHAR(256),
    is_frame        BOOLEAN DEFAULT false NOT NULL,
    permission_type VARCHAR(16)           NOT NULL,
    is_visible      BOOLEAN DEFAULT TRUE  NOT NULL,
    owner_tenant_id VARCHAR(64)           NOT NULL,
    is_deleted      BOOLEAN DEFAULT FALSE,
    create_at       timestamp             NOT NULL,
    create_by       VARCHAR(32)           NOT NULL,
    update_at       timestamp             NOT NULL,
    update_by       VARCHAR(32)           NOT NULL,
    UNIQUE (permission_code)
);
CREATE INDEX permission_parent_id_idx ON permissions (parent_id);
COMMENT ON INDEX permission_parent_id_idx IS '权限父级id索引';


COMMENT ON TABLE permissions IS '角色表';
COMMENT ON COLUMN permissions.id IS '角色名称';
COMMENT ON COLUMN permissions.parent_id IS '父级节点id，-1时为一级菜菜单';
COMMENT ON COLUMN permissions.display_order IS '显示顺序';
COMMENT ON COLUMN permissions.permission_name IS '权限名称';
COMMENT ON COLUMN permissions.permission_code IS '权限编码';
COMMENT ON COLUMN permissions.icon IS 'icon';
COMMENT ON COLUMN permissions.route_path IS '路由路径';
COMMENT ON COLUMN permissions.component_path IS '组件路径';
COMMENT ON COLUMN permissions.url_params IS 'url参数';
COMMENT ON COLUMN permissions.is_frame IS '是否外链';
COMMENT ON COLUMN permissions.permission_type IS '权限类型字典项';
COMMENT ON COLUMN permissions.is_visible IS '是否显示标记';
COMMENT ON COLUMN permissions.is_deleted IS '删除标记';
COMMENT ON COLUMN permissions.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN permissions.is_deleted IS '删除标记：false 未删除';
COMMENT ON COLUMN permissions.create_at IS '创建时间';
COMMENT ON COLUMN permissions.create_by IS '创建者';
COMMENT ON COLUMN permissions.update_at IS '更新时间';
COMMENT ON COLUMN permissions.update_by IS '更新者';

DROP TABLE IF EXISTS accounts;
-- 账号表
CREATE TABLE accounts
(
    account                     VARCHAR(128)         NOT NULL,
    account_type                VARCHAR(32)          NOT NULL,
    ref_user_id                 BIGINT               NOT NULL,
    credentials                 VARCHAR(128)         NOT NULL,
    credentials_expiration_time TIMESTAMP,
    ref_account_id              VARCHAR(128),
    is_enabled                  BOOLEAN DEFAULT true NOT NULL,
    create_at                   timestamp            NOT NULL,
    create_by                   VARCHAR(32)          NOT NULL,
    update_at                   timestamp            NOT NULL,
    update_by                   VARCHAR(32)          NOT NULL,
    PRIMARY KEY (account, account_type)
);

CREATE INDEX account_user_id_idx ON accounts (ref_user_id);
COMMENT ON INDEX account_user_id_idx IS '账号用户id索引';
COMMENT ON TABLE accounts IS '账号表';
COMMENT ON COLUMN accounts.account IS '账号';
COMMENT ON COLUMN accounts.account_type IS '账号类型字典项，如密码、微信等';
COMMENT ON COLUMN accounts.ref_user_id IS '属于哪个用户';
COMMENT ON COLUMN accounts.credentials IS '登录凭证';
COMMENT ON COLUMN accounts.credentials_expiration_time IS '凭证过期时间，为null则永不过期';
COMMENT ON COLUMN accounts.ref_account_id IS '第三方账号登录时的账号id';
COMMENT ON COLUMN accounts.is_enabled IS '是否启用该账号登录';
COMMENT ON COLUMN accounts.create_at IS '创建时间';
COMMENT ON COLUMN accounts.create_by IS '创建者';
COMMENT ON COLUMN accounts.update_at IS '更新时间';
COMMENT ON COLUMN accounts.update_by IS '更新者';


DROP TABLE IF EXISTS dict_type;
-- 字典表
CREATE TABLE dict_type
(
    id                BIGINT       NOT NULL PRIMARY KEY,
    dict_name         VARCHAR(128) NOT NULL,
    type_code         VARCHAR(128) NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE,
    is_allow_modified BOOLEAN DEFAULT FALSE,
    remark            VARCHAR(128) NOT NULL,
    create_at         timestamp    NOT NULL,
    create_by         VARCHAR(32)  NOT NULL,
    update_at         timestamp    NOT NULL,
    update_by         VARCHAR(32)  NOT NULL,
    UNIQUE (type_code)
);

COMMENT ON TABLE dict_type IS '字典类型表';
COMMENT ON COLUMN dict_type.id IS '主键id';
COMMENT ON COLUMN dict_type.dict_name IS '字典类型名称';
COMMENT ON COLUMN dict_type.type_code IS '类型编码';
COMMENT ON COLUMN dict_type.is_deleted IS '删除标记';
COMMENT ON COLUMN dict_type.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN dict_type.remark IS '备注';
COMMENT ON COLUMN dict_type.create_at IS '创建时间';
COMMENT ON COLUMN dict_type.create_by IS '创建者';
COMMENT ON COLUMN dict_type.update_at IS '更新时间';
COMMENT ON COLUMN dict_type.update_by IS '更新者';


DROP TABLE IF EXISTS dict_item;
CREATE TABLE dict_item
(
    id                BIGINT       NOT NULL PRIMARY KEY,
    type_code         VARCHAR(128) NOT NULL,
    item_name         VARCHAR(128) NOT NULL,
    item_code         VARCHAR(128) NOT NULL,
    display_order     INT          NOT NULL DEFAULT 0,
    is_allow_modified BOOLEAN               DEFAULT TRUE,
    is_deleted        BOOLEAN               DEFAULT FALSE,
    remark            VARCHAR(128) NOT NULL,
    create_at         timestamp    NOT NULL,
    create_by         VARCHAR(32)  NOT NULL,
    update_at         timestamp    NOT NULL,
    update_by         VARCHAR(32)  NOT NULL,
    UNIQUE (item_code)
);

CREATE INDEX dict_item_type_code_idx ON dict_item (type_code);
COMMENT ON INDEX dict_item_type_code_idx IS '字典类型编码索引';

COMMENT ON TABLE dict_item IS '字典类型';
COMMENT ON COLUMN dict_item.id IS '主键id';
COMMENT ON COLUMN dict_item.type_code IS '类型编码';
COMMENT ON COLUMN dict_item.item_name IS '字典项名称';
COMMENT ON COLUMN dict_item.item_code IS '字典项名称';
COMMENT ON COLUMN dict_item.display_order IS '显示顺序';

COMMENT ON COLUMN dict_item.is_deleted IS '删除标记';
COMMENT ON COLUMN dict_item.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN dict_item.remark IS '备注';
COMMENT ON COLUMN dict_item.create_at IS '创建时间';
COMMENT ON COLUMN dict_item.create_by IS '创建者';
COMMENT ON COLUMN dict_item.update_at IS '更新时间';
COMMENT ON COLUMN dict_item.update_by IS '更新者';

