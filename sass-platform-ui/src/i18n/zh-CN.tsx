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
const ZhCN = {
    // 公共参数
    Common: {
        createBy: '创建人',
        createAt: '创建时间',
        updateBy: '操作人',
        updateAt: '操作时间',
        status: '状态',
        displayOrder: '显示顺序',
        displayOrderPlaceholder: '请输入显示顺序',
        remark: '备注',
        reset: '重置',
        action: '操作',
        success: '成功！',
        failed: '失败！',
        yes: '是',
        no: '否',
        enabled: '启用',
        disabled: '禁用',
    },
    // button
    Button: {
        add: '新增',
        edit: '修改',
        delete: '删除',
        search: '搜索',
        confirm: '确定',
        submit: '提交',
        cancel: '取消'
    },
    // header
    Header: {
        title: '多租户后台管理系统',
        personCenter: '个人中心',
        logout: '退出',
    },
    //登录
    Login: {
        usernamePlaceholder: '请输入用户名',
        usernameEmptyMessage: '请输入用户名!',
        passwordEmptyMessage: '请输入用户密码!',
        passwordPlaceholder: '请输入密码',
        otherLogin: '其它登录方式',
        wechatLogin: '微信登录',
        weLinkLogin: 'WeLink 扫码',
        loginButton: '登录',
    },
    // 菜单
    Menu: {
        main: '权限列表',
        home: '主页',
        system: '系统管理',
        tenant: '租户管理',
        user: '用户管理',
        role: '角色管理',
        permission: '权限管理',
    },
    // 权限
    Permission: {
        add: '新增权限',
        edit: '修改权限',
        parentPermission: '上级权限',
        type: '权限类型',
        typeCheckMessage: '请选择权限类型！',
        DIR: '目录',
        MENU: '菜单',
        BUTTON: '按钮',
        icon: ' 权限图标',
        iconPlaceholder: '请输入图标名称',
        listName: '权限列表',
        name: '权限名称',
        namePlaceholder: '请输入权限名称',
        nameCheckMessage: '请输入权限名称',
        code: '权限编码',
        codeTips: '权限编码，当前租户下唯一',
        codePlaceholder: '请输入权限编码',
        codeCheckMessage: '请输入权限编码',
        codeExistsErrorMessage: '权限编码已存在！',
        isFrame: '是否外链',
        routePath: '路由地址',
        routePathTips: '访问的路由地址，相对路径，如：`system`',
        routePathPlaceHolder: '请输入路由地址',
        routePathCheckMessage: '请输入路由地址',
        componentPath: '组件路径',
        componentPathPlaceholder: '请输入组件路径',
        componentPathCheckMessage: '请输入组件路径',
        routeParams: '路由参数',
        routeParamsPlaceholder: '请输入路由参数',
        displayStatus: '显示状态',
        status: '权限状态',
    },
    Role: {
        list: '角色列表',
        add: '新增角色',
        edit: '修改角色',
        name: '角色名称',
        namePlaceholder: '请输入角色名称',
        code: '角色编码',
        codePlaceholder: '请输入角色编码',
        dataScope: '数据权限',
        dataScopePlaceholder: '请选择数据权限',
    }
}

export {
    ZhCN
}