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

import {FieldNames} from "rc-tree/lib/interface";
import * as React from "react";
import {AnyObject} from "antd/es/_util/type";

/**
 * 表单树属性
 */
export interface FormTreeProps<T = AnyObject> {
    /**
     * 树数据
     */
    treeData: T[];

    /**
     * fieldNames
     */
    fieldNames: FieldNames;

    /**
     * titleRender
     * @param node node节点
     */
    titleRender?: (node: T) => React.ReactNode;
}