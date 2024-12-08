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
import {
    Button,
    Checkbox,
    CheckboxProps,
    Form,
    Input,
    message,
    Modal,
    Radio,
    Space,
    TableColumnsType,
    Tag,
    Tree
} from "antd";
import {TenantInfo} from "@/model/tenant";
import {IconFont, PageList} from "@/components";
import {MenuTreeType, useMenuTree} from "@/hooks/useMenuTree";
import {Menu} from "@/model/menu";
import {useAppSelector} from "@/store";
import TextArea from "antd/es/input/TextArea";
import './index.scss'
import {userApi} from "@/apis/user";
import {tenantApi} from "@/apis/tenant";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user";

/**
 * 转换映射关系
 * @param trees tree数据
 * @param idKeyMap idKeyMap的映射对象
 */
const parseIdKeyMap = (trees: MenuTreeType[], idKeyMap: Map<Key, string>) => {
    trees.forEach(tree => {
        idKeyMap.set(tree.key, tree.id!);
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

/**
 * 租户组件
 * @constructor
 */
export const Tenant: React.FC = () => {

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


    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [updateId, setUpdateId] = useState<string>();
    const [form] = Form.useForm();
    const [isEnabled, setIsEnabled] = useState<boolean>(true);
    const userMenus: Menu[] = useAppSelector((state) => state.user.userMenus);
    const menuTree = useMenuTree(userMenus);
    const [tenantQuery, setTenantQuery] = useState<{ [key: string]: unknown }>({});

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
    const [pageResult, setPageResult] = useState<PageResult<TenantInfo>>();
    useEffect(() => {
        tenantApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<TenantInfo>) => {
                setPageResult({...res});
            })
    }, [pageQuery])

    /**
     * 分页查询请求
     */
    const pageRequest = () => {
        tenantApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<TenantInfo>) => {
                setPageResult({...res})
            })
    }


    /**
     * 展开/折叠
     * @param e 事件
     */
    const onExpanded: CheckboxProps['onChange'] = (e) => {
        if (e.target.checked) {
            setExpandedKeys(Array.from(idKeyMap.keys()));
        } else {
            setExpandedKeys([]);
        }
    };

    /**
     * 全选/全不选
     * @param e 事件
     */
    const onSelectedAll: CheckboxProps['onChange'] = (e) => {
        if (e.target.checked) {
            setCheckedKeys(Array.from(idKeyMap.keys()));
        } else {
            setCheckedKeys([]);
        }
    }

    /**
     * 打开模态组
     * @param tenantId 租户id
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
        setCheckedKeys([]);
        form.resetFields();
    }

    /**
     * 处理租户
     * @param value 租户
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
            pageRequest()
        })
            .catch((error: Error) => {
                message.error(error.message).then();
            })
            .finally(() => {
                setIsModalButtonLoading(false);
            })
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (<>
        <PageList
            tableProps={{
                tableName: '租户列表',
                columns: columns,
                pageData: pageResult,
                setPageQuery: setPageQuery,
                rowSelection: rowSelection,
                components: [
                    <>
                        <AddButton onClick={() => openModal()}/>
                        <DeleteButton onClick={async () => {
                            tenantApi.deleteInfoApi(rowKeys as string[]).then();
                            pageRequest()
                        }}/>
                    </>
                ]
            }}
            headerSearchProps={{
                components: [
                    <><label htmlFor="tenantName">租户名称</label>
                        <Input placeholder={'请输入租户名称'} id={'tenantName'} onChange={(e) => {
                            setTenantQuery({tenantName: e.target.value})
                        }}/>
                    </>
                ],
                onSearchClick: () => setPageQuery({...pageQuery, ...tenantQuery})
            }}
        />

        <Modal
            title={updateId ? "修改租户" : "新增租户"}
            open={isModalOpen}
            onCancel={() => closeModal()}
            footer={[]}
            closeIcon={<IconFont type="i-Close" style={{
                fontSize: '1.5rem',
            }}/>}
            destroyOnClose
        >
            <Form
                name="basic"
                form={form}
                autoComplete="off"
                labelCol={{span: 5}}
                style={{width: 600}}
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
                        <div>
                            <Space>
                                <Checkbox onChange={onExpanded}>展开/折叠</Checkbox>
                                <Checkbox onChange={onSelectedAll}>全选/全不选</Checkbox>
                            </Space>
                        </div>
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
