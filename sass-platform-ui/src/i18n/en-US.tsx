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
const EnUS = {
    // 公共参数
    Common: {
        createBy: 'Create By',
        createAt: 'Create At',
        updateBy: 'Update By',
        updateAt: 'Update At',
        status: 'Status',
        statusPlaceholder: 'Please choose Status',
        displayOrder: 'Display Order',
        displayOrderPlaceholder: 'Please enter Display Order',
        remark: 'Remark',
        reset: 'Reset',
        action: 'Action',
        success: 'Success!',
        error: 'Error!',
        yes: 'Yes',
        no: 'No',
        enabled: 'Enabled',
        disabled: 'Disabled',

    },
    // button
    Button: {
        add: 'Add',
        edit: 'Edit',
        delete: 'Delete',
        search: 'Search',
        confirm: 'Confirm',
        submit: 'Submit',
        cancel: 'Cancel'
    },
    // header
    Header: {
        title: 'Sass Platform',
        personCenter: 'Person Center',
        logout: 'Logout',
    },
    // 登录
    Login: {
        usernamePlaceholder: 'Please enter your username',
        usernameEmptyMessage: 'Please enter your username!',
        passwordPlaceholder: 'Please enter your password',
        passwordEmptyMessage: 'Please enter your password!',
        otherLogin: 'Alternative Login Options',
        wechatLogin: 'Wechat Login',
        weLinkLogin: 'WeLink QR Login',
        loginButton: 'Login',
    },
    // 菜单
    Menu: {
        main: 'Permission List',
        home: 'Home',
        systemManage: 'System Manage',
        tenantManage: 'Tenant Manage',
        userManage: 'User Manage',
        roleManage: 'Role Manage',
        permissionManage: 'Permission Manage',
        dictManage: 'Dictionary Manage',
        dictItem: 'Dictionary Item',

    },
    // 租户
    Tenant: {
        add: 'Add Tenant',
        edit: 'Edit Tenant',
        list: 'Tenant List',
        name: 'Tenant Name',
        namePlaceholder: 'Please enter Tenant Name',
        code: 'Tenant Code',
        codePlaceholder: 'Please enter Tenant Code',
        codeExistsErrorMessage: 'Tenant Code is exists',
        type: 'Tenant Type',
        typePlaceholder: 'Please choose Tenant Type',
        contactPerson: 'Contact Person',
        contactPersonPlaceholder: 'Please enter Contact Person',
        contactInfo: 'Contact Info',
        contactInfoPlaceholder: 'Please enter Contact Info',
        permissions: 'Tenant Permission',
        permissionsPlaceholder: 'Please choose Tenant Permission',
    },
    // 权限
    Permission: {
        add: 'Add Permission',
        edit: 'Edit Permission',
        parentPermission: 'Parent Permission',
        type: 'Permission Type',
        typeCheckMessage: 'Please choose Permission Type',
        DIR: 'Directory',
        MENU: 'Menu',
        BUTTON: 'Button',
        icon: 'Permission Icon',
        iconPlaceholder: 'Please enter Icon Name',
        listName: 'Permission List',
        name: 'Permission Name',
        namePlaceholder: 'Please enter Permission Name',
        nameCheckMessage: 'Please enter Permission Name',
        code: 'Permission Code',
        codePlaceholder: 'Please enter Permission Code',
        codeExistsErrorMessage: 'Permission Code exists!',
        codeCheckMessage: 'Please enter Permission Code',
        codeTips: 'Permission code, unique within the current tenant.',
        isFrame: 'Is Frame',
        routePath: 'Route Path',
        routePathTips: 'The accessed route address, relative path, such as: `system`',
        routePathPlaceHolder: 'Please enter Route Path',
        routePathCheckMessage: 'Please enter Route Path',
        componentPath: 'Component Path',
        componentPathPlaceholder: 'Please enter Component Path',
        routeParams: 'Route Params',
        routeParamsPlaceholder: 'Please enter Route Params',
        displayStatus: 'Display Status',
    },
    // 角色
    Role: {
        list: 'Role List',
        add: 'Add Role',
        edit: 'Edit Role',
        codeTips: 'The Role Code is unique under the current tenant',
        codeExistsErrorMessage: 'Role Code is exists',
        name: 'Role Name',
        namePlaceholder: 'Please enter Role Name',
        code: 'Role Code',
        codePlaceholder: 'Please enter Role Code',
        dataScope: 'Data Scope',
        dataScopePlaceholder: 'Please choose Role Code',
        permissionIds: 'Role Permission',

    },
    // 字典类型
    DictType: {
        add: 'Add Dictionary Type',
        edit: 'Edit Dictionary Type',
        list: 'Dictionary Type List',
        name: 'Dictionary Name',
        namePlaceholder: 'Please enter Dictionary Name',
        code: 'Dictionary Code',
        codePlaceholder: 'Please enter Dictionary Code',
    },
    DictItem: {
        add: 'Add Dictionary Item',
        edit: 'Edit Dictionary Item',
        list: 'Dictionary Item List',
        name: 'Dictionary Item Name',
        namePlaceholder: 'Please enter Dictionary Item Name',
        code: 'Dictionary Item Code',
        codePlaceholder: 'Please enter Dictionary Item Code',
    }
}

export {
    EnUS
}