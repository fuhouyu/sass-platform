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

import {Organization as OrganizationModal} from "@/model/organization.tsx";
import React, {useEffect} from "react";
import {organizationApi} from "@/apis/organization.tsx";

/**
 * 设置树数据
 * @param list 菜单集合
 * @param key key
 * @param children 子集
 */
const updateTreeData = (list: OrganizationModal[], key: React.Key, children: OrganizationModal[]): OrganizationModal[] => {
    return list.map((node: OrganizationModal) => {
        if (node.id === key) {
            return {
                ...node,
                children,
            };
        }
        if (node.children) {
            return {
                ...node,
                children: updateTreeData(node.children, key, children),
            };
        }
        return node;
    });

}


/**
 * 懒加载组织树
 */
export function useOrganizationLazyData(setLazyData: (value: (((prevState: OrganizationModal[]) => OrganizationModal[]) | OrganizationModal[])) => void) {

    useEffect(() => {
        // 先查询出一级菜单
        const initOrganization = async () => {
            const organizations = await organizationApi.getOrganizationListApi();
            setLazyData(organizations);
        }
        initOrganization().then();
    }, [setLazyData]);

    /**
     * 懒加载树
     * @param key key，这里是主键id
     * @param children 子菜单
     */
    const onLoadData = async ({key, children}: { key: React.Key, children?: OrganizationModal[] | undefined }) => {
        if (children) {
            return new Promise<void>((resolve) => {
                resolve()
            })
        }
        const res = await organizationApi.getOrganizationListApi(key.toString());
        const lazyData = (origin: OrganizationModal[]) => updateTreeData(origin, key, res);
        setLazyData(lazyData);
    }
    return {onLoadData}
}