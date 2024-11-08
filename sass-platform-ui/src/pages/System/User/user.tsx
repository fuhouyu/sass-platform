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
import {Button, Col, Form, Input, message, Modal, Radio, Row, Space, TableColumnsType} from "antd";
import {getUserinfoByIdApi, getUserListApi, removeUserApi} from "@/apis/user";
import {PageList} from "@components";
import './index.scss'
import {IconFont} from "@/components";
import {UserinfoInterface} from "@/model/user";

const User: React.FC = () => {

    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [form] = Form.useForm();

    /**
     * 打开模态组
     * @param userId 用户id
     */
    const openModal = (userId: number) => {
        setIsModalOpen(true);
        getUserinfoByIdApi(userId)
            .then((res: UserinfoInterface) => form.setFieldsValue({...res}))
            .catch((err: Error) => {
                message.error(err.message)
            })
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
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
                        <a onClick={() => openModal(record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];



    return (
        <>
            <PageList pageListInterface={{listName: '用户', columns: columns}} pageRequestApi={getUserListApi}
                      deleteButtonApi={removeUserApi}/>
            <Modal
                title="用户修改"
                className="ant-modal-header"
                open={isModalOpen}
                onOk={() => closeModal}
                onCancel={() => closeModal}
                width={600}
                footer={[
                    <Button key='onOk' type="primary" onClick={closeModal}>确定</Button>,
                    <Button key='onCancel' onClick={closeModal}>取消</Button>
                ]}
                closeIcon={<IconFont type="i-close-circle" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    name="basic"
                    form={form}
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    // onFinish={onFinish}
                    // onFinishFailed={onFinishFailed}
                    autoComplete="off"
                >
                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label="真实姓名"
                                name="realName"
                                key="realName"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{required: true, message: '真实姓名未填写'}]}
                            >
                                <Input placeholder='请输入真实姓名'/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label="用户昵称"
                                name="nickname"
                                key="nickname"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{required: true}]}
                            >
                                <Input placeholder='请输入用户昵称'/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label="邮箱"
                                name="email"
                                key="email"
                                wrapperCol={{offset: 1}}
                                colon={false}
                            >
                                <Input placeholder='请输入邮箱地址'/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label="性别"
                                name="gender"
                                key="gender"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{required: true}]}
                            >
                                <Radio.Group>
                                    <Radio value="male">男</Radio>
                                    <Radio value="female">女</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>
        </>
    )
}

export default User;