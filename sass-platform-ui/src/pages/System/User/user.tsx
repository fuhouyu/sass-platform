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
import {Modal, Space, Table, TableColumnsType} from "antd";
import {getUserinfoByIdApi, getUserListApi, removeUserApi} from "@/apis/user";
import {PageList} from "@components";
import './index.scss'
import {IconFont} from "@/components";
import {UserinfoInterface} from "@/model/user";
import {Userinfo} from "@/pages/Userinfo/userinfo";

const User: React.FC = () => {

    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [userId, setUserId] = useState<number | null>(null);

    const modalHandle = (isModalOpen?: boolean = false,
                         id?: number | null) => {
        setIsModalOpen(isModalOpen);
        setUserId(id)
    }

    const columns: TableColumnsType = [
        {
            title: '用户名',
            dataIndex: 'username',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: '真实姓名',
            dataIndex: 'realName',
            defaultSortOrder: 'descend',
        },
        {
            title: '昵称',
            dataIndex: 'nickname',
        },
        {
            title: '性别',
            dataIndex: 'gender',
        },
        {
            title: '登录时间',
            dataIndex: 'loginDate',
        },
        {
            title: '登录ip',
            dataIndex: 'loginIp',
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
            render: (_, record: UserinfoInterface) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => modalHandle(true, record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    const [detailDataSource, setDetailDataSource] = useState<object[]>([]);

    const userDetailColumns = [
        {
            dataIndex: 'attribute',
            key: 'attribute',
            render: (text: string) => <strong>{text}：</strong>, // 增加冒号和加粗
        },
        {
            dataIndex: 'value',
            key: 'value',
        },
    ];

    useEffect(() => {
        if (!isModalOpen) {
            return
        }
        getUserinfoByIdApi(userId)
            .then((res: UserinfoInterface) => {
                setDetailDataSource([
                    {
                        "key": "username",
                        "attribute": "用户名",
                        "value": res.username
                    }
                ])
            })
    }, [isModalOpen]);
    return (
        <>
            <PageList pageListInterface={{listName: '用户', columns: columns}} pageRequestApi={getUserListApi}
                      deleteButtonApi={removeUserApi}/>
            <Modal
                title="用户修改"
                className="ant-modal-header"
                open={isModalOpen}
                onOk={() => modalHandle(false)}
                onCancel={() => modalHandle(false)}
                footer="Footer"
                closeIcon={<IconFont type="i-close-circle" style={{
                    fontSize: '24px',
                }}/>}
            >
            </Modal>
        </>
    )
}

export default User;