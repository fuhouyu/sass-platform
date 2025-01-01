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
        statusPlaceholder: '请选择状态',
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
        paramsError: '参数错误',
        pending: '处理中，请稍候...'
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
        title: '后台管理',
        logout: '退出',
    },
    // 登录
    Login: {
        usernamePlaceholder: '请输入用户名',
        usernameEmptyMessage: '请输入用户名',
        passwordEmptyMessage: '请输入用户密码',
        passwordPlaceholder: '密码必填',
        otherLogin: '其它登录方式',
        wechatLogin: '微信登录',
        usernamePasswordLogin: '账号密码',
        usernamePasswordLoginTitle: '请使用<span>账号密码</span>登录',
        weLinkLogin: 'WeLink 登录',
        weLinkLoginTitle: '请使用<span>WeLink 扫码</span>登录',
        weLinkQRTips: "打开<span>WeLink APP</span> - 点击右上角“<span>+</span>”图标 - 点击<span>扫一扫</span>",
        loginButton: '登录',
    },
    // 租户
    Tenant: {
        add: '新增租户',
        edit: '修改租户',
        list: '租户列表',
        name: '租户名称',
        namePlaceholder: '请输入租户名称',
        code: '租户编码',
        codePlaceholder: '请输入租户编码',
        codeExistsErrorMessage: '租户编码已存在',
        type: '租户类型',
        typePlaceholder: '请选择租户类型',
        contactPerson: '联系人',
        contactPersonPlaceholder: '请输入联系人',
        contactInfo: '联系信息',
        contactInfoPlaceholder: '请输入联系信息',
        permissions: '租户权限',
        permissionsPlaceholder: '请选择租户权限',

    },
    // 菜单
    Menu: {
        main: '权限列表',
        home: '主页',
        detail: '详情',
        add: '新增',
        edit: '修改',
        delete: '删除',
        systemManage: '系统管理',
        tenantManage: '租户管理',
        userManage: '用户管理',
        roleManage: '角色管理',
        permissionManage: '权限管理',
        dictManage: '字典管理',
        dictItem: '字典项',
        profile: '个人资料',
        accountSettings: '账号设置',
    },
    Account: {
        thirdPartyAccount: '第三方账号',
        welink: 'WeLink 账号',
        bind: '绑定账号',
        alreadyBind: '已绑定',
        unbind: '取消绑定',
        bindSuccess: '绑定成功',
    },
    User: {
        list: '用户列表',
        add: '新增用户',
        edit: '修改用户',
        email: '邮箱',
        emailPlaceholder: '请输入邮箱',
        emailCheckMessage: '请输入正确的邮箱',
        gender: '性别',
        genderPlaceholder: '请选择性别',
        loginDate: '最后登录时间',
        loginIp: '最后登录IP',
        realName: '真实姓名',
        realNamePlaceholder: '请输入真实姓名',
        nickname: '昵称',
        nicknamePlaceholder: '请输入昵称',
        username: '用户名',
        usernamePlaceholder: '请输入用户名',
        usernameCheckMessage: '请输入用户名',
        usernameExistsErrorMessage: '用户名已存在',
        password: '密码',
        passwordPlaceholder: '请输入密码',
        passwordCheckMessage: '请输入密码',
        male: '男',
        female: '女',

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
        codeTips: '角色编码当前租户下唯一',
        codeExistsErrorMessage: '角色编码已存在',
        dataScope: '数据权限',
        dataScopePlaceholder: '请选择数据权限',
        permissionIds: '角色权限',
    },
    DictType: {
        add: '新增字典类型',
        edit: '修改字典类型',
        list: '字典类型列表',
        name: '字典名称',
        namePlaceholder: '请输入字典名称',
        code: '字典编码',
        codePlaceholder: '请输入字典编码',
        codeTips: '字典编码，当前租户下唯一'
    },
    DictItem: {
        add: '新增字典项',
        edit: '修改字典项',
        list: '字典项列表',
        name: '字典项名称',
        namePlaceholder: '请输入字典项名称',
        code: '字典项编码',
        codePlaceholder: '请输入字典项编码',
        codeTips: '字典项编码，当前字典类型下唯一'
    },
}

export {
    ZhCN
}