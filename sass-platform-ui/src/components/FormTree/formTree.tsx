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

import React, {Key, useEffect, useRef, useState} from "react";
import {Checkbox, CheckboxProps, Space, Tree, TreeProps} from "antd";
import {AnyObject} from "antd/es/_util/type";
import './index.scss'
import {useTranslation} from "react-i18next";


export const FormTree = <T extends object>({formTreeProps, onSelectedAll}: {
    formTreeProps: TreeProps<T>,
    onSelectedAll?: (ids: string[]) => void
}) => {

    const {t} = useTranslation();

    const extractAllIds = (treeData?: T[], key?: string): string[] => {
        if (!treeData) {
            return []
        }
        const allIds: string[] = [];
        const extractIdsFromNode = (node: AnyObject): void => {
            // 获取当前节点的id并添加到allIds数组
            allIds.push(node[key ?? 'id']);
            // 如果当前节点有子节点，递归调用extractIdsFromNode处理子节点
            if (node.children && node.children.length > 0) {
                node.children.forEach((child: T) => {
                    extractIdsFromNode(child);
                });
            }
        };
        treeData.forEach((node) => {
            extractIdsFromNode(node);
        });
        return allIds;
    };
    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const ids: string[] = extractAllIds(formTreeProps.treeData, formTreeProps.fieldNames?.key)
    const [selectedAll, setSelectedAll] = useState<boolean>(false);
    const [expanded, setExpanded] = useState<boolean>(false);


    /**
     * 展开/折叠
     * @param e 事件
     */
    const onExpanded: CheckboxProps['onChange'] = (e) => {
        const value: boolean = e.target.checked;
        setExpanded(value);
        if (value) {
            setExpandedKeys(ids);
        } else {
            setExpandedKeys([]);
        }
    };
    const initValueRef = useRef(() => {
        const checkedKeys = formTreeProps.checkedKeys;
        if (checkedKeys && Array.isArray(checkedKeys)) {
            if (ids.length === checkedKeys.length) {
                setSelectedAll(true);
                setExpandedKeys(ids);
                setExpanded(true);
            }
        }
    });

    useEffect(() => {
        initValueRef.current();
    }, [])


    return (
        <div className="menu-list">
            <div>
                <Space>
                    <Checkbox checked={expanded} onChange={onExpanded}>{t('Common.expandOrCollapse')}</Checkbox>
                    <Checkbox checked={selectedAll} onChange={e => {
                        if (!onSelectedAll) {
                            return
                        }
                        setSelectedAll(e.target.checked)
                        if (e.target.checked) {
                            onSelectedAll(ids);
                        } else {
                            onSelectedAll([]);
                        }
                    }}>{t('Common.selectAllOrSelectNone')}</Checkbox>
                </Space>
            </div>
            <div className={'tree-info'}>
                <Tree<T>
                    className="menu-tree"
                    checkable
                    motion={false}
                    blockNode
                    expandedKeys={expandedKeys}
                    onExpand={(expandedKeysValue: Key[]) => {
                        setExpandedKeys(expandedKeysValue);
                    }}
                    {...formTreeProps}
                    onCheck={(checked, info) => {
                        if (!formTreeProps.onCheck) {
                            return
                        }
                        formTreeProps.onCheck(checked, info);
                        if ((checked as Key[]).length === ids.length) {
                            setSelectedAll(true)
                        } else {
                            setSelectedAll(false)
                        }
                    }}
                />
            </div>
        </div>
    )
}