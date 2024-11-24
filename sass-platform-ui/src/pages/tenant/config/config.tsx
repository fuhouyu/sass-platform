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
    Form,
    GetProp,
    Input,
    message,
    Modal,
    TableColumnsType,
    Tag,
    Tree,
    TreeDataNode,
    TreeProps
} from "antd";
import {IconFont, PageList} from "@/components";
import {SearchInput} from "@components/List/pageParams";
import {getTenantConfigListApi, removerTenantConfigApi} from "@/apis/tenantConfig";
import {getUserinfoByIdApi} from "@/apis/user";
import {UserModel} from "@/model/user";
import {useMenuTree} from "@/hooks/useMenuTree";
import './index.scss'
import TextArea from "antd/es/input/TextArea";

const extractKeys = (trees: TreeDataNode[]): Key[] => {
    const keys: Key[] = []
    trees.forEach(tree => {
        keys.push(tree.key);
        if (tree.children) {
            keys.push(...extractKeys(tree.children))
        }
    });
    return keys;
}
export const Config: React.FC = () => {

    const columns: TableColumnsType = [
        {
            title: '配置名称',
            dataIndex: 'name',
            showSorterTooltip: {target: 'full-header'},
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
            // render: (_, _: TenantConfigModel) => {
            //     return (<>
            //         <Space size="middle" style={{whiteSpace: 'nowrap'}}>
            //             <a onClick={() => {
            //             }}>修改</a>
            //         </Space>
            //     </>)
            // }
        }
    ];

    const [isModalOpen, setIsModalOpen] = useState<boolean>(true);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [isAdded, setIsAdded] = useState<boolean>(true);
    const [form] = Form.useForm();

    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const [allMenuKeys, setAllMenuKeys] = useState<React.Key[]>([]);
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
    const menuTree = useMenuTree<TreeDataNode>();
    const onExpand: TreeProps['onExpand'] = (expandedKeysValue) => {
        console.log('onExpand', expandedKeysValue);
        setExpandedKeys(expandedKeysValue);
        setAutoExpandParent(false);
    };

    const menusOptions = [
        {label: '展开/折叠', value: 'expanded'},
        {label: '全选/全不选', value: 'selectAll'},
    ];

    const onMenusOptions: GetProp<typeof Checkbox.Group, 'onChange'> = (checkedValues) => {
        console.log('checked = ', checkedValues);
        if (checkedValues.length == 0) {
            setExpandedKeys([]);
            setCheckedKeys([]);
            return
        }
        checkedValues.map(value => {
            switch (value) {
                case 'expanded':
                    setExpandedKeys(allMenuKeys);
                    break;
                case 'selectAll':
                    setCheckedKeys(allMenuKeys);
                    break

            }
        })

    };

    const onCheck: TreeProps['onCheck'] = (checkedKeysValue) => {
        console.log('onCheck', checkedKeysValue);
        setCheckedKeys(checkedKeysValue as React.Key[]);
    };

    const onSelect: TreeProps['onSelect'] = (selectedKeysValue, info) => {
        console.log('onSelect', info);
        setSelectedKeys(selectedKeysValue);
    };

    useEffect(() => {
        setAllMenuKeys(extractKeys(menuTree));
    }, [menuTree]);

    /**
     * 打开模态组
     * @param userId 用户id
     * @param isAdd 是否添加用户
     */
    const openModal = (userId?: string,
                       isAdd?: boolean) => {
        setIsModalOpen(true);
        if (isAdd) {
            setIsAdded(isAdd)
            return;
        }
        getUserinfoByIdApi(userId!)
            .then((res: UserModel) => {
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
        // form.resetFields();
    }


    return (<>
        <PageList
            // ref={pageListRef}
            searchComments={[
                {
                    name: '配置名称',
                    key: 'keyword',
                    comment: SearchInput,
                    placeholder: '配置名称',
                }
            ]}
            listName='租户配置'
            columns={columns}
            pageRequestApi={getTenantConfigListApi}
            addCallback={() => {
                openModal(undefined, true)
            }}
            deleteCallback={(ids: string[]) => removerTenantConfigApi(ids)}
        />

        <Modal
            title={isAdded ? "新增配置" : "修改配置"}
            className="ant-modal-header"
            open={isModalOpen}
            onCancel={() => closeModal()}
            width={600}
            footer={[
                <Button key='onOk' type="primary" loading={isModalButtonLoading}
                    /*onClick={handlerUserForm}*/>确定</Button>,
                <Button key='onCancel' onClick={() => closeModal()}>取消</Button>
            ]}
            closeIcon={<IconFont type="i-Close" style={{
                fontSize: '24px',
            }}/>}
        >
            <Form
                name="basic"
                form={form}
                style={{maxWidth: 600}}
                autoComplete="off"
            >
                <Form.Item
                    label="配置名称"
                    name="name"
                    validateTrigger="onBlur"
                    key="name"
                    labelCol={{span: 4}}
                    wrapperCol={{span: 20}}
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: '请输入${label}'}]}
                >
                    <Input placeholder='请输入配置名称' maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label="权限列表"
                    name="permissionList"
                    key="permissionList"
                    labelCol={{span: 4}}
                    wrapperCol={{span: 20}}
                    colon={false}
                    required={true}
                    hasFeedback
                >
                    <div className="menu-list">
                        <Checkbox.Group options={menusOptions} onChange={onMenusOptions}/>
                        <Tree
                            className="menu-tree"
                            checkable
                            onExpand={onExpand}
                            expandedKeys={expandedKeys}
                            autoExpandParent={autoExpandParent}
                            onCheck={onCheck}
                            checkedKeys={checkedKeys}
                            onSelect={onSelect}
                            selectedKeys={selectedKeys}
                            treeData={menuTree}
                        />
                    </div>
                </Form.Item>
                <Form.Item
                    label="备注"
                    name="remark"
                    key="remark"
                    labelCol={{span: 4}}
                    wrapperCol={{span: 20}}
                    colon={false}
                    hasFeedback
                >
                    <TextArea className="remark" showCount maxLength={500}/>
                </Form.Item>
            </Form>
        </Modal>
    </>)
}