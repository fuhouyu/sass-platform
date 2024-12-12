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

import React, {Key, useState} from "react";
import {Checkbox, CheckboxProps, Space, Tree} from "antd";
import {FormTreeProps} from "@components/FormTree/interface";
import {AnyObject} from "antd/es/_util/type";
import './index.scss'

export const FormTree = <T extends object>(formTreeProps: FormTreeProps<T>) => {

    const {treeData, titleRender, fieldNames} = formTreeProps;

    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
    const ids = treeData.map(item => (item as AnyObject)[fieldNames.key!]);

    /**
     * 展开/折叠
     * @param e 事件
     */
    const onExpanded: CheckboxProps['onChange'] = (e) => {
        if (e.target.checked) {
            setExpandedKeys(ids);
        } else {
            setExpandedKeys([]);
        }
    };

    /**
     * 全选/全不选
     * @param e 事件
     */
    const onSelectedAll: CheckboxProps['onChange'] = (e) => {
        console.log(ids)
        if (e.target.checked) {
            setCheckedKeys(ids);
        } else {
            setCheckedKeys([]);
        }
    }

    return (
        <>
            <div className="menu-list">
                <div>
                    <Space>
                        <Checkbox onChange={onExpanded}>展开/折叠</Checkbox>
                        <Checkbox onChange={onSelectedAll}>全选/全不选</Checkbox>
                    </Space>
                </div>
                <Tree<T>
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
                    fieldNames={fieldNames}
                    titleRender={titleRender}
                    onSelect={(selectedKeysValue: Key[]) => setSelectedKeys(selectedKeysValue)}
                    selectedKeys={selectedKeys}
                    treeData={treeData}
                />
            </div>
        </>
    )
}