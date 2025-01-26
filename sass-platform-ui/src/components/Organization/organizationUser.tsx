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

import {useOrganizationLazyData} from "@/hooks/useOrganizationLazyData.tsx";
import {Col, Row, Tree} from "antd";
import {DownOutlined} from "@ant-design/icons";
import React, {Key} from "react";
import {Organization as OrganizationModal} from "@/model/organization.tsx";
import {PageList} from "@/components";
import {PageListProps} from "@components/List/page/interface.tsx";
import {EventDataNode} from "antd/es/tree";

declare type OrganizationUserProps = {
    onSelectTree: (selectedKeys: Key[], info: {
        event: 'select';
        selected: boolean;
        node: EventDataNode<OrganizationModal>;
        selectedNodes: OrganizationModal[];
        nativeEvent: MouseEvent;
    }) => void
} & PageListProps;

/**
 * 用户组织组件
 * @constructor 构造函数
 */
export const OrganizationUser = (userPageListProps: OrganizationUserProps) => {
    const {onLoadData, organizationLazyData} = useOrganizationLazyData();
    const {tableProps, headerSearchProps, setPageQuery, onSelectTree} = userPageListProps;

    return (
        <Row gutter={24} className={'main-container'}>
            <Col span={3} className={'tree-container'}>
                <div className='tree-info'>
                    <Tree
                        defaultExpandParent={true}
                        showLine
                        blockNode
                        motion={false}
                        fieldNames={{key: 'id', title: 'organizationName'}}
                        switcherIcon={<DownOutlined/>}
                        loadData={onLoadData}
                        treeData={organizationLazyData}
                        onSelect={onSelectTree}
                    />
                </div>
            </Col>
            <Col span={21}>
                <PageList
                    tableProps={tableProps}
                    headerSearchProps={headerSearchProps}
                    setPageQuery={setPageQuery}
                />
            </Col>
        </Row>
    )
}