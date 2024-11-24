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


import {useCallback} from "react";
import {IconFont} from "@/components";
import {MenuProps, TreeDataNode} from "antd";
import {Menus} from "@/model/menus";


export type MenuType = Required<MenuProps>['items'][number];

export function useMenuTree<T = MenuType | TreeDataNode>(menus: Menus[]): T[] {
    const convertMenuItem = useCallback((permissionInterfaces: Menus[]): (T[] | undefined | null) => {
        if (permissionInterfaces === undefined || permissionInterfaces.length === 0) {
            return undefined;
        }
        return permissionInterfaces?.map((item: Menus) => {
            return {
                key: item.routePath ?? item.id,
                title: item.permissionName,
                label: item.permissionName,
                icon: item.icon ?
                    <IconFont type={item.icon} style={{fontSize: '16px'}}/> : undefined,
                children: item.children ? convertMenuItem(item.children) : undefined
            } as unknown as T;
        })
    }, [])


    return convertMenuItem(menus) ?? [];
}
