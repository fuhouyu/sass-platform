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

import {Organization, Organization as OrganizationModal} from "@/model/organization.tsx";
import {useCallback, useState} from "react";
import {organizationApi} from "@/apis/organization.tsx";

/**
 * 设置树数据
 * @param treeData
 * @param key key
 * @param children 子集
 */
function updateTreeData(
    treeData: OrganizationModal[],
    key: React.Key,
    children: OrganizationModal[]
): OrganizationModal[] {
    return treeData.map(node => {
        if (node.id === key) {
            return {...node, children: [...children]}; // 返回新引用
        }
        if (node.children) {
            return {...node, children: updateTreeData(node.children, key, children)}; // 深度更新
        }
        return {...node}; // 确保返回新引用
    });
}



/**
 * 懒加载组织树
 */
export function useOrganizationLazyData(): {
    initOrganization: () => Promise<void>,
    organizationLazyData: Organization[];
    onLoadData: ({key, children}: { key: React.Key; children?: Organization[] }) => Promise<void>
} {
    // 维护 lazyData 状态
    const [organizationLazyData, setOrganizationLazyData] = useState<OrganizationModal[]>([]);

    /**
     * 初始化data
     */
    const initOrganization = useCallback(async () => {
        const organizations = await organizationApi.getOrganizationListApi();
        setOrganizationLazyData(organizations);
    }, [setOrganizationLazyData]);


    /**
     * 懒加载树
     * @param key 主键 ID
     * @param children 子菜单
     */
    const onLoadData = async ({key, children}: { key: React.Key; children?: OrganizationModal[] }) => {
        if (children) {
            return Promise.resolve();
        }
        const res = await organizationApi.getOrganizationListApi(key as string);
        setOrganizationLazyData(prevState => updateTreeData(prevState, key, res));
    };

    return {initOrganization, organizationLazyData, onLoadData};
}
