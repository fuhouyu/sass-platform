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


import React, {useState} from "react";
import {Modal, Space, TableColumnsType} from "antd";
import {getUserListApi} from "@/apis/user";
import {PageList} from "@components";
import './index.scss'

const User: React.FC = () => {

    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);


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
            render: () => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a>修改</a>
                        <a onClick={() => setIsModalOpen(true)}>详情</a>
                    </Space>
                </>)
            }
        }
    ];

    return (
        <>
            <PageList pageListInterface={{listName: '用户', columns: columns}} pageRequestApi={getUserListApi}/>
            <Modal
                title="用户详情"
                className="ant-modal-header"
                open={isModalOpen}
                onOk={() => setIsModalOpen(false)}
                onCancel={() => setIsModalOpen(false)}
                footer="Footer"
            >
                <p>Some contents...</p>
                <p>Some contents...</p>
                <p>Some contents...</p>
            </Modal>
        </>
    )
}

export default User;