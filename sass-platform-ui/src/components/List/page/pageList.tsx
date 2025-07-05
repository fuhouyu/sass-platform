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
import SearchHeader from '@components/List/header/searchHeader';
import { PageListProps } from '@components/List/page/interface';
import Table from '@components/List/table/table';
import { Flex } from 'antd';

const PageList = <T extends object>(props: PageListProps<T>) => {
    const { headerSearchProps, tableProps } = props;
    return (
        <Flex vertical style={{
          height: '100%'
        }}>
            {headerSearchProps && <SearchHeader {...headerSearchProps} />}
            <Table<T> className={'page-table-container '} {...tableProps} />
        </Flex>
    );
};

export default PageList;
