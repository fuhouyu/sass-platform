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
import './index.scss'
import {Button, Col, Form, Input, message, Modal, Radio, Row, Space, TableColumnsType, Tag} from "antd";
import {Role as RoleModel} from "@/model/role";
import {roleApi} from "@/apis/role";
import {PageQuery, PageResult} from "@/model/pageQuery";
import type {TableRowSelection} from "antd/es/table/interface";
import {IconFont, PageList} from "@/components";
import {AddButton, DeleteButton} from "@components/Button/commonButton";
import {PASSWORD_REGEX, USERNAME_REGEX} from "@/constants/regexConstant";
import {useTranslation} from "react-i18next";

export const Role: React.FC = () => {
    const {t} = useTranslation();

    const columns: TableColumnsType = [
        {
            title: t('Role.name'),
            dataIndex: 'roleName',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('Role.code'),
            dataIndex: 'roleCode',
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (isEnabled: boolean) => (
                isEnabled ? <Tag color={"#E8F4FF"} style={{border: "1px solid blue"}}>
                        <span style={{color: '#2090FF'}}>{t('Common.enabled')}</span>
                    </Tag> :
                    <Tag color={"#FFEDED"} style={{border: "1px solid #FFB6B6"}}>
                        <span style={{color: '#FF9696'}}>{t('Common.disabled')}</span>
                    </Tag>
            )
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
        {
            title: t('Common.action'),
            dataIndex: 'action',
            render: (_, record: RoleModel) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => openModal(record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    const [updateId, setUpdateUserId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [roleQuery, setUserQuery] = useState<{ [key: string]: unknown }>({});

    /**
     * 打开模态组
     * @param roleId 角色id
     */
    const openModal = async (roleId?: string) => {
        setUpdateUserId(roleId);
        if (roleId) {
            const roleInfo: RoleModel = await roleApi.getInfoByIdApi(roleId);
            form.setFieldsValue({...roleInfo})
        }
        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        form.resetFields();
    }

    /**
     * 处理角色表单
     */
    const handlerUserForm = async () => {
        await form.validateFields();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? updateUserDetail() : saveUserDetail());
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
     * 保存角色详情
     */
    const saveUserDetail: () => Promise<string> = () => {
        const roleDetail = form.getFieldsValue();
        return roleApi.saveInfoApi(roleDetail)
    }

    /**
     * 修改角色详情
     */
    const updateUserDetail: () => Promise<void> = () => {
        const roleInfo: RoleModel = form.getFieldsValue();
        return roleApi.editInfoApi(roleInfo.id!, roleInfo);
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
    const [pageResult, setPageResult] = useState<PageResult<RoleModel>>();
    useEffect(() => {
        roleApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<RoleModel>) => {
                setPageResult({...res});
            })
    }, [pageQuery])

    /**
     * 分页查询请求
     */
    const pageRequest = () => {
        roleApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<RoleModel>) => {
                setPageResult({...res})
            })
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<RoleModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableName: t('Role.list'),
                    columns: columns,
                    pageData: pageResult,
                    setPageQuery: setPageQuery,
                    rowSelection: rowSelection,
                    components: [
                        <>
                            <AddButton onClick={() => openModal()}/>
                            <DeleteButton onClick={async () => {
                                roleApi.deleteInfoApi(rowKeys as string[]).then();
                                pageRequest()
                            }}/>
                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="roleCode">{t('Role.code')}</label>
                            <Input placeholder={t('Role.codePlaceholder')} id={'roleCode'} onChange={(e) => {
                                setUserQuery({roleName: e.target.value})
                            }}/>
                        </>,
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...roleQuery})
                }}
            />

            <Modal
                title={updateId ? t('Role.add') : t('Role.edit')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={600}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handlerUserForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
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
                    {!updateId &&
                        <Row gutter={24}>
                            <Col span={12}>
                                <Form.Item
                                    label="角色名"
                                    name="rolename"
                                    validateTrigger="onBlur"
                                    key="rolename"
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
                                                    return Promise.reject(new Error("角色名格式不正确，必须以字母开头，并使用3到20个字符，仅包含字母、数字和下划线。"));
                                                }
                                                const exists: boolean = await roleApi.checkRoleCodeExists(value);
                                                if (exists) {
                                                    return Promise.reject(new Error('角色名已存在'));
                                                }

                                            }
                                        })
                                    ]}
                                >
                                    <Input placeholder='请输入角色名' maxLength={20}/>
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
                                    <Input placeholder='请输入角色密码' type='password'/>
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
                                label="角色昵称"
                                name="nickname"
                                key="nickname"
                                wrapperCol={{offset: 1}}
                                colon={false}
                            >
                                <Input placeholder='请输入角色昵称'/>
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
