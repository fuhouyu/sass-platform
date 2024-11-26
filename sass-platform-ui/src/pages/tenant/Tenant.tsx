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


import React, {Key, useRef, useState} from "react";
import {Button, Checkbox, Form, GetProp, Input, message, Modal, Radio, Space, TableColumnsType, Tag, Tree} from "antd";
import {TenantInfo} from "@/model/tenant";
import {IconFont, PageList} from "@/components";
import {PageListHandler, SearchInput} from "@components/List/pageParams";
import {MenuTreeType, useMenuTree} from "@/hooks/useMenuTree";
import {Menu} from "@/model/menu";
import {useAppSelector} from "@/store";
import TextArea from "antd/es/input/TextArea";
import './index.scss'
import {userApi} from "@/apis/user";
import {tenantApi} from "@/apis/tenant";

/**
 * 转换映射关系
 * @param trees tree数据
 * @param idKeyMap idKeyMap的映射对象
 */
const parseIdKeyMap = (trees: MenuTreeType[], idKeyMap: Map<Key, string>) => {
    trees.forEach(tree => {
        idKeyMap.set(tree.key, tree.id);
        if (tree.children) {
            parseIdKeyMap(tree.children, idKeyMap);
        }
    });
}

/**
 * 解析修改时已选中的菜单项
 * @param trees 树
 * @param ids id集合
 */
const parseMenuKey = (trees: MenuTreeType[], ids?: Key[]): Key[] => {
    if (!ids || ids.length === 0) {
        return [];
    }
    const keys: Key[] = []
    trees.forEach(tree => {
        ids.forEach(id => {
            if (id === tree.id) {
                keys.push(tree.key)
            }
        })
        if (tree.children) {
            keys.push(...parseMenuKey(tree.children, ids));
        }
    });

    return keys;
}

const Tenant: React.FC = () => {

    const columns: TableColumnsType = [
        {
            title: '租户编码',
            dataIndex: 'tenantCode',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: '租户名称',
            dataIndex: 'tenantName',
            defaultSortOrder: 'descend',
        },
        {
            title: '租户类型',
            dataIndex: 'tenantType',
        },
        {
            title: '联系人',
            dataIndex: 'contactPerson',
        },
        {
            title: '联系方式',
            dataIndex: 'contactInfo',
        },
        {
            title: '是否启用',
            dataIndex: 'isEnabled',
            align: 'center',
            render: (isEnabled: boolean) => (
                isEnabled ? <Tag color={"#E8F4FF"} style={{border: "1px solid blue"}}>
                        <span style={{color: '#2090FF'}}>启用</span>
                    </Tag> :
                    <Tag color={"#FFEDED"} style={{border: "1px solid #FFB6B6"}}>
                        <span style={{color: '#FF9696'}}>禁用</span>
                    </Tag>
            )
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
            render: (_, tenantConfig: TenantInfo) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => {
                            openModal(tenantConfig.id)
                        }}>修改</a>
                    </Space>
                </>)
            }
        }
    ];


    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const pageListRef = useRef<PageListHandler>();
    const [updateId, setUpdateId] = useState<string>();
    const [form] = Form.useForm();
    const [isEnabled, setIsEnabled] = useState<boolean>(true);
    const userMenus: Menu[] = useAppSelector((state) => state.user.userMenus);
    const menuTree = useMenuTree(userMenus);

    // key 和 id映射
    const idKeyMap = new Map<Key, string>();
    parseIdKeyMap(menuTree, idKeyMap);

    /**
     * 权限树相关
     */
    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);

    const [selectMenusOptions, setSelectMenusOptions] = useState<string[]>([]);
    const menusOptions = [
        {label: '展开/折叠', value: 'expanded'},
        {label: '全选/全不选', value: 'selectAll'},
    ];

    const onMenusOptions: GetProp<typeof Checkbox.Group, 'onChange'> = (checkedValues) => {
        if (checkedValues.length == 0) {
            setExpandedKeys([]);
            setCheckedKeys([]);
            setSelectMenusOptions([])
            return;
        }
        setSelectMenusOptions(checkedValues as string[]);
        checkedValues.map(value => {
            switch (value) {
                case 'expanded':
                    setExpandedKeys(Array.from(idKeyMap.keys()));
                    break;
                case 'selectAll':
                    setCheckedKeys(Array.from(idKeyMap.keys()));
                    break
            }
        })

    };


    /**
     * 打开模态组
     * @param tenantId 租户id
     * @param isAdd 是否添加用户
     */
    const openModal = (tenantId?: string) => {
        setIsModalOpen(true);
        setUpdateId(tenantId);
        if (!tenantId) {
            return
        }
        // 修改获取租户数据，先获取详情
        userApi.getInfoByIdApi(tenantId!)
            .then((res: TenantInfo) => {
                form.setFieldsValue({...res})
                if (res.permissionIds) {
                    setCheckedKeys(parseMenuKey(menuTree, res.permissionIds))
                }
            })
            .catch((err: Error) => {
                message.error(err.message).then()
            });
    }


    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        cleanFormValues();
    }

    /**
     * 清除form表单中的值
     */
    const cleanFormValues = () => {
        setExpandedKeys([]);
        setSelectMenusOptions([]);
        form.resetFields();
        setCheckedKeys([]);
    }

    /**
     * 处理租户租户
     * @param value 租户租户
     */
    const handleTenantConfig = (value: TenantInfo) => {
        setIsModalButtonLoading(true);
        const ids: string[] = [];
        checkedKeys.forEach(checkedKey => {
            const id = idKeyMap.get(checkedKey);
            if (id) ids.push(id);
        });
        value.permissionIds = ids;
        const promise = updateId ? tenantApi.editInfoApi(updateId, value) : tenantApi.saveInfoApi(value);
        promise.then(() => {
            setIsModalOpen(false);
            cleanFormValues();
            message.success("操作成功").then();
            pageListRef.current?.refresh();
        })
            .catch((error: Error) => {
                message.error(error.message).then();
            })
            .finally(() => {
                setIsModalButtonLoading(false);
            })
    }

    return (<>
        <PageList
            ref={pageListRef}
            searchComments={[
                {
                    name: '租户名称',
                    key: 'tenantName',
                    comment: SearchInput,
                    placeholder: '租户名称',
                }
            ]}
            listName='租户'
            columns={columns}
            pageRequestApi={tenantApi.pageInfoListApi}
            addCallback={() => {
                openModal()
            }}
            deleteCallback={(ids: string[]) => tenantApi.deleteInfoApi(ids)}
        />

        <Modal
            title={updateId ? "修改租户" : "新增租户"}
            className="ant-modal-header"
            open={isModalOpen}
            onCancel={() => closeModal()}
            footer={[]}
            width={600}
            closeIcon={<IconFont type="i-Close" style={{
                fontSize: '1.5rem',
            }}/>}
            destroyOnClose
        >
            <Form
                name="basic"
                form={form}
                labelCol={{span: 4}}
                autoComplete="off"
                onFinish={handleTenantConfig}
                initialValues={{isEnabled: isEnabled}}
            >
                <Form.Item
                    label="租户名称"
                    name="tenantName"
                    validateTrigger="onBlur"
                    key="tenantName"
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: '请输入${label}'}]}
                >
                    <Input placeholder='请输入租户名称' maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label="租户编码"
                    name="tenantCode"
                    validateTrigger="onBlur"
                    key="tenantCode"
                    colon={false}
                    required={true}
                    hasFeedback={updateId === null}
                    rules={[{required: true, message: '请输入${label}'}]}
                >
                    <Input disabled={updateId != null} placeholder='请输入租户编码' maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label="租户权限"
                    key="permissionList"
                    colon={false}
                    required={true}
                >
                    <div className="menu-list">
                        <Checkbox.Group value={selectMenusOptions} options={menusOptions} onChange={onMenusOptions}/>
                        <Tree
                            className="menu-tree"
                            checkable
                            onExpand={(expandedKeysValue: Key[]) => {
                                setExpandedKeys(expandedKeysValue);
                                setAutoExpandParent(false);
                            }}
                            expandedKeys={expandedKeys}
                            autoExpandParent={autoExpandParent}
                            onCheck={(checkedKeysValue) => setCheckedKeys(checkedKeysValue as React.Key[])}
                            checkedKeys={checkedKeys}
                            onSelect={(selectedKeysValue: Key[]) => setSelectedKeys(selectedKeysValue)}
                            selectedKeys={selectedKeys}
                            treeData={menuTree}
                        />
                    </div>
                </Form.Item>
                <Form.Item
                    label="联系人"
                    name="contactPerson"
                    validateTrigger="onBlur"
                    key="contactPerson"
                    colon={false}
                    required={true}

                    hasFeedback
                    rules={[{required: true, message: '请输入${label}'}]}
                >
                    <Input placeholder='请输入联系人' maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label="联系方式"
                    name="contactInfo"
                    validateTrigger="onBlur"
                    key="contactInfo"
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: '请输入${label}'}]}
                >
                    <Input placeholder='请输入联系方式' maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label="状态"
                    name="isEnabled"
                    key="isEnabled"
                    colon={false}
                    hasFeedback
                >
                    <Radio.Group onChange={(e) => {
                        setIsEnabled(e.target.value);
                    }}>
                        <Radio value={true}>启用</Radio>
                        <Radio value={false}>禁用</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    label="备注"
                    name="remark"
                    key="remark"
                    colon={false}
                >
                    <TextArea className="remark" showCount maxLength={500}/>
                </Form.Item>
                <Form.Item
                    className="form-button"
                >
                    <Space
                        size="middle"
                    >
                        <Button
                            type="primary"
                            htmlType="submit"
                            loading={isModalButtonLoading}>确定</Button>
                        <Button key='onCancel' onClick={() => closeModal()}>取消</Button>
                    </Space>
                </Form.Item>
            </Form>
        </Modal>
    </>)
}

export default Tenant;