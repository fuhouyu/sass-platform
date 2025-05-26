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

import {Button, Input, Splitter, TableColumnsType, Tag, Tree} from "antd";
import {userApi} from "@/apis/adminUser.ts";
import {Key, useEffect, useRef, useState} from "react";
import {useTranslation} from "react-i18next";
import {IUserinfo} from "@/types/user";
import {IPageQuery} from "@/types/pageQuery";
import {useOrganizationLazyData} from "@/hooks/useOrganizationLazyData.tsx";
import {OrganizationUserModalProps} from "@components/Organization/interface.tsx";
import './index.scss'
import {Modal, PageList} from "@/components";
import {IOrganization} from "@/types/organization";
import {TableRefType} from "@components/List/table/interface.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";

export const OrganizationUserModal = (organizationUserProps: OrganizationUserModalProps) => {

    const {t} = useTranslation();
    const {initOrganization, onLoadData, organizationLazyData} = useOrganizationLazyData();
    const {isModalOpen, setIsModalOpen, rowSelection} = organizationUserProps;
  const tableRef = useRef<TableRefType<IUserinfo>>(null);
    const columns: TableColumnsType = [
        {
            title: t('User.username'),
            align: 'center',
            dataIndex: 'username',
            width: 120,
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('User.realName'),
            align: 'center',
            dataIndex: 'realName',
            width: 120,
            defaultSortOrder: 'descend',
        },
        {
            title: t('Position.name'),
            align: 'center',
            width: 120,
            dataIndex: ['userPosition', 'positionName'],
        },
        {
            title: t('Position.isMain'),
            align: 'center',
            width: 120,
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
    const {querySearchParams} = useRouteSearchParams();
    const params = querySearchParams();
  const [pageQuery, setPageQuery] = useState<IPageQuery>({
        pageNum: 1,
        pageSize: 10,
        organizationId: params.organizationId
    });


    useEffect(() => {
        if (isModalOpen) {
            initOrganization().then();
        }
      return () => setPageQuery({} as IPageQuery)
    }, [initOrganization, isModalOpen])

    /**
     * 关闭组织用户modal
     */
    const closeOrganizationUserModal = () => {
        setIsModalOpen(false);
        rowSelection?.onChange?.([], [], {type: "all"})
    }


    return (
        <Modal
            title={t('Organization.chooseMember')}
            open={isModalOpen}
            destroyOnClose
            width={'60%'}
            onCancel={closeOrganizationUserModal}
            styles={{
                body: {
                    height: '40vh'
                }
            }}
            footer={[
                <Button key='onOrganizationUserAddOk' type="primary" onClick={() => {
                    closeOrganizationUserModal();
                }}
                >{t('Button.confirm')}</Button>,
                <Button key='onOrganizationUserAddCancel'
                        onClick={() => closeOrganizationUserModal()}>{t('Button.cancel')}</Button>
            ]}
        >

            <Splitter style={{height: '100%'}}>
                <Splitter.Panel className={'tree-container organization-user-modal'} defaultSize="20%" min="20%"
                                max="70%">
                    <div className='tree-info'>

                      <Tree<IOrganization>
                            defaultExpandParent={true}
                            blockNode
                            showIcon={false}
                            motion={false}
                            fieldNames={{key: 'id', title: 'organizationName'}}
                            loadData={onLoadData}
                            defaultSelectedKeys={[params.organizationId]}
                            treeData={organizationLazyData}
                            onSelect={(selectedKeys: Key[]) => {
                                let organizationId = undefined;
                                if (selectedKeys.length > 0) {
                                    organizationId = selectedKeys[0].toLocaleString();
                                }
                                setPageQuery({...pageQuery, organizationId})
                                tableRef.current?.refreshPageList({pageQuery: {...pageQuery, organizationId}});
                            }}
                        />
                    </div>
                </Splitter.Panel>
                <Splitter.Panel className={'organization-user-info'}>
                    <PageList
                        tableProps={{
                            disableTableHint: true,
                            tableRef: tableRef,
                            tableName: t('User.list'),
                            columns: columns,
                            pageApi: userApi.pageInfoListApi,
                            rowSelection: rowSelection,
                        }}
                        headerSearchProps={{
                            components: [
                                <><label htmlFor="username">{t('User.username')}</label>
                                    <Input
                                        allowClear
                                        placeholder={t('User.usernamePlaceholder')} id={'username'}
                                        onChange={(e) => {
                                            setPageQuery({...pageQuery, username: e.target.value})
                                        }}/>
                                </>,
                            ],
                            onSearchClick: () => {
                                tableRef.current?.refreshPageList({
                                    pageQuery: {...pageQuery}
                                })
                            }
                        }}
                    />
                </Splitter.Panel>
            </Splitter>
        </Modal>
    )
}
