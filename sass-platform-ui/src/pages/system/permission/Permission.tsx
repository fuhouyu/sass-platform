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


import React, {useEffect, useState} from "react";
import {DownOutlined} from "@ant-design/icons";
import {Tree} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import {useMenuTree} from "@/hooks/useMenuTree";

export const Permission: React.FC = () => {
    const [menus, setMenus] = useState<Menu[]>();

    useEffect(() => {
        permissionApi.getUserPermissionApi()
            .then((res: Menu[]) => {
                setMenus(res);
            })
    }, [])
    const menuTree = useMenuTree(menus!);
    return (
        <>
            <div>
                <Tree
                    showLine
                    switcherIcon={<DownOutlined/>}
                    // onSelect={onSelect}
                    treeData={menuTree}
                />
            </div>
        </>
    )

}