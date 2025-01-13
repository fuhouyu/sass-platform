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


import React, {Key, useCallback, useEffect, useState} from "react";
import {
    Button,
    Card,
    Col,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Row,
    Select,
    Space,
    TableColumnsType,
    Tag,
    Tree,
    TreeSelect
} from "antd";
import {IconFont, Modal, PageList, PermissionButton} from "@/components";
import './index.scss'
import {Userinfo} from "@/model/user";
import {userApi} from "@/apis/user";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {UserPermissionConstant} from "@/constants/permissionConstant.tsx";
import {DownOutlined} from "@ant-design/icons";
import {useOrganizationLazyData} from "@/hooks/useOrganizationLazyData.tsx";
import {Organization} from "@/model/organization.tsx";
import {organizationApi} from "@/apis/organization.tsx";
import {useDictItem} from "@/hooks/useDictItem.tsx";
import {AccountType} from "@/model/account.tsx";
import {useAppSelector} from "@/store";

export const User: React.FC = () => {
    const buttonPermissions = useButton(UserPermissionConstant.List);
    const {t} = useTranslation();
    const {findDictItemName} = useDictItem(['GENDER']);
    const columns: TableColumnsType = [
        {
            title: t('User.username'),
            align: 'center',
            dataIndex: 'username',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('User.realName'),
            align: 'center',
            dataIndex: 'realName',
            defaultSortOrder: 'descend',
        },
        {
            title: t('User.nickname'),
            align: 'center',
            dataIndex: 'nickname',
        },
        {
            title: t('User.gender'),
            align: 'center',
            dataIndex: 'gender',
            render: (_, record: Userinfo) => {
                return findDictItemName('GENDER', record.gender);
            }
        },
        {
            title: t('User.loginDate'),
            align: 'center',
            dataIndex: 'loginDate',
        },
        {
            title: t('User.loginIp'),
            align: 'center',
            dataIndex: 'loginIp',
        },
        {
            title: t('Position.name'),
            align: 'center',
            dataIndex: ['userPosition', 'positionName'],
        },
        {
            title: t('Position.isMain'),
            align: 'center',
            dataIndex: ['userPosition', 'isMain'],
            render: (isMain: boolean) => (
                isMain ?
                    <Tag bordered={false} color="success">
                        {t('Common.yes')}
                    </Tag>
                    :
                    <Tag bordered={false} color="error">
                        {t('Common.no')}
                    </Tag>
            )
        },
        {
            title: t('Common.updateAt'),
            align: 'center',
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            align: 'center',
            dataIndex: 'updateBy',
        },
        {
            title: '操作',
            align: 'center',
            dataIndex: 'action',
            render: (_, record: Userinfo) => {
                return (
                    <PermissionButton permissionStr={UserPermissionConstant.EDIT} buttonPermissions={buttonPermissions}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                )
            }
        }
    ];
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });
    const initForm: Userinfo = {
        gender: 'MALE',
        userPosition: {
            isMain: true,
        },
    }
    const [updateUserId, setUpdateUserId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [userQuery, setUserQuery] = useState<{ [key: string]: unknown }>({});
    const [formInitValues, setFormInitValues] = useState<Userinfo>(initForm);
    const {organizationLazyData, onLoadData} = useOrganizationLazyData();
    const [organizationTree, setOrganizationTree] = useState<Organization[]>();
    const [pageResult, setPageResult] = useState<PageResult<Userinfo>>();
    const language = useAppSelector(state => state.locale.language);


    /**
     * 打开模态组
     * @param userId 用户id
     */
    const openModal = async (userId?: string) => {
        setUpdateUserId(userId);
        setOrganizationTree(await organizationApi.getOrganizationTreeSelect());
        if (!userId) {
            setIsModalOpen(true);
            return;
        }
        const userinfo = await userApi.getInfoByIdApi(userId);
        setFormInitValues(userinfo);
        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        const organizationId = formInitValues.userPosition?.organizationId;
        setFormInitValues({...initForm, userPosition: {organizationId}});
        form.resetFields();
    }

    /**
     * 处理用户表单
     */
    const handlerUserForm = async () => {
        await form.validateFields();
        setIsModalButtonLoading(true);
        const userDetail: Userinfo = form.getFieldsValue();
        if (userDetail.account) {
            userDetail.account.accountType = AccountType.PASSWORD;
        }
        try {
            await (updateUserId ? userApi.editInfoApi(updateUserId, userDetail) : userApi.saveInfoApi(userDetail));
            message.success(t('Common.success')).then();
            await pageQueryCallback();
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }

    const pageQueryCallback = useCallback(async () => {
        setPageResult(await userApi.pageInfoListApi(pageQuery));
    }, [pageQuery]);

    useEffect(() => {
        pageQueryCallback().then();
    }, [pageQueryCallback])


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };


    return (
        <>
            <Row gutter={24} className={'main-container'}>
                <Col span={3} className={'tree-container'}>
                    <div className='tree-info'>
                        <Tree<Organization>
                            defaultExpandParent={true}
                            showLine
                            fieldNames={{key: 'id', title: 'organizationName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={organizationLazyData}
                            onSelect={(selectedKeys: Key[]) => {
                                if (!selectedKeys) {
                                    return
                                }
                                setFormInitValues({
                                    ...initForm,
                                    userPosition: {organizationId: selectedKeys[0] as string}
                                });
                                setPageQuery({...pageQuery, ...userQuery, organizationId: selectedKeys[0] as number});
                            }}
                        />
                    </div>
                </Col>
                <Col span={21}>
                    <PageList
                        tableProps={{
                            tableName: t('User.list'),
                            columns: columns,
                            pageData: pageResult,
                            pageQuery: pageQuery,
                            setPageQuery: setPageQuery,
                            rowSelection: rowSelection,
                            components: [
                                <>
                                    <PermissionButton permissionStr={UserPermissionConstant.ADD}
                                                      buttonPermissions={buttonPermissions}>
                                        <AddButton onClick={() => openModal()}/>
                                    </PermissionButton>
                                    <PermissionButton permissionStr={UserPermissionConstant.DELETE}
                                                      buttonPermissions={buttonPermissions}>
                                        <Popconfirm
                                            title={t('Button.delete')}
                                            description={t('Button.deleteConfirm')}
                                            okText={t('Common.yes')}
                                            cancelText={t('Common.no')}
                                            onConfirm={async () => {
                                                await userApi.deleteInfoApi(rowKeys as string[]);
                                                await pageQueryCallback();
                                            }}
                                        >
                                            <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>
                                        </Popconfirm>
                                    </PermissionButton>

                                </>
                            ]
                        }}
                        headerSearchProps={{
                            components: [
                                <><label htmlFor="username">{t('User.username')}</label>
                                    <Input placeholder={t('User.usernamePlaceholder')} id={'username'}
                                           onChange={(e) => {
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
                                            {value: 'MALE', label: <span>{t('User.male')}</span>},
                                            {value: 'FEMALE', label: <span>{t('User.female')}</span>}
                                        ]}
                                    />
                                </>
                            ],
                            onSearchClick: () => setPageQuery({...pageQuery, ...userQuery})
                        }}
                    />
                </Col>
            </Row>

            <Modal
                title={updateUserId ? t('User.edit') : t('User.add')}
                className="ant-modal-header"
                open={isModalOpen}
                destroyOnClose={true}
                width={750}
                onCancel={() => closeModal()}
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
                    clearOnDestroy={true}
                    name="modal-form"
                    form={form}
                    validateTrigger={'onBlur'}
                    labelCol={{span: 7}}
                    initialValues={formInitValues}
                >
                    <Space style={{width: '100%'}} wrap direction={'vertical'}>
                        <Card title={t('User.accountInfo')} size={'small'}>
                                <Row gutter={24}>
                                    <Col span={12}>
                                        <Form.Item
                                            label={t('User.username')}
                                            name="username"
                                            validateTrigger="onBlur"
                                            key="username"
                                            colon={false}
                                            required={true}
                                            hasFeedback
                                            rules={updateUserId ? [] : [{
                                                required: true,
                                                type: "string",
                                                message: t('User.usernamePlaceholder'),
                                                max: 20,
                                            },
                                                () => ({
                                                    validator: async (_, value: string) => {
                                                        const exists: boolean = await userApi.checkUsernameExistsApi(value);
                                                        if (exists) {
                                                            return Promise.reject(new Error(t('User.usernameExistsErrorMessage')));
                                                        }

                                                    }
                                                })
                                            ]}
                                        >
                                            <Input disabled={updateUserId != undefined}
                                                   placeholder={t('User.usernamePlaceholder')} maxLength={20}/>
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item
                                            label={updateUserId ? t('User.editPassword') : t('User.password')}
                                            name={['account', 'password']}
                                            key="password"
                                            hasFeedback
                                            colon={false}
                                            rules={updateUserId ? [] : [{
                                                required: true,
                                                message: t('User.passwordPlaceholder'),
                                            }]}
                                        >
                                            <Input.Password
                                                placeholder={t('User.passwordPlaceholder')}
                                            />
                                        </Form.Item>
                                    </Col>
                                </Row>
                            </Card>

                        <Card title={t('User.info')} size={'small'}>
                            <Row gutter={24}>
                                <Col span={12}>
                                    <Form.Item
                                        label={t('User.realName')}
                                        name="realName"
                                        key="realName"
                                        hasFeedback
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
                                        hasFeedback
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
                                        hasFeedback
                                        colon={false}
                                        rules={[{required: true}]}
                                    >
                                        <Radio.Group>
                                            <Radio value="MALE">{t('User.male')}</Radio>
                                            <Radio value="FEMALE">{t('User.female')}</Radio>
                                        </Radio.Group>
                                    </Form.Item>
                                </Col>
                            </Row>
                        </Card>

                        <Card title={t('User.positionInfo')} size={'small'}>
                            <Row gutter={24}>
                                <Col span={12}>
                                    <Form.Item
                                        label={t('Position.ownerOrganization')}
                                        name={['userPosition', 'organizationId']}
                                        key="organizationId"
                                        colon={false}
                                        hasFeedback
                                        labelCol={{span: language == 'zh' ? 7 : 12}}
                                        rules={[{required: true, message: t('Position.ownerOrganizationPlaceholder')}]}
                                    >
                                        <TreeSelect<Organization>
                                            disabled={formInitValues.userPosition?.organizationId != null}
                                            showSearch
                                            placeholder={t('Position.ownerOrganizationPlaceholder')}
                                            fieldNames={{label: 'organizationName', value: 'id'}}
                                            dropdownStyle={{maxHeight: 400, overflow: 'auto'}}
                                            allowClear
                                            treeDefaultExpandAll
                                            treeData={organizationTree}
                                        />
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item
                                        label={t('Position.name')}
                                        name={['userPosition', 'positionName']}
                                        key="positionName"
                                        labelCol={{span: language == 'zh' ? 7 : 8}}
                                        colon={false}
                                        hasFeedback
                                        rules={[{required: true, message: t('Position.namePlaceholder')}]}
                                    >
                                        <Input max={50} placeholder={t('Position.namePlaceholder')}/>
                                    </Form.Item>
                                </Col>
                            </Row>

                            <Row gutter={24}>
                                <Col span={12}>
                                    <Form.Item
                                        label={t('Position.orderInOrganization')}
                                        name={['userPosition', 'orderInOrganization']}
                                        key="orderInOrganization"
                                        colon={false}
                                        hasFeedback
                                        labelCol={{span: language == 'zh' ? 7 : 12}}
                                        rules={[{
                                            required: true,
                                            message: t('Position.orderInOrganizationPlaceholder')
                                        }]}
                                    >
                                        <InputNumber changeOnWheel
                                                     controls
                                                     style={{width: language == 'zh' ? 230 : 164}}
                                                     placeholder={t('Position.orderInOrganizationPlaceholder')}/>
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item
                                        label={t('Position.isMain')}
                                        name={['userPosition', 'isMain']}
                                        key={'isMain'}
                                        colon={false}
                                        hasFeedback
                                        labelCol={{span: language == 'zh' ? 7 : 8}}
                                        required
                                    >
                                        <Radio.Group>
                                            <Radio value={true}>{t('Common.yes')}</Radio>
                                            <Radio value={false}>{t('Common.no')}</Radio>
                                        </Radio.Group>
                                    </Form.Item>

                                </Col>
                            </Row>
                        </Card>
                    </Space>
                </Form>
            </Modal>
        </>
    );
}
