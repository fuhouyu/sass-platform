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
import {Input, Space, Splitter, Table, TableColumnsType, Tree} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import {Userinfo} from "@/model/user";

const updateTreeData = (list: Menu[], key: React.Key, children: Menu[]): Menu[] => {
    return list.map((node: Menu) => {
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

export const Permission: React.FC = () => {
    const [treeData, setTreeData] = useState<Menu[]>([]);

    useEffect(() => {
        // 先查询出一级菜单
        permissionApi.getPermissionListApi()
            .then((res: Menu[]) => {
                setTreeData(res);
            })
    }, [])

    const columns: TableColumnsType = [
        {
            title: '权限名称',
            dataIndex: 'permissionName',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: '权限编码',
            dataIndex: 'permissionCode',

        },
        {
            title: '显示顺序',
            dataIndex: 'displayOrder',
            defaultSortOrder: 'descend',
        },
        {
            title: '创建时间',
            dataIndex: 'createAt',
            sorter: true,
            showSorterTooltip: false
        },
        {
            title: '创建人',
            dataIndex: 'createBy'
        },
        {
            title: '更新时间',
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: '操作人',
            dataIndex: 'updateBy',
        },
        {
            title: '操作',
            dataIndex: 'action',
            render: (_, record: Userinfo) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => openModal(record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    /**
     * 懒加载菜单
     * @param key key，这里是主键id
     * @param children 子菜单
     */
    const onLoadData = async ({key, children}: { key: React.Key, children?: Menu[] | undefined }) => {
        if (children) {
            return new Promise<void>((resolve) => {
                resolve()
            })
        }
        const res = await permissionApi.getPermissionListApi(key.toString());
        setTreeData((origin) => updateTreeData(origin, key, res));
    }
    return (
        <>
            <div className='permission-container'>
                <Splitter className='permission-splitter'>
                    <Splitter.Panel defaultSize="30%" min="20%" max="70%">
                        <Input style={{
                            margin: '0 0 .5rem 0'
                        }} placeholder='请输入权限名称' allowClear/>
                        <Tree
                            style={{
                                padding: '.5rem',
                                height: '100%'
                            }}
                            height={1000}
                            showLine
                            fieldNames={{key: 'id', title: 'permissionName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={treeData}
                        />
                    </Splitter.Panel>
                    <Splitter.Panel>

                        <Table columns={columns}/>
                    </Splitter.Panel>
                </Splitter>
            </div>

        </>
    )

}

