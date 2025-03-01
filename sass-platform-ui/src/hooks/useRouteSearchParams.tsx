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

import {useSearchParams} from "react-router-dom";

const useRouteSearchParams = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    /**
     * 更新路径参数
     * @param newParams 新的路径参数值
     */
    const updateSearchParams = (newParams: Record<string, string | number | boolean | undefined | null>) => {

        const updatedParams = new URLSearchParams(searchParams);

        // 遍历 newParams，更新或删除参数
        Object.entries(newParams).forEach(([key, value]) => {
            if (value === null || value === undefined || value === "") {
                updatedParams.delete(key); // 如果值为 null，则删除该参数
            } else {
                updatedParams.set(key, String(value)); // 否则更新参数
            }
        });
        setSearchParams(updatedParams);
    };

    /**
     * 获取查询的参数对象
     */
    const querySearchParams = () => {
        return Object.fromEntries(searchParams.entries());
    }

    return {
        querySearchParams,
        updateSearchParams,

    }
};

export default useRouteSearchParams;