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
import './index.scss'

/**
 * 账号类型
 */
interface AccountType {
    key: string;
    accountType: string;
    bindTime: string;
    status: boolean;
}

/**
 * 账号设置
 * @constructor 构造函数
 */
export const AccountSettings = () => {
    //
    // const columns: TableProps<AccountType>['columns'] = [
    //     {
    //         title: '',
    //         dataIndex: 'name',
    //         key: 'name',
    //         render: (text) => <a>{text}</a>,
    //     },
    //     {
    //         title: 'Age',
    //         dataIndex: 'age',
    //         key: 'age',
    //     },
    //     {
    //         title: 'Address',
    //         dataIndex: 'address',
    //         key: 'address',
    //     },
    //     {
    //         title: 'Tags',
    //         key: 'tags',
    //         dataIndex: 'tags',
    //         render: (_, { tags }) => (
    //             <>
    //                 {tags.map((tag) => {
    //                     let color = tag.length > 5 ? 'geekblue' : 'green';
    //                     if (tag === 'loser') {
    //                         color = 'volcano';
    //                     }
    //                     return (
    //                         <Tag color={color} key={tag}>
    //                             {tag.toUpperCase()}
    //                         </Tag>
    //                     );
    //                 })}
    //             </>
    //         ),
    //     },
    //     {
    //         title: 'Action',
    //         key: 'action',
    //         render: (_, record) => (
    //             <Space size="middle">
    //                 <a>Invite {record.name}</a>
    //                 <a>Delete</a>
    //             </Space>
    //         ),
    //     },
    // ];

    return (
        <div className={'account-container'}>
            <h4 className={'account-title'}>第三方账号绑定</h4>
            <p>绑定账号后，你可以直接使用第三方账号进行登录</p>
            {/*<Table<AccountType> columns={columns} dataSource={data} />*/}
        </div>
    );
};