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
import {Button, Col, Form, Input, message, Modal, Radio, Row, Select, Space, TableColumnsType} from "antd";
import {IconFont, Page} from "@/components";
import './index.scss'
import {Userinfo} from "@/model/user";
import {PASSWORD_REGEX, USERNAME_REGEX} from "@/constants/regexConstant";
import {userApi} from "@/apis/user";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {TenantInfo} from "@/model/tenant";
import {AddButton, DeleteButton} from "@components/Button/commonButton";

export const User: React.FC = () => {

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
            render: (_, record: Userinfo) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => openModal(record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    const [updateUserId, setUpdateUserId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [userQuery, setUserQuery] = useState<{ [key: string]: unknown }>({});

    /**
     * 打开模态组
     * @param userId 用户id
     */
    const openModal = (userId?: string) => {
        setIsModalOpen(true);
        setUpdateUserId(userId);
        if (!userId) {
            return;
        }
        userApi.getInfoByIdApi(userId!)
            .then((res: Userinfo) => {
                form.setFieldsValue({...res})
            })
            .catch((err: Error) => {
                message.error(err.message).then()
            })
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        form.resetFields();
    }

    /**
     * 处理用户表单
     */
    const handlerUserForm = async () => {
        await form.validateFields();
        setIsModalButtonLoading(true);
        try {
            await (updateUserId ? updateUserDetail() : saveUserDetail());
            message.success("操作成功").then()
            setIsModalOpen(false);
            form.resetFields();
        } catch (error: unknown) {
            message.error(error instanceof Error ? error.message : '未知错误').then()
        } finally {
            setIsModalButtonLoading(false);
        }
    }

    /**
     * 保存用户详情
     */
    const saveUserDetail: () => Promise<string> = () => {
        const userDetail = form.getFieldsValue();
        return userApi.saveInfoApi(userDetail)
    }

    /**
     * 修改用户详情
     */
    const updateUserDetail: () => Promise<void> = () => {
        const userinfo: Userinfo = form.getFieldsValue();
        return userApi.editInfoApi(userinfo.id!, userinfo);
    }

    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });

    /**
     * 分页查询结果
     */
    const [pageResult, setPageResult] = useState<PageResult<Userinfo>>();
    useEffect(() => {
        userApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<Userinfo>) => {
                setPageResult({...res});
            })
    }, [pageQuery])

    /**
     * 分页查询请求
     */
    const pageRequest = () => {
        userApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<TenantInfo>) => {
                setPageResult({...res})
            })
    }


    return (
        <>
            <Page
                tableProps={{
                    tableName: '用户列表',
                    columns: columns,
                    pageData: pageResult,
                    setPageQuery: setPageQuery,
                    setMultipleChooseRowKey: setRowKeys,
                    components: [
                        <>
                            <AddButton onClick={() => openModal()}/>
                            <DeleteButton onClick={async () => {
                                userApi.deleteInfoApi(rowKeys as string[]).then();
                                pageRequest()
                            }}/>
                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="username">用户名</label>
                            <Input placeholder={'请输入用户名'} id={'username'} onChange={(e) => {
                                setUserQuery({username: e.target.value})
                            }}/>
                        </>,
                        <>
                            <span>性别</span>
                            <Select
                                key={'gender'}
                                placeholder={'用户性别'}
                                onChange={(value) => userQuery['gender'] = value}
                                options={[
                                    {value: 'male', label: <span>男</span>},
                                    {value: 'female', label: <span>女</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...userQuery})
                }}
            />

            <Modal
                title={updateUserId ? "修改用户" : "新增用户"}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={600}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handlerUserForm}>确定</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>取消</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    name="basic"
                    form={form}
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    autoComplete="off"
                >
                    {!updateUserId &&
                        <Row gutter={24}>
                            <Col span={12}>
                                <Form.Item
                                    label="用户名"
                                    name="username"
                                    validateTrigger="onBlur"
                                    key="username"
                                    wrapperCol={{offset: 1}}
                                    colon={false}
                                    required={true}
                                    hasFeedback
                                    rules={[{
                                        required: true,
                                        type: "string",
                                        message: USERNAME_REGEX.message,
                                        max: 20,
                                    },
                                        () => ({
                                            validator: async (_, value: string) => {
                                                if (!USERNAME_REGEX.regex.test(value)) {
                                                    return Promise.reject(new Error("用户名格式不正确，必须以字母开头，并使用3到20个字符，仅包含字母、数字和下划线。"));
                                                }
                                                const exists: boolean = await userApi.checkUsernameExistsApi(value);
                                                if (exists) {
                                                    return Promise.reject(new Error('用户名已存在'));
                                                }

                                            }
                                        })
                                    ]}
                                >
                                    <Input placeholder='请输入用户名' maxLength={20}/>
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item
                                    label="密码"
                                    name="password"
                                    key="password"
                                    hasFeedback
                                    wrapperCol={{offset: 1}}
                                    colon={false}

                                    rules={[{
                                        required: true,
                                        pattern: PASSWORD_REGEX.regex,
                                        message: PASSWORD_REGEX.message,
                                    }]}
                                >
                                    <Input placeholder='请输入用户密码' type='password'/>
                                </Form.Item>
                            </Col>
                        </Row>}
                    <Row gutter={24}>
                        <Form.Item name="id" hidden>
                            <Input/>
                        </Form.Item>

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
                                rules={[{
                                    type: 'email',
                                    message: "请输入正确的邮箱账号"
                                }]}
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
                                initialValue={'male'}
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
