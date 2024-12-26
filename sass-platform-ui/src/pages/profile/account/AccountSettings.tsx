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
import {Button, Card, Flex, Modal} from "antd";
import {IconFont, WeLinkLogin} from "@/components";
import {useEffect, useState} from "react";
import {accountApi} from "@/apis/account.tsx";
import {Account, AccountType} from "@/model/account.tsx";

/**
 * 账号设置
 * @constructor 构造函数
 */
export const AccountSettings = () => {

    const [accounts, setAccounts] = useState<Account[]>([]);
    const weLinkBind = accounts.find(account => account.accountType === AccountType.WELINK);
    const [open, setOpen] = useState<boolean>(false);

    useEffect(() => {
        const getAccounts = async () => {
            setAccounts(await accountApi.getAccountForMe());
        }
        getAccounts().then();
    }, []);

    const unbind = async () => {
        await accountApi.unbindThirdPartyAccount(AccountType.WELINK, weLinkBind!.account);
    }

    return (
        <div className={'account-container'}>
            <Card title="第三方账号绑定" bordered={false}>
                <ul>
                    <li>
                        <Flex justify={'space-between'} align={'center'}>
                            <div className={'account-left'}>
                                <IconFont type={'i-WeLink'} className={'account-icon'}/>
                                <div className={'text-block'}>
                                    <span className={'account-title'}>WeLink 账号</span>
                                    {weLinkBind && <span className={'sub-title'}>已绑定：{weLinkBind.account}</span>}
                                </div>
                            </div>
                            <Button onClick={() =>
                                weLinkBind ? unbind() :
                                    setOpen(true)
                            }
                                    icon={<IconFont type={weLinkBind ? 'i-jiechubangding' : 'i-bangdingpingtai'}/>}>
                                {weLinkBind ? '取消绑定' : '绑定'}
                            </Button>
                        </Flex>
                    </li>
                </ul>
            </Card>

            <Modal
                title={'绑定第三方账号'}
                footer={[]}
                open={open}
                closable={false}
                onCancel={() => setOpen(false)}
            >
                <WeLinkLogin redirectType={'bind'}/>
            </Modal>
        </div>
    );
};