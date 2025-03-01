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


import {PageQuery, PageResult} from "@/model/pageQuery.tsx";
import {useCallback, useState} from "react";
import {useSearchParams} from "react-router-dom";

const initPageQuery: PageQuery = {
    pageNum: 1,
    pageSize: 10
}

export function usePageList<T>(pageQueryApi: (pageQuery: PageQuery) => Promise<PageResult<T>>) {

    const [pageDataList, setPageDataList] = useState<PageResult<T>>({} as PageResult<T>);
    const [searchParams] = useSearchParams();

    /**
     * 刷新页面
     */
    const refreshPageList = useCallback(async (dataCallback?: (pageData: PageResult<T>) => void) => {
        const currentParams = Object.fromEntries(searchParams.entries());
        const mergedParams = {...initPageQuery, ...currentParams};
        const res = await pageQueryApi(mergedParams);
        dataCallback?.(res);
        setPageDataList(res)
    }, [pageQueryApi, searchParams]);


    return {
        pageDataList,
        refreshPageList
    }
}