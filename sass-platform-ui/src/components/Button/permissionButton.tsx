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


import {FC, ReactNode} from "react";
import {IMenu} from "@/types/menu";

export interface PermissionButtonProps {
  buttonPermissions: IMenu[],
    permissionStr: string,
  children: ReactNode
}

/**
 * 按照权限渲染按钮
 * @param buttonPermissions 按钮权限
 * @param permissionStr 权限字符串
 * @param children 按钮组件
 * @constructor PermissionButton
 */
export const PermissionButton: FC<PermissionButtonProps> = ({
                                                                      buttonPermissions,
                                                                      permissionStr,
                                                                      children
                                                                  }: PermissionButtonProps) => {
    const hasPermission = buttonPermissions.some(item => item?.permissionCode === permissionStr);
    return (
        <>
            {hasPermission && children}
        </>
    )
}
