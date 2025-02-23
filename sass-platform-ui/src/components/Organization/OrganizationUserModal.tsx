/*
 * Copyright 2024-2025 fuhouyu.
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

import {Button, Col, Input, Row, Table, TableColumnsType, Tag, Tree} from "antd";
import {userApi} from "@/apis/user.tsx";
import React, {Key, useCallback, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {Userinfo} from "@/model/user.tsx";
import {PageQuery, PageResult} from "@/model/pageQuery.tsx";
import {useOrganizationLazyData} from "@/hooks/useOrganizationLazyData.tsx";
import {DownOutlined} from "@ant-design/icons";
import {OrganizationUserModalProps} from "@components/Organization/interface.tsx";
import './index.scss'
import {Modal} from "@/components";

export const OrganizationUserModal = (organizationUserProps: OrganizationUserModalProps) => {

    const {t} = useTranslation();
    const {onLoadData, organizationLazyData} = useOrganizationLazyData();
    const {isModalOpen, setIsModalOpen, rowSelection} = organizationUserProps;
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
    ];
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });
    const [pageResult, setPageResult] = useState<PageResult<Userinfo>>();

    const pageQueryCallback = useCallback(async () => {
        setPageResult(await userApi.pageInfoListApi(pageQuery));
    }, [pageQuery]);

    useEffect(() => {
        pageQueryCallback().then();
    }, [pageQueryCallback])

    /**
     * 关闭组织用户modal
     */
    const closeOrganizationUserModal = () => {
        setIsModalOpen(false);
        rowSelection?.onChange?.([], [], {type: "all"})
        setPageResult(undefined);
    }


    return (
        <Modal
            title={t('Organization.chooseMember')}
            open={isModalOpen}
            destroyOnClose
            width={900}
            styles={{body: {height: '44.5vh'}}}
            onCancel={closeOrganizationUserModal}
            footer={[
                <Button key='onOrganizationUserAddOk' type="primary" onClick={() => {
                    closeOrganizationUserModal();
                }}
                >{t('Button.confirm')}</Button>,
                <Button key='onOrganizationUserAddCancel'
                        onClick={() => closeOrganizationUserModal()}>{t('Button.cancel')}</Button>
            ]}
        >
            <Row gutter={24} className={'main-container'}>
                <Col span={6} className={'tree-container organization-tree'}>
                    <div className='tree-info'>
                        <Tree.DirectoryTree
                            defaultExpandParent={true}
                            blockNode
                            showIcon={false}
                            motion={false}
                            fieldNames={{key: 'id', title: 'organizationName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={organizationLazyData}
                            onSelect={(selectedKeys: Key[]) => {
                                if (!selectedKeys) {
                                    return
                                }
                                setPageQuery({...pageQuery, organizationId: selectedKeys[0] as number});
                            }}
                        />
                    </div>
                </Col>
                <Col span={18}>
                    <div className={'organization-user-search'}>
                        <label htmlFor="username">{t('User.username')}</label>
                        <Input placeholder={t('User.usernamePlaceholder')} id={'username'}
                               onChange={(e) => {
                                   setPageQuery({...pageQuery, username: e.target.value})
                               }}/>
                    </div>
                    <Table<Userinfo>
                        rowKey={'id'}
                        bordered
                        rowSelection={{...rowSelection, type: 'radio', columnWidth: 48}}
                        columns={columns}
                        dataSource={pageResult?.list}
                        pagination={{
                            total: pageResult?.total,
                            hideOnSinglePage: false,
                            showSizeChanger: true,
                            defaultPageSize: pageResult?.pageSize ?? 10,
                        }}
                    />
                </Col>
            </Row>
        </Modal>
    )
}