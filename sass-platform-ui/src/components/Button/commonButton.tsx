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

import {Button} from "antd";
import {IconFont} from "@/components";
import {ButtonProps} from "@components/Button/interface";
import './index.scss'
import {useTranslation} from "react-i18next";


/**
 * 添加按钮
 * @param buttonProps 按钮属性
 * @constructor 构造函数
 */
export const AddButton = (buttonProps: ButtonProps) => {
    const {t} = useTranslation();

    return (
        <Button className="add-button"
                onClick={buttonProps.onClick}
                icon={<IconFont type="i-add"/>}
        >
            {t('Button.add')}
        </Button>
    )
}

/**
 * 删除按钮
 * @param buttonProps 按钮属性
 * @constructor 构造函数
 */
export const DeleteButton = (buttonProps: ButtonProps) => {
    const {t} = useTranslation();
    return (
        <Button className="del-button"
                onClick={buttonProps.onClick}
                icon={<IconFont type="i-delete"/>}>
            {t('Button.delete')}
        </Button>
    )
}