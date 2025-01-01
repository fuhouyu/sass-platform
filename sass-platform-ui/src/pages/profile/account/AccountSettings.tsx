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
import {Button, Card, Flex} from "antd";
import {IconFont} from "@/components";
import {useEffect, useState} from "react";
import {accountApi} from "@/apis/account.tsx";
import {Account, AccountType} from "@/model/account.tsx";
import {useTranslation} from "react-i18next";

/**
 * 账号设置
 * @constructor 构造函数
 */
export const AccountSettings = () => {

    const [accounts, setAccounts] = useState<Account[]>([]);
    const weLinkBind = accounts.find(account => account.accountType === AccountType.WELINK);
    const {t} = useTranslation()

    const getAccounts = async () => {
        setAccounts(await accountApi.getAccountForMe());
    }
    useEffect(() => {
        getAccounts().then();
    }, []);

    const unbind = async () => {
        await accountApi.unbindThirdPartyAccount(AccountType.WELINK, weLinkBind!.account);
        await getAccounts();
    }

    const bindAccount = () => {
        const width = 400; // 弹窗宽度
        const height = 500; // 弹窗高度
        const left = (window.screen.width - width) / 2; // 居中定位
        const top = (window.screen.height - height) / 2; // 居中定位
        const specs = `width=${width},height=${height},left=${left},top=${top},resizable=no,scrollbars=no`;

        const newWindow = window.open('/account-bind', '_blank', specs);

        // 定时检查窗口是否关闭
        const timer = setInterval(() => {
            if (newWindow && newWindow.closed) {
                clearInterval(timer);
                // 在这里处理窗口关闭后的逻辑，比如刷新页面或更新状态
                getAccounts().then();
            }
        }, 500);
    }

    return (
        <div className={'account-container'}>
            <Card title={t('Account.thirdPartyAccount')} bordered={false}>
                <ul>
                    <li>
                        <Flex justify={'space-between'} align={'center'}>
                            <div className={'account-left'}>
                                <IconFont type={'i-WeLink'} className={'account-icon'}/>
                                <div className={'text-block'}>
                                    <span className={'account-title'}>{t('Account.welink')}</span>
                                    {weLinkBind && <span
                                        className={'sub-title'}>{t('Account.alreadyBind')}：{weLinkBind.account}</span>}
                                </div>
                            </div>
                            <Button onClick={() =>
                                weLinkBind ? unbind() :
                                    bindAccount()
                            }
                                    icon={<IconFont type={weLinkBind ? 'i-jiechubangding' : 'i-bangdingpingtai'}/>}>
                                {weLinkBind ? t('Account.unbind') : t('Account.bind')}
                            </Button>
                        </Flex>
                    </li>
                </ul>
            </Card>
        </div>
    );
};