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

DROP TABLE IF EXISTS tenant_info;
CREATE TABLE tenant_info
(
    id             BIGINT PRIMARY KEY    NOT NULL,
    admin_user_id BIGINT                NOT NULL,
    tenant_code    VARCHAR(64)           NOT NULL,
    tenant_name    VARCHAR(64)           NOT NULL,
    tenant_type    VARCHAR(12)           NOT NULL,
    remark         VARCHAR(256),
    icon          BIGINT,
    contact_person VARCHAR(20)           NOT NULL,
    contact_info   VARCHAR(20)           NOT NULL,
    start_date    DATE,
    end_date      DATE,
    is_enabled     BOOLEAN DEFAULT TRUE  NOT NULL,
    is_platform   BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted     BOOLEAN DEFAULT FALSE NOT NULL,
    created_at    TIMESTAMP             NOT NULL,
    created_by    VARCHAR(64)           NOT NULL,
    updated_at    TIMESTAMP             NOT NULL,
    updated_by    VARCHAR(64)           NOT NULL,
    UNIQUE (tenant_code)
);


COMMENT ON TABLE tenant_info IS '租户表';
COMMENT ON COLUMN tenant_info.id IS '主键id';
COMMENT ON COLUMN tenant_info.admin_user_id IS '管理员id';
COMMENT ON COLUMN tenant_info.tenant_code IS '租户编码';
COMMENT ON COLUMN tenant_info.tenant_name IS '租户名称';
COMMENT ON COLUMN tenant_info.tenant_type IS '租户类型字典项';
COMMENT ON COLUMN tenant_info.remark IS '描述';
COMMENT ON COLUMN tenant_info.icon IS '租户图标';
COMMENT ON COLUMN tenant_info.contact_person IS '联系人';
COMMENT ON COLUMN tenant_info.contact_info IS '联系方式';
COMMENT ON COLUMN tenant_info.start_date IS '租户有效开始日期';
COMMENT ON COLUMN tenant_info.end_date IS '租户有效结束日期';
COMMENT ON COLUMN tenant_info.is_enabled IS '状态：true 启用，false禁用';
COMMENT ON COLUMN tenant_info.is_platform IS '是否平台';
COMMENT ON COLUMN tenant_info.is_deleted IS '删除标记: false 未删除';
COMMENT ON COLUMN tenant_info.created_at IS '创建时间';
COMMENT ON COLUMN tenant_info.created_by IS '创建人';
COMMENT ON COLUMN tenant_info.updated_at IS '更新时间';
COMMENT ON COLUMN tenant_info.updated_by IS '更新人';


-- 内置租户
INSERT INTO tenant_info(id, admin_user_id, tenant_code, tenant_name, tenant_type, remark, icon, contact_person,
                        contact_info, created_at,
                        created_by, updated_at, updated_by, is_platform)
VALUES (1, 1, 'platform_tenant', '平台租户', 'COMPANY', '平台租户', null, 'fuhouyu', 'fuhouyu@live.cn', now(), 'admin',
        now(), 'admin', true);

-- 租户权限
DROP TABLE IF EXISTS tenant_has_permission;
CREATE TABLE tenant_has_permission
(
    tenant_id     BIGINT      NOT NULL,
    permission_id BIGINT      NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(64) NOT NULL,
    PRIMARY KEY (tenant_id, permission_id)
);
COMMENT ON TABLE tenant_has_permission IS '租户权限关系表';
COMMENT ON COLUMN tenant_has_permission.tenant_id IS '租户id';
COMMENT ON COLUMN tenant_has_permission.permission_id IS '权限id';
COMMENT ON COLUMN tenant_has_permission.created_at IS '创建时间';
COMMENT ON COLUMN tenant_has_permission.created_by IS '创建人';


DROP TABLE IF EXISTS admin_users;
-- 用户表
CREATE TABLE admin_users
(
    id              BIGINT PRIMARY KEY NOT NULL,
    username        VARCHAR(64)        NOT NULL,
    real_name       VARCHAR(64),
    nickname        VARCHAR(64),
    email           VARCHAR(64),
    gender          VARCHAR(8),
    avatar          BIGINT,
    login_date      TIMESTAMP,
    login_ip        VARCHAR(64),
    is_enabled      BOOLEAN DEFAULT TRUE,
    is_deleted      BOOLEAN DEFAULT FALSE,
    owner_tenant_id BIGINT             NOT NULL,
    created_at      TIMESTAMP          NOT NULL,
    created_by      VARCHAR(32)        NOT NULL,
    updated_at      TIMESTAMP          NOT NULL,
    updated_by      VARCHAR(32)        NOT NULL,
    UNIQUE (username)
);


COMMENT ON TABLE admin_users IS '用户表';
COMMENT ON COLUMN admin_users.id IS '用户主键id';
COMMENT ON COLUMN admin_users.username IS '用户名称';
COMMENT ON COLUMN admin_users.real_name IS '真实姓名';
COMMENT ON COLUMN admin_users.nickname IS '昵称';
COMMENT ON COLUMN admin_users.email IS '邮箱地址';
COMMENT ON COLUMN admin_users.gender IS '性别';
COMMENT ON COLUMN admin_users.avatar IS '头像资源id';
COMMENT ON COLUMN admin_users.login_date IS '登录日期';
COMMENT ON COLUMN admin_users.login_ip IS '登录ip';
COMMENT ON COLUMN admin_users.is_enabled IS '是否启用：true 启用';
COMMENT ON COLUMN admin_users.is_deleted IS '删除标记：false 未删除';
COMMENT ON COLUMN admin_users.owner_tenant_id IS '租户id';
COMMENT ON COLUMN admin_users.created_at IS '创建时间';
COMMENT ON COLUMN admin_users.created_by IS '创建人';
COMMENT ON COLUMN admin_users.updated_at IS '更新时间';
COMMENT ON COLUMN admin_users.updated_by IS '更新人';

INSERT INTO admin_users(id, username, real_name, nickname, email, gender, avatar, login_date, login_ip, created_at,
                        created_by,
                        updated_at, updated_by, owner_tenant_id)
VALUES (1, 'admin', '管理员', '管理员', 'fuhouyu@live.cn', 'MALE',
        null, now(), '127.0.0.1', now(), 'admin', now(), 'admin', 1);


-- 角色表
DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id                BIGINT PRIMARY KEY    NOT NULL,
    role_name         VARCHAR(64)           NOT NULL,
    role_code         VARCHAR(64)           NOT NULL,
    display_order     INT                   NOT NULL DEFAULT 0,
    data_scope        VARCHAR(32)           NOT NULL,
    is_enabled        BOOLEAN DEFAULT TRUE  NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE NOT NULL,
    is_allow_modified BOOLEAN DEFAULT TRUE  NOT NULL,
    owner_tenant_id BIGINT      NOT NULL,
    created_at      TIMESTAMP   NOT NULL,
    created_by      VARCHAR(32) NOT NULL,
    updated_at      TIMESTAMP   NOT NULL,
    updated_by      VARCHAR(32) NOT NULL,
    UNIQUE (owner_tenant_id, role_code)
);

COMMENT ON TABLE roles IS '角色表';
COMMENT ON COLUMN roles.id IS '主键id';
COMMENT ON COLUMN roles.role_name IS '角色名称';
COMMENT ON COLUMN roles.role_code IS '角色编码';
COMMENT ON COLUMN roles.display_order IS '显示顺序';
COMMENT ON COLUMN roles.data_scope IS '数据权限，字典项';
COMMENT ON COLUMN roles.is_enabled IS '启用/禁用';
COMMENT ON COLUMN roles.is_deleted IS '删除标记';
COMMENT ON COLUMN roles.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN roles.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN roles.created_at IS '创建时间';
COMMENT ON COLUMN roles.created_by IS '创建人';
COMMENT ON COLUMN roles.updated_at IS '更新时间';
COMMENT ON COLUMN roles.updated_by IS '更新人';

INSERT INTO roles(id, owner_tenant_id, role_name, role_code, data_scope, created_at, created_by, updated_at, updated_by)
VALUES (1, 1, '超级管理员', 'super_admin', 'ALL', now(), 'admin', now(), 'admin');


-- 用户角色表
DROP TABLE IF EXISTS user_has_role;
CREATE TABLE user_has_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE user_has_role IS '用户和角色的关联关系表';
COMMENT ON COLUMN user_has_role.user_id IS '用户id';
COMMENT ON COLUMN user_has_role.role_id IS '角色id';
COMMENT ON COLUMN user_has_role.created_at IS '创建时间';
COMMENT ON COLUMN user_has_role.created_by IS '创建人';

INSERT INTO user_has_role(user_id, role_id, created_at, created_by)
VALUES (1, 1, now(), 'admin');

-- 权限表
DROP TABLE IF EXISTS permissions;
CREATE TABLE permissions
(
    id                BIGINT PRIMARY KEY    NOT NULL,
    parent_id         BIGINT                NOT NULL,
    permission_name   VARCHAR(64)           NOT NULL,
    permission_code   VARCHAR(64)           NOT NULL,
    display_order     INT     DEFAULT 0,
    icon              VARCHAR(64),
    route_path        VARCHAR(32),
    component_path    VARCHAR(32),
    url_params        VARCHAR(256),
    is_frame          BOOLEAN DEFAULT false NOT NULL,
    permission_type   VARCHAR(16)           NOT NULL,
    is_allow_modified BOOLEAN DEFAULT TRUE  NOT NULL,
    is_visible        BOOLEAN DEFAULT TRUE  NOT NULL,
    is_leaf           BOOLEAN DEFAULT TRUE  NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE NOT NULL,
    owner_tenant_id BIGINT               NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE NOT NULL,
    created_at      TIMESTAMP            NOT NULL,
    created_by      VARCHAR(32)          NOT NULL,
    updated_at      TIMESTAMP            NOT NULL,
    updated_by      VARCHAR(32)          NOT NULL,
    UNIQUE (permission_code)
);
CREATE INDEX idx_permission_parent_id ON permissions (parent_id);
COMMENT ON INDEX idx_permission_parent_id IS '权限父级id索引';
CREATE INDEX idx_permission_tenant_id ON permissions (owner_tenant_id, permission_code);
COMMENT ON INDEX idx_permission_tenant_id IS '租户下的权限编码唯一索引索引';

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
COMMENT ON COLUMN permissions.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN permissions.is_visible IS '是否显示标记';
COMMENT ON COLUMN permissions.is_deleted IS '删除标记';
COMMENT ON COLUMN permissions.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN permissions.is_leaf IS '是否为叶子节点：true 是 false 否';
COMMENT ON COLUMN permissions.is_enabled IS '启禁用状态：true 启用，false 禁用';
COMMENT ON COLUMN permissions.created_at IS '创建时间';
COMMENT ON COLUMN permissions.created_by IS '创建人';
COMMENT ON COLUMN permissions.updated_at IS '更新时间';
COMMENT ON COLUMN permissions.updated_by IS '更新人';

INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (2, -1, 'Menu.systemManage', 'system', 3, 'i-xitongshezhi', 'system', null, '', false, 'DIR', false, true, false,
        true, 1, false, now(), 'admin', now(), 'admin');
-- 权限管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (23, 2, 'Menu.permissionManage', 'system:permission:list', 3, 'i-icon-quanxian', 'permission',
        'system/permission',
        '', false, 'MENU', false, true, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (231, 23, 'Menu.query', 'system:permission:query', 1, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (232, 23, 'Menu.add', 'system:permission:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (233, 23, 'Menu.edit', 'system:permission:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (234, 23, 'Menu.delete', 'system:permission:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1, false, now(), 'admin', now(), 'admin');

-- 用户管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (21, 2, 'Menu.userManage', 'system:user:list', 1, 'i-yonghu1', 'user', 'system/user', '', false, 'MENU', false,
        true,
        false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (211, 21, 'Menu.query', 'system:user:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'Menu.admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (212, 21, 'Menu.add', 'system:user:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1, false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (213, 21, 'Menu.edit', 'system:user:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true, 1, false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (214, 21, 'Menu.delete', 'system:user:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');

-- 字典管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (24, 2, 'Menu.dictManage', 'system:dict-type:list', 4, 'i-zidian1', 'dict-type', 'system/dictType', null, false,
        'MENU', false, true, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (241, 24, 'Menu.query', 'system:dict-type:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (242, 24, 'Menu.add', 'system:dict-type:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (243, 24, 'Menu.edit', 'system:dict-type:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (244, 24, 'Menu.delete', 'system:dict-type:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1,
        false, now(), 'admin', now(), 'admin');

-- 字典项管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (25, 2, 'Menu.dictItem', 'system:dict-item:list', 4, 'i-zidian1', 'dict-item/:dictCode?', 'system/dictItem',
        null,
        false, 'MENU', false, false, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (251, 25, 'Menu.query', 'system:dict-item:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (252, 25, 'Menu.add', 'system:dict-item:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (253, 25, 'Menu.edit', 'system:dict-item:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (254, 25, 'Menu.delete', 'system:dict-item:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1, false, now(), 'admin', now(), 'admin');

-- 角色管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (22, 2, 'Menu.roleManage', 'system:role:list', 2, 'i-jiaoseguanli2', 'role', 'system/role', '', false, 'MENU',
        false,
        true, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (221, 22, 'Menu.query', 'system:role:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (222, 22, 'Menu.add', 'system:role:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1, false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (223, 22, 'Menu.edit', 'system:role:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true, 1, false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (224, 22, 'Menu.delete', 'system:role:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');

-- 租户管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (1, -1, 'Menu.tenantManage', 'tenant:list', 2, 'i-zuhuguanli', 'tenant', 'tenant', '', false, 'MENU', false,
        true,
        false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (11, 1, 'Menu.query', 'tenant:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (12, 1, 'Menu.add', 'tenant:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1, false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (13, 1, 'Menu.edit', 'tenant:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (14, 1, 'Menu.delete', 'tenant:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');

-- 租户空间
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (3, -1, 'Menu.tenantSpace', 'tenant:space:list', 6, '', 'tenant-space', 'tenant/space', '', false, 'DIR', false,
        false, false, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (31, 3, 'Menu.query', 'tenant-space:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (32, 3, 'Menu.resourceList', 'tenant-space:resource-list', 2, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (33, 3, 'Menu.delete', 'tenant-space:delete', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false, now(), 'admin', now(), 'admin');


-- 组织管理
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (26, 2, 'Menu.organizationManage', 'system:organization:list', 2, 'i-drxx95', 'organization',
        'system/organization',
        '', false, 'MENU', false, true,
        false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (261, 26, 'Menu.query', 'system:organization:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (262, 26, 'Menu.add', 'system:organization:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (263, 26, 'Menu.edit', 'system:organization:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (264, 26, 'Menu.delete', 'system:organization:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (265, 26, 'Menu.addMember', 'system:organization:add-member', 5, '', '', '', '', false, 'BUTTON', false, true,
        true,
        true, 1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (266, 26, 'Menu.deleteMember', 'system:organization:delete-member', 6, '', '', '', '', false, 'BUTTON', false,
        true,
        true, true, 1,
        false, now(), 'admin', now(), 'admin');

-- 操作日志
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (27, 2, 'Menu.operationLogManage', 'system:operation-log:list', 4, 'i-caozuorizhi', 'operationLog',
        'system/operationLog', '', false, 'MENU',
        false,
        true, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (271, 27, 'Menu.query', 'system:operation-log:query', 1, '', '', '', '', false, 'BUTTON', false, true, true,
        true,
        1,
        false, now(), 'admin', now(), 'admin');

-- 参数配置
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (28, 2, 'Menu.paramConfig', 'system:param-config:list', 5, 'i-canshuguanli', 'paramConfig',
        'system/paramConfig', '', false, 'MENU',
        false,
        true, false, true, 1, false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (281, 28, 'Menu.query', 'system:param-config:query', 1, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (282, 28, 'Menu.add', 'system:param-config:add', 2, '', '', '', '', false, 'BUTTON', false, true, true, true, 1,
        false,
        now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (283, 28, 'Menu.edit', 'system:param-config:edit', 3, '', '', '', '', false, 'BUTTON', false, true, true, true,
        1,
        false, now(), 'admin', now(), 'admin');
INSERT INTO permissions (id, parent_id, permission_name, permission_code, display_order, icon, route_path,
                         component_path, url_params, is_frame, permission_type, is_allow_modified, is_visible, is_leaf,
                         is_enabled, owner_tenant_id, is_deleted, created_at, created_by, updated_at, updated_by)
VALUES (284, 28, 'Menu.delete', 'system:param-config:delete', 4, '', '', '', '', false, 'BUTTON', false, true, true,
        true, 1,
        false, now(), 'admin', now(), 'admin');

-- 角色关联的权限
DROP TABLE IF EXISTS role_has_permission;
CREATE TABLE role_has_permission
(
    role_id       BIGINT      NOT NULL,
    permission_id BIGINT      NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

COMMENT ON TABLE role_has_permission IS '角色和权限的关联关系表';
COMMENT ON COLUMN role_has_permission.role_id IS '角色id';
COMMENT ON COLUMN role_has_permission.permission_id IS '权限id';
COMMENT ON COLUMN role_has_permission.created_at IS '创建时间';
COMMENT ON COLUMN role_has_permission.created_by IS '创建人';

INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 2, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 23, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 21, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 24, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 25, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 22, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 1, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 26, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 234, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 251, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 223, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 222, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 243, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 254, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 253, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 212, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 242, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 241, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 214, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 232, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 221, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 233, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 252, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 12, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 213, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 211, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 244, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 11, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 14, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 231, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 13, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 224, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 261, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 262, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 263, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 264, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 265, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 266, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 3, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 31, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 32, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 33, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 27, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 271, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 28, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 281, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 282, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 283, now(), 'admin');
INSERT INTO role_has_permission(role_id, permission_id, created_at, created_by)
VALUES (1, 284, now(), 'admin');
DROP TABLE IF EXISTS accounts;
-- 账号表
CREATE TABLE accounts
(
    account                     VARCHAR(128)         NOT NULL,
    account_type                VARCHAR(32)          NOT NULL,
    user_id         BIGINT               NOT NULL,
    credentials     VARCHAR(128),
    credentials_expiration_time TIMESTAMP,
    ref_account_id              VARCHAR(128),
    user_type       VARCHAR(32)          NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE NOT NULL,
    owner_tenant_id BIGINT               NOT NULL,
    created_at      TIMESTAMP            NOT NULL,
    created_by      VARCHAR(32)          NOT NULL,
    updated_at      TIMESTAMP            NOT NULL,
    updated_by      VARCHAR(32)          NOT NULL,
    PRIMARY KEY (owner_tenant_id, account, account_type)
);

CREATE INDEX idx_account_user_id ON accounts (user_id);
COMMENT ON INDEX idx_account_user_id IS '账号用户id索引';
COMMENT ON TABLE accounts IS '账号表';
COMMENT ON COLUMN accounts.account IS '账号';
COMMENT ON COLUMN accounts.account_type IS '账号类型字典项，如密码、微信等';
COMMENT ON COLUMN accounts.user_id IS '所对应的用户id';
COMMENT ON COLUMN accounts.credentials IS '登录凭证';
COMMENT ON COLUMN accounts.credentials_expiration_time IS '凭证过期时间，为null则永不过期';
COMMENT ON COLUMN accounts.user_type IS '账号类型：ADMIN/NORMAL';
COMMENT ON COLUMN accounts.ref_account_id IS '第三方账号登录时的账号id';
COMMENT ON COLUMN accounts.is_enabled IS '是否启用该账号登录';
COMMENT ON COLUMN accounts.owner_tenant_id IS '当前账号所属的租户id';
COMMENT ON COLUMN accounts.created_at IS '创建时间';
COMMENT ON COLUMN accounts.created_by IS '创建人';
COMMENT ON COLUMN accounts.updated_at IS '更新时间';
COMMENT ON COLUMN accounts.updated_by IS '更新人';

INSERT INTO accounts(owner_tenant_id, account, account_type, user_id, credentials, credentials_expiration_time,
                     ref_account_id,
                     user_type,
                     created_at, created_by, updated_at, updated_by)
VALUES (1, 'admin', 'PASSWORD', 1, '{sm3}$3mb29qZzcuSEhKSnU1LkpRbgQk6/3N6wriraK7V5V0SE74tuRB7TVNRiigXOiMu3JNE',
        null, null, 'ADMIN', now(), 'admin', now(), 'admin');


DROP TABLE IF EXISTS dict_type;
-- 字典表
CREATE TABLE dict_type
(
    id                BIGINT                NOT NULL PRIMARY KEY,
    dict_name         VARCHAR(128)          NOT NULL,
    dict_code       VARCHAR(128)         NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE NOT NULL,
    is_allow_modified BOOLEAN DEFAULT TRUE  NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE NOT NULL,
    display_order   INT     DEFAULT 0    NOT NULL,
    owner_tenant_id BIGINT               NOT NULL,
    remark          VARCHAR(128),
    created_at      TIMESTAMP            NOT NULL,
    created_by      VARCHAR(32)          NOT NULL,
    updated_at      TIMESTAMP            NOT NULL,
    updated_by      VARCHAR(32)          NOT NULL,
    UNIQUE (owner_tenant_id, dict_code)
);

COMMENT ON TABLE dict_type IS '字典类型表';
COMMENT ON COLUMN dict_type.id IS '主键id';
COMMENT ON COLUMN dict_type.dict_name IS '字典类型名称';
COMMENT ON COLUMN dict_type.dict_code IS '字典类型编码';
COMMENT ON COLUMN dict_type.display_order IS '排序字段';
COMMENT ON COLUMN dict_type.is_enabled IS '启禁用状态： true 启用 false 禁用';
COMMENT ON COLUMN dict_type.is_deleted IS '删除标记';
COMMENT ON COLUMN dict_type.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN dict_type.remark IS '备注';
COMMENT ON COLUMN dict_type.owner_tenant_id IS '所属租户的id';
COMMENT ON COLUMN dict_type.created_at IS '创建时间';
COMMENT ON COLUMN dict_type.created_by IS '创建人';
COMMENT ON COLUMN dict_type.updated_at IS '更新时间';
COMMENT ON COLUMN dict_type.updated_by IS '更新人';

INSERT INTO dict_type
(id, dict_name, dict_code, display_order, is_enabled, is_deleted, is_allow_modified, remark, owner_tenant_id,
 created_at,
 created_by, updated_at, updated_by)
VALUES (1, '性别', 'GENDER', 1, true, false, false, '性别', 1, now(), 'admin', now(), 'admin');

INSERT INTO dict_type
(id, dict_name, dict_code, display_order, is_enabled, is_deleted, is_allow_modified, remark, owner_tenant_id,
 created_at,
 created_by, updated_at, updated_by)
VALUES (2, '租户类型', 'TENANT_TYPE', 2, true, false, false, '租户类型', 1, now(), 'admin', now(), 'admin');

INSERT INTO dict_type
(id, dict_name, dict_code, display_order, is_enabled, is_deleted, is_allow_modified, remark, owner_tenant_id,
 created_at,
 created_by, updated_at, updated_by)
VALUES (3, '日志类型', 'OPERATION_LOG_TYPE', 3, true, false, false, '日志操作类型', 1, now(), 'admin', now(), 'admin');



DROP TABLE IF EXISTS dict_item;
CREATE TABLE dict_item
(
    id                BIGINT                NOT NULL PRIMARY KEY,
    dict_code       VARCHAR(128)         NOT NULL,
    item_name         VARCHAR(128)          NOT NULL,
    item_code         VARCHAR(128)          NOT NULL,
    display_order     INT                   NOT NULL DEFAULT 0,
    is_allow_modified BOOLEAN DEFAULT TRUE  NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE NOT NULL,
    owner_tenant_id BIGINT               NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE NOT NULL,
    remark          VARCHAR(128),
    created_at      TIMESTAMP            NOT NULL,
    created_by      VARCHAR(32)          NOT NULL,
    updated_at      TIMESTAMP            NOT NULL,
    updated_by      VARCHAR(32)          NOT NULL,
    UNIQUE (owner_tenant_id, dict_code, item_code)
);

COMMENT ON TABLE dict_item IS '字典类型';
COMMENT ON COLUMN dict_item.id IS '主键id';
COMMENT ON COLUMN dict_item.dict_code IS '类型编码';
COMMENT ON COLUMN dict_item.item_name IS '字典项名称';
COMMENT ON COLUMN dict_item.item_code IS '字典项名称';
COMMENT ON COLUMN dict_item.display_order IS '显示顺序';
COMMENT ON COLUMN dict_item.is_deleted IS '删除标记';
COMMENT ON COLUMN dict_item.is_enabled IS '启禁用状态：true 启用';
COMMENT ON COLUMN dict_item.is_allow_modified IS '是否允许修改';
COMMENT ON COLUMN dict_item.owner_tenant_id IS '所属租户的id';
COMMENT ON COLUMN dict_item.remark IS '备注';
COMMENT ON COLUMN dict_item.created_at IS '创建时间';
COMMENT ON COLUMN dict_item.created_by IS '创建人';
COMMENT ON COLUMN dict_item.updated_at IS '更新时间';
COMMENT ON COLUMN dict_item.updated_by IS '更新人';

-- 性别
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (1, 'GENDER', '男', 'MALE', 1, true, false, false, 1, '性别男', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (2, 'GENDER', '女', 'FEMALE', 2, true, false, false, 1, '性别女', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (3, 'GENDER', '未知', 'UNKNOWN', 3, true, false, false, 1, '未知', now(), 'admin', now(), 'admin');

-- 租户类型
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (4, 'TENANT_TYPE', '公司', 'COMPANY', 1, true, false, false, 1, '公司', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (5, 'TENANT_TYPE', '学校', 'SCHOOL', 2, true, false, false, 1, '学校', now(), 'admin', now(), 'admin');

-- 操作日志类型
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (6, 'OPERATION_LOG_TYPE', '登录', 'LOGIN', 1, true, false, false, 1, '登录', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (7, 'OPERATION_LOG_TYPE', '登出', 'LOGOUT', 2, true, false, false, 1, '登出', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (8, 'OPERATION_LOG_TYPE', '查询', 'QUERY', 3, true, false, false, 1, '查询', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (9, 'OPERATION_LOG_TYPE', '创建', 'CREATE', 4, true, false, false, 1, '创建', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (10, 'OPERATION_LOG_TYPE', '更新', 'UPDATE', 5, true, false, false, 1, '更新', now(), 'admin', now(), 'admin');
INSERT INTO dict_item(id, dict_code, item_name, item_code, display_order, is_enabled, is_deleted, is_allow_modified,
                      owner_tenant_id, remark, created_at, created_by, updated_at, updated_by)
VALUES (11, 'OPERATION_LOG_TYPE', '删除', 'DELETE', 6, true, false, false, 1, '删除', now(), 'admin', now(), 'admin');



DROP TABLE IF EXISTS organizations;
CREATE TABLE organizations
(
    id                BIGINT PRIMARY KEY NOT NULL,
    parent_id         BIGINT             NOT NULL,
    organization_name VARCHAR(255)       NOT NULL,
    organization_code VARCHAR(255)       NOT NULL,
    organization_type VARCHAR(32)        NOT NULL,
    is_enabled BOOLEAN     NOT NULL DEFAULT true,
    is_leaf    BOOLEAN     NOT NULL DEFAULT true,
    remark            VARCHAR(255),
    display_order     INTEGER            NOT NULL DEFAULT 1,
    owner_tenant_id   BIGINT             NOT NULL,
    is_deleted BOOLEAN     NOT NULL DEFAULT false,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64) NOT NULL
);

CREATE UNIQUE INDEX uni_tenant_organization_code ON organizations (owner_tenant_id, organization_code);
COMMENT ON INDEX uni_tenant_organization_code IS '租户组织编码';

CREATE INDEX idx_organization_parent_id ON organizations (parent_id);
COMMENT ON INDEX idx_organization_parent_id IS '组织的父级id索引';

COMMENT ON TABLE organizations IS '组织机构表';
COMMENT ON COLUMN organizations.id IS '主键id';
COMMENT ON COLUMN organizations.parent_id IS '上级id';
COMMENT ON COLUMN organizations.organization_name IS '组织机构名称';
COMMENT ON COLUMN organizations.organization_code IS '组织编码';
COMMENT ON COLUMN organizations.organization_type IS '组织类型，从数据字典中获取';
COMMENT ON COLUMN organizations.is_enabled IS 'true启用，false禁用';
COMMENT ON COLUMN organizations.is_leaf IS '是否是叶子节点：true 是';
COMMENT ON COLUMN organizations.remark IS '备注';
COMMENT ON COLUMN organizations.owner_tenant_id IS '所属租户的id';
COMMENT ON COLUMN organizations.display_order IS '排序字段';
COMMENT ON COLUMN organizations.is_deleted IS '是否删除： false 未删除';
COMMENT ON COLUMN organizations.created_at IS '创建时间';
COMMENT ON COLUMN organizations.updated_at IS '更新时间';
COMMENT ON COLUMN organizations.created_by IS '创建人';
COMMENT ON COLUMN organizations.updated_by IS '更新人';

INSERT INTO organizations (id, parent_id, organization_name, organization_code, organization_type, is_enabled, is_leaf,
                           remark, display_order, owner_tenant_id, is_deleted, created_at, updated_at, created_by,
                           updated_by)
VALUES (1, -1, '根组织', 'ROOT', 'ROOT', true, true, '根组织', 1, 1, false, now(), now(), 'admin', 'admin');

DROP TABLE IF EXISTS user_positions;
CREATE TABLE user_positions
(
    organization_id       BIGINT       NOT NULL,
    user_id               BIGINT       NOT NULL,
    position_name         VARCHAR(128) NOT NULL,
    is_main               BOOLEAN      NOT NULL DEFAULT false,
    order_in_organization BIGINT       NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64) NOT NULL,
    PRIMARY KEY (organization_id, user_id)
);

COMMENT ON table user_positions IS '用户岗位信息表';
COMMENT ON column user_positions.organization_id IS '组织id';
COMMENT ON column user_positions.user_id IS '用户id';
COMMENT ON column user_positions.position_name IS '职位信息';
COMMENT ON column user_positions.is_main IS '是否是主职部门 true 是 false 否';
COMMENT ON column user_positions.order_in_organization IS '组织内的排序';
COMMENT ON COLUMN user_positions.created_at IS '创建时间';
COMMENT ON COLUMN user_positions.updated_at IS '更新时间';
COMMENT ON COLUMN user_positions.created_by IS '创建人';
COMMENT ON COLUMN user_positions.updated_by IS '更新人';

INSERT INTO user_positions (organization_id, user_id, position_name, is_main, order_in_organization, created_at,
                            updated_at, created_by, updated_by)
VALUES (1, 1, '系统所有者', true, 1, '2024-11-10 21:35:00', '2024-11-10 21:35:00', 'admin', 'admin');

-- 租户空间
DROP TABLE IF EXISTS tenant_space;
CREATE TABLE tenant_space
(
    tenant_id   BIGINT       NOT NULL,
    bucket_name VARCHAR(100) NOT NULL,
    capacity   BIGINT      NOT NULL,
    acl        VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(32) NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    updated_by VARCHAR(32) NOT NULL,
    PRIMARY KEY (tenant_id)
);
COMMENT ON TABLE tenant_space IS '租户空间表';
COMMENT ON COLUMN tenant_space.tenant_id IS '租户id';
COMMENT ON COLUMN tenant_space.bucket_name IS '存储空间名称';
COMMENT ON COLUMN tenant_space.capacity IS '存储容量，GB';
COMMENT ON COLUMN tenant_space.acl IS '存储策略: private, public-read, public-read-write, authenticated-read';
COMMENT ON COLUMN tenant_space.created_at IS '创建时间';
COMMENT ON COLUMN tenant_space.created_by IS '创建人';
COMMENT ON COLUMN tenant_space.updated_at IS '更新时间';
COMMENT ON COLUMN tenant_space.updated_by IS '更新人';


INSERT INTO tenant_space (tenant_id, bucket_name, capacity, acl,
                          created_at, created_by, updated_at, updated_by)
VALUES (1, 'platform-bucket', 1, 'private', now(), 'admin', now(), 'admin');

-- 资源表
DROP TABLE IF EXISTS resources;
CREATE TABLE resources
(
    id           BIGINT PRIMARY KEY,
    parent_id    BIGINT       NOT NULL DEFAULT -1,
    name            VARCHAR(255) NOT NULL,
    size            BIGINT       NOT NULL DEFAULT 0,
    mime_type       VARCHAR(100),
    object_key   VARCHAR(255) NOT NULL,
    version         INT          NOT NULL DEFAULT 1,
    etag         VARCHAR(64)  NOT NULL,
    is_directory BOOLEAN      NOT NULL DEFAULT FALSE,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    is_public       BOOLEAN      NOT NULL DEFAULT FALSE,
    owner_tenant_id BIGINT       NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(32)  NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    updated_by   VARCHAR(32)  NOT NULL,
    UNIQUE (owner_tenant_id, object_key)
);
CREATE INDEX idx_resources_parent_id ON resources (parent_id);
COMMENT ON INDEX idx_resources_parent_id IS '资源表父级id索引';
COMMENT ON TABLE resources IS '存储各种类型的资源信息';
COMMENT ON COLUMN resources.id IS '资源唯一标识';
COMMENT ON COLUMN resources.parent_id IS '父级id';
COMMENT ON COLUMN resources.name IS '资源名称';
COMMENT ON COLUMN resources.size IS '资源大小（字节）';
COMMENT ON COLUMN resources.mime_type IS '资源的 MIME 类型';
COMMENT ON COLUMN resources.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN resources.object_key IS '对象存储中的对象名称';
COMMENT ON COLUMN resources.etag IS 'etag';
COMMENT ON COLUMN resources.version IS '资源的版本号';
COMMENT ON COLUMN resources.is_public IS '是否允许公开访问';
COMMENT ON COLUMN resources.created_at IS '创建时间';
COMMENT ON COLUMN resources.created_by IS '创建人';
COMMENT ON COLUMN resources.updated_at IS '更新时间';
COMMENT ON COLUMN resources.updated_by IS '更新人';

-- 前台用户表（小程序、APP等用户）
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id              BIGINT PRIMARY KEY,
    username        VARCHAR(32) NOT NULL,
    nickname        VARCHAR(64),
    phone           VARCHAR(128),
    gender          VARCHAR(12) NOT NULL,
    avatar          VARCHAR(128),
    email           VARCHAR(128),
    birthday        DATE,
    login_date      TIMESTAMP,
    login_ip        VARCHAR(64),
    is_enabled      BOOLEAN     NOT NULL DEFAULT TRUE,
    is_deleted      BOOLEAN     NOT NULL DEFAULT FALSE,
    owner_tenant_id BIGINT      NOT NULL,
    created_at      TIMESTAMP   NOT NULL,
    created_by      VARCHAR(32) NOT NULL,
    updated_at      TIMESTAMP   NOT NULL,
    updated_by      VARCHAR(32) NOT NULL
);

COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户主键id';
COMMENT ON COLUMN users.username IS '用户名';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.email IS '邮箱地址';
COMMENT ON COLUMN users.birthday IS '生日';
COMMENT ON COLUMN users.gender IS '性别';
COMMENT ON COLUMN users.avatar IS '头像url';
COMMENT ON COLUMN users.login_date IS '登录日期';
COMMENT ON COLUMN users.login_ip IS '登录ip';
COMMENT ON COLUMN users.owner_tenant_id IS '当前用户所属的租户id';
COMMENT ON COLUMN users.is_enabled IS '是否启用：true 启用';
COMMENT ON COLUMN users.is_deleted IS '删除标记：false 未删除';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.created_by IS '创建人';
COMMENT ON COLUMN users.updated_at IS '更新时间';
COMMENT ON COLUMN users.updated_by IS '更新人';

-- 操作日志
DROP TABLE IF EXISTS operation_log;
CREATE TABLE operation_log
(
    id               BIGINT       NOT NULL PRIMARY KEY,
    module_name      VARCHAR(100) NOT NULL,
    request_uri      VARCHAR(500) NOT NULL,
    request_ip       VARCHAR(32)  NOT NULL,
    request_location VARCHAR(64)  NOT NULL,
    request_method   VARCHAR(10)  NOT NULL,
    request_param TEXT,
    response_data    TEXT,
    operation_type   VARCHAR(50)  NOT NULL,
    content       VARCHAR(1024),
    content_en    VARCHAR(1024),
    error_message VARCHAR(1024),
    is_success    BOOLEAN NOT NULL,
    risk_type        VARCHAR(50)  NOT NULL,
    system_name      VARCHAR(100) NOT NULL,
    operation_user   VARCHAR(100) NOT NULL,
    operation_time   TIMESTAMP    NOT NULL,
    owner_tenant_id  BIGINT       NOT NULL
);

-- 添加表注释
COMMENT ON TABLE operation_log IS '系统操作日志表';

-- 添加字段注释
COMMENT ON COLUMN operation_log.id IS '主键ID';
COMMENT ON COLUMN operation_log.module_name IS '模块名称';
COMMENT ON COLUMN operation_log.request_ip IS '请求ip';
COMMENT ON COLUMN operation_log.request_location IS '请求位置';
COMMENT ON COLUMN operation_log.request_uri IS '请求地址';
COMMENT ON COLUMN operation_log.request_method IS '请求方法(GET/POST/PUT/DELETE等)';
COMMENT ON COLUMN operation_log.operation_type IS '操作类型';
COMMENT ON COLUMN operation_log.content IS '日志内容(中文)';
COMMENT ON COLUMN operation_log.content_en IS '日志内容(英文)';
COMMENT ON COLUMN operation_log.operation_user IS '操作人';
COMMENT ON COLUMN operation_log.operation_time IS '操作时间';
COMMENT ON COLUMN operation_log.is_success IS '操作状态(true/false)';
COMMENT ON COLUMN operation_log.error_message IS '错误信息';
COMMENT ON COLUMN operation_log.owner_tenant_id IS '所属的租户id';
COMMENT ON COLUMN operation_log.risk_type IS '操作风险类型';
COMMENT ON COLUMN operation_log.request_param IS '请求参数(JSON格式)';
COMMENT ON COLUMN operation_log.response_data IS '响应数据,isSuccess为false时，这里显示错误信息';
COMMENT ON COLUMN operation_log.system_name IS '系统名称';

-- 创建索引
CREATE INDEX idx_operation_log_module_name ON operation_log (owner_tenant_id, module_name);
CREATE INDEX idx_operation_log_operation_type ON operation_log (owner_tenant_id, operation_type);
CREATE INDEX idx_operation_log_risk_type ON operation_log (owner_tenant_id, risk_type);

COMMENT ON index idx_operation_log_module_name IS '模块名称';
COMMENT ON index idx_operation_log_operation_type IS '操作类型';
COMMENT ON index idx_operation_log_risk_type IS '风险类型';


DROP TABLE IF EXISTS param_configs;
CREATE TABLE param_configs
(
    id                BIGSERIAL    NOT NULL PRIMARY KEY,
    config_name       VARCHAR(128) NOT NULL,
    config_key        VARCHAR(256) NOT NULL,
    config_value      VARCHAR(256) NOT NULL,
    group_key         VARCHAR(31)  NOT NULL,
    remark            VARCHAR(512),
    is_allow_modified BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP    NOT NULL,
    created_by        VARCHAR(32)  NOT NULL,
    updated_at        TIMESTAMP    NOT NULL,
    updated_by        VARCHAR(32)  NOT NULL
);
COMMENT ON TABLE param_configs IS '系统参数';
COMMENT ON COLUMN param_configs.id IS '主键id';
COMMENT ON COLUMN param_configs.config_name IS '配置名称';
COMMENT ON COLUMN param_configs.config_key IS '配置key';
COMMENT ON COLUMN param_configs.config_value IS '配置值';
COMMENT ON COLUMN param_configs.group_key IS '分组标识';
COMMENT ON COLUMN param_configs.remark IS '备注';
COMMENT ON COLUMN param_configs.created_at IS '创建时间';
COMMENT ON COLUMN param_configs.created_by IS '创建人';
COMMENT ON COLUMN param_configs.updated_at IS '更新时间';
COMMENT ON COLUMN param_configs.updated_by IS '更新人';

-- 创建索引
CREATE INDEX idx_param_config_group_key ON param_configs (group_key, config_key);
COMMENT ON INDEX idx_param_config_group_key IS '参数配置的分组标识和配置key的组合索引';



-- 登录失败
INSERT INTO param_configs (id, config_name, config_key, config_value, group_key, remark, created_at, created_by,
                           updated_at, updated_by)
VALUES (1, '登录失败警告次数', 'LOGIN_FAIL_WARING_COUNT', '3', 'LOGIN_ERROR', '登录失败警告次数', now(), 'admin', now(),
        'admin');
INSERT INTO param_configs
(id, config_name, config_key, config_value, group_key, remark, created_at, created_by,
 updated_at, updated_by)
VALUES (2, '登录失败警告', 'LOGIN_FAIL_WARING_MESSAGE', '您已尝试%s次登录失败，%s次后将被锁定！',
        'LOGIN_ERROR', '登录失败时的警告，超出登录失败警告次数，即触发', now(), 'admin', now(),
        'admin');
INSERT INTO param_configs (id, config_name, config_key, config_value, group_key, remark, created_at, created_by,
                           updated_at, updated_by)
VALUES (3, '登录失败错误提示', 'LOGIN_FAIL_MESSAGE', '3', 'LOGIN_ERROR', '登录失败的错误信息', now(), 'admin', now(),
        'admin');
INSERT INTO param_configs (id, config_name, config_key, config_value, group_key, remark, created_at, created_by,
                           updated_at, updated_by)
VALUES (4, '登录失败的最大次数', 'LOGIN_FAIL_MAX_ERROR_COUNT', '5', 'LOGIN_ERROR', '登录失败的最大次数', now(), 'admin',
        now(),
        'admin');
INSERT INTO param_configs (id, config_name, config_key, config_value, group_key, remark, created_at, created_by,
                           updated_at, updated_by)
VALUES (5, '登录失败锁定时间', 'LOGIN_FAIL_LOCKED_TIME', '15', 'LOGIN_ERROR', '登录失败的锁定时间', now(), 'admin',
        now(),
        'admin');



