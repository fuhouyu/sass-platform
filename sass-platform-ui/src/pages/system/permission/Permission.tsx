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


import React, {Key, useEffect, useState} from "react";
import {DownOutlined} from "@ant-design/icons";
import {Button, Col, Input, Row, Table, TableColumnsType, Tree} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import './index.scss'
import {IconFont} from "@/components";
import {useTranslation} from "react-i18next";

/**
 * 设置树数据
 * @param list 菜单集合
 * @param key key
 * @param children 子集
 */
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
    const [tableData, setTableData] = useState<Menu[]>([])
    const {t} = useTranslation();
    useEffect(() => {
        // 先查询出一级菜单
        permissionApi.getPermissionListApi()
            .then((res: Menu[]) => {
                res.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
                setTreeData(res);
            })
    }, [t])

    const columns: TableColumnsType = [
        {
            title: t('Permission.name'),
            dataIndex: 'permissionName',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('Permission.code'),
            dataIndex: 'permissionCode',

        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
        },
        // {
        //     title: '操作',
        //     dataIndex: 'action',
        //     render: (_, record: Userinfo) => {
        //         return (<>
        //             <Space size="middle" style={{whiteSpace: 'nowrap'}}>
        //                 <a onClick={() => openModal(record.id)}>修改</a>
        //             </Space>
        //         </>)
        //     }
        // }
    ];

    /**
     * 树被点击时的事件
     * @param selectedKeys 当前选中的key
     */
    const onSelectTree = async (selectedKeys: Key[]) => {
        if (!selectedKeys || selectedKeys.length === 0) {
            return
        }
        // 这里只会有一条
        const child = await permissionApi.getPermissionListApi(selectedKeys[0].toLocaleString())
        child.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
        setTableData(child);

    }

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
        res.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
        setTreeData((origin) => updateTreeData(origin, key, res));
    }
    return (
        <>
            <Row gutter={24} className={'main-container'}>
                <Col span={3} className={'tree-container'}>
                    <Input
                        className='search-input'
                        placeholder={t('Permission.namePlaceholder')} allowClear/>
                    <div className='tree-info'>
                        <Tree
                            showLine
                            fieldNames={{key: 'id', title: 'permissionName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={treeData}
                            onSelect={onSelectTree}
                        />
                    </div>
                </Col>
                <Col span={21}>
                    <div className="title-line">
                        <div className="buttons">
                            <Button className="add-button"
                                    icon={<IconFont type="i-add"/>}
                            >
                                新增
                            </Button>
                            <Button className="del-button"
                                    icon={<IconFont type="i-delete"/>}>
                                删除
                            </Button>
                        </div>
                    </div>
                    <Table columns={columns} rowKey='id' dataSource={tableData} className='table-info'/>
                </Col>
            </Row>


        </>
    )

}

