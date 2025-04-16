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


import {ReactNode, useCallback} from "react";
import {IconFont} from "@/components";
import {MenuProps as AntdMenuProps, TreeDataNode} from "antd";
import {Menu, MenuType} from "@/model/menu";
import {useTranslation} from "react-i18next";

export type MenuProps = Required<AntdMenuProps>['items'][number];

export type MenuTreeType = {
    id?: string | undefined;
    key: string;
    children?: MenuTreeType[] | undefined | null;
    icon?: ReactNode;
} & (MenuProps | TreeDataNode);

export function useMenuTree(menus: Menu[], excludeType?: MenuType[]): MenuTreeType[] {
    const {t} = useTranslation();
    const convertMenuItem = useCallback((permissionInterfaces: Menu[], parentPath?: string | undefined): (MenuTreeType[] | undefined | null) => {
        if (permissionInterfaces === undefined || permissionInterfaces.length === 0) {
            return undefined;
        }
        const menus = permissionInterfaces?.map((item: Menu) => {
            if (!item.isVisible || excludeType?.includes(item.permissionType!)) {
                return undefined;
            }
            const routePath = parentPath ? parentPath + '/' + item.routePath : item.routePath
            return {
                id: item.id,
                key: routePath,
                title: t(`${item.permissionName}`),
                label: t(`${item.permissionName}`),
                icon: item.icon ?
                    <IconFont type={item.icon} style={{fontSize: '1rem'}}/> : undefined,
                children: item.children ? convertMenuItem(item.children, routePath) : undefined,
            };
        }).filter(Boolean) as MenuTreeType[];
        return menus.length > 0 ? menus : null;
    }, [excludeType, t])


    return convertMenuItem(menus) ?? [];
}
