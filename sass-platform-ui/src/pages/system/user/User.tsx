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
import {Button, Col, Form, Input, message, Modal, Radio, Row, Select, TableColumnsType} from "antd";
import {IconFont, PageList} from "@/components";
import './index.scss'
import {Userinfo} from "@/model/user";
import {PASSWORD_REGEX, USERNAME_REGEX} from "@/constants/regexConstant";
import {userApi} from "@/apis/user";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {useTranslation} from "react-i18next";

export const User: React.FC = () => {

    const {t} = useTranslation();
    const columns: TableColumnsType = [
        {
            title: t('User.username'),
            dataIndex: 'username',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('User.realName'),
            dataIndex: 'realName',
            defaultSortOrder: 'descend',
        },
        {
            title: t('User.nickname'),
            dataIndex: 'nickname',
        },
        {
            title: t('User.gender'),
            dataIndex: 'gender',
        },
        {
            title: t('User.loginDate'),
            dataIndex: 'loginDate',
        },
        {
            title: t('User.loginIp'),
            dataIndex: 'loginIp',
        },
        {
            title: t('Common.createAt'),
            dataIndex: 'createAt',
            sorter: true,
            showSorterTooltip: false
        },
        {
            title: t('Common.createBy'),
            dataIndex: 'createBy'
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
            title: '操作',
            dataIndex: 'action',
            render: (_, record: Userinfo) => {
                return (<EditButton onClick={() => openModal(record.id)}/>)
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
    const openModal = async (userId?: string) => {
        setIsModalOpen(true);
        setUpdateUserId(userId);
        if (!userId) {
            return;
        }
        const userinfo = await userApi.getInfoByIdApi(userId)
        form.setFieldsValue({...userinfo})
        setUpdateUserId(userId);

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
            message.success(t('Common.success')).then();
            setIsModalOpen(false);
            form.resetFields();
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
    const pageRequest = async () => {
        setPageResult(await userApi.pageInfoListApi(pageQuery))
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableName: t('User.title'),
                    columns: columns,
                    pageData: pageResult,
                    setPageQuery: setPageQuery,
                    rowSelection: rowSelection,
                    components: [
                        <>
                            <AddButton onClick={() => openModal()}/>
                            <DeleteButton onClick={async () => {
                                userApi.deleteInfoApi(rowKeys as string[]).then();
                                await pageRequest()
                            }}/>
                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="username">{t('User.username')}</label>
                            <Input placeholder={t('User.usernamePlaceholder')} id={'username'} onChange={(e) => {
                                setUserQuery({username: e.target.value})
                            }}/>
                        </>,
                        <>
                            <span>{t('User.gender')}</span>
                            <Select
                                key={'gender'}
                                placeholder={t('User.genderPlaceholder')}
                                onChange={(value) => userQuery['gender'] = value}
                                options={[
                                    {value: 'male', label: <span>{t('User.male')}</span>},
                                    {value: 'female', label: <span>{t('User.female')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...userQuery})
                }}
            />

            <Modal
                title={updateUserId ? t('User.edit') : t('User.add')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={600}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handlerUserForm}>{t('Button.confirm')}</Button>,
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
                    {!updateUserId &&
                        <Row gutter={24}>
                            <Col span={12}>
                                <Form.Item
                                    label={t('User.username')}
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
                                                    return Promise.reject(new Error(t('User.usernameExistsErrorMessage')));
                                                }

                                            }
                                        })
                                    ]}
                                >
                                    <Input placeholder={t('User.usernamePlaceholder')} maxLength={20}/>
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item
                                    label={t('User.password')}
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
                                    <Input placeholder={t('User.passwordPlaceholder')} type='password'/>
                                </Form.Item>
                            </Col>
                        </Row>}
                    <Row gutter={24}>
                        <Form.Item name="id" hidden>
                            <Input/>
                        </Form.Item>

                        <Col span={12}>
                            <Form.Item
                                label={t('User.realName')}
                                name="realName"
                                key="realName"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{required: true, message: t('User.realNamePlaceholder')}]}
                            >
                                <Input placeholder={t('User.realNamePlaceholder')}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label={t('User.nickname')}
                                name="nickname"
                                key="nickname"
                                wrapperCol={{offset: 1}}
                                colon={false}
                            >
                                <Input placeholder={t('User.realNamePlaceholder')}/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label={t('User.email')}
                                name="email"
                                key="email"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{
                                    type: 'email',
                                    message: t('User.emailCheckMessage')
                                }]}
                            >
                                <Input placeholder={t('User.emailPlaceholder')}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label={t('User.gender')}
                                name="gender"
                                key="gender"
                                wrapperCol={{offset: 1}}
                                colon={false}
                                rules={[{required: true}]}
                                initialValue={'male'}
                            >
                                <Radio.Group>
                                    <Radio value="male">{t('User.male')}</Radio>
                                    <Radio value="female">{t('User.female')}</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Col>
                    </Row>

                </Form>
            </Modal>
        </>
    )
}
