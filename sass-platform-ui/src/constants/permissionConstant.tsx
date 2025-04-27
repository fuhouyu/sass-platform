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
 * 租户权限常量
 */
class TenantPermissionConstant {

    /**
     * 租户列表权限
     */
    static readonly List: string = 'tenant:list';

    /**
     * 租户添加权限
     */
    static readonly ADD: string = 'tenant:add';

    /**
     * 租户修改权限
     */
    static readonly EDIT: string = 'tenant:edit';

    /**
     * 租户删除权限
     */
    static readonly DELETE: string = 'tenant:delete';

    /**
     * 租户空间
     */
    static readonly TENANT_SPACE: string = 'tenant:space';
}

/**
 * 用户权限常量
 */
class UserPermissionConstant {
    /**
     * 用户列表权限
     */
    static readonly List: string = 'system:user:list';

    /**
     * 用户添加权限
     */
    static readonly ADD: string = 'system:user:add';

    /**
     * 用户修改权限
     */
    static readonly EDIT: string = 'system:user:edit';

    /**
     * 用户删除权限
     */
    static readonly DELETE: string = 'system:user:delete';

}


/**
 * 角色权限常量
 */
class RolePermissionConstant {
    /**
     * 角色列表权限
     */
    static readonly List: string = 'system:role:list';

    /**
     * 角色添加权限
     */
    static readonly ADD: string = 'system:role:add';

    /**
     * 角色修改权限
     */
    static readonly EDIT: string = 'system:role:edit';

    /**
     * 角色删除权限
     */
    static readonly DELETE: string = 'system:role:delete';

}

/**
 * 权限常量
 */
class PermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'system:permission:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'system:permission:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'system:permission:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'system:permission:delete';

}

/**
 * 字典常量
 */
class DictTypePermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'system:dict-type:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'system:dict-type:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'system:dict-type:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'system:dict-type:delete';

}


/**
 * 字典项常量
 */
class DictItemPermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'system:dict-item:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'system:dict-item:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'system:dict-item:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'system:dict-item:delete';
}

/**
 * 组织常量
 */
class OrganizationPermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'system:organization:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'system:organization:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'system:organization:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'system:organization:delete';

    /**
     * 新增成员
     */
    static readonly ADD_MEMBER: string = 'system:organization:add-member';

    /**
     * 删除成员
     */
    static readonly DELETE_MEMBER: string = 'system:organization:delete-member';
}

/**
 * 参数配置常量
 */
class ParamConfigPermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'system:param-config:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'system:param-config:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'system:param-config:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'system:param-config:delete';
}


/**
 * 应用常量
 */
class TenantResourcePermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'tenant:resource:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'tenant:resource:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'tenant:resource:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'tenant:resource:delete';

    /**
     * 预览
     */
    static readonly PREVIEW: string = "tenant:resource:preview";

    /**
     * 分享
     */
    static readonly SHARE: string = "tenant:resource:share";

    /**
     * 下载
     */
    static readonly DOWNLOAD: string = "tenant:resource:download";


    /**
     * 在线编译
     */
    static readonly OFFICE_EDIT: string = "tenant:resource:office-edit";

}



/**
 * 应用常量
 */
class ApplicationPermissionConstant {
    /**
     * 列表权限
     */
    static readonly List: string = 'application:list';

    /**
     * 添加权限
     */
    static readonly ADD: string = 'application:add';

    /**
     * 修改权限
     */
    static readonly EDIT: string = 'application:edit';

    /**
     * 删除权限
     */
    static readonly DELETE: string = 'application:delete';

}

/**
 * 在线用户
 */
class OnlineUserPermissionConstant {
    static readonly List: string = 'monitor:online-user:list';
    static readonly ForceLogout: string = 'monitor:online-user:logout';
}

export {
    TenantPermissionConstant,
    UserPermissionConstant,
    RolePermissionConstant,
    PermissionConstant,
    DictTypePermissionConstant,
    DictItemPermissionConstant,
    OrganizationPermissionConstant,
    ParamConfigPermissionConstant,
    ApplicationPermissionConstant,
    TenantResourcePermissionConstant,
    OnlineUserPermissionConstant
}