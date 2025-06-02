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


import {FC, useCallback, useEffect, useRef, useState} from "react";
import "./index.scss"
import {Avatar, Button, Divider, Flex, Form, Input, Select, Space} from "antd";
import {useNavigate, useSearchParams} from "react-router-dom";
import {IUserAuthentication} from "@/types/authentication";
import useAuth from "@/hooks/useAuth";
import {IconFont, WeLinkLogin} from "@/components";
import {useTranslation} from "react-i18next";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher";
import {Turnstile, TurnstileInstance} from "@marsidev/react-turnstile";
import {useUserStore} from "@/store";
import {ITenantInfo} from "@/types/tenant";
import {tenantApi} from "@/apis/tenant.ts";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";

import Icon, {MoonOutlined, SunOutlined} from "@ant-design/icons";
import {LoginSvg} from "@/views/login/components/LoginSvg.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {useThemeStore} from "@/store/modules/theme.ts";
import {useRoutes} from "@/hooks/useRoutes.tsx";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.ts";
import logo from '@/assets/images/logo.png'
import {AccountType} from "@/enums/accountType.ts";


/**
 * 登录组件
 * @constructor
 */
const Login: FC = () => {
  usePageTitle('Menu.login');
  const navigate = useNavigate();
  const turnstileRef = useRef<TurnstileInstance | null>(null);
  const [loginButtonLoading, setLoginButtonLoading] = useState<boolean>(false);
  const isAuth = useAuth();
  const {LanguageSwitcherButton} = useLanguageSwitcher('switch-language-button');
  const {t} = useTranslation();
  const [searchParams] = useSearchParams();
  const [weLinkQr, setWeLinkQr] = useState<boolean>(false);
  const [turnstileToken, setTurnstileToken] = useState<string | undefined>();
  const {fetchLogin} = useUserStore(state => state);
  const {preview} = useResourceAction();
  const [tenantList, setTenantList] = useState<ITenantInfo[]>([]);
  const [tenantId, setTenantId] = useState<string>();
  const {theme, changeTheme} = useThemeStore();
  const {updateDynamicRoutes} = useRoutes();
  const [themeIcon, setThemeIcon] = useState(
    theme === 'light' ? <MoonOutlined/> : <SunOutlined/>
  );

  const initTenant = useCallback(async () => {
    const tenantInfos = await tenantApi.list();
    setTenantList(tenantInfos);
    setTenantId(tenantInfos[0].id);
  }, [])

  // 如果本身存在token，跳转回首页
  useEffect(() => {
    if (isAuth) {
      navigate(BaseUrlConstant.HOME_URL);
      return
    }
    initTenant().then();
  }, [isAuth, navigate, initTenant]);


  const onFinish = async (loginData: IUserAuthentication) => {
    setLoginButtonLoading(true);
    loginData.accountType = AccountType.PASSWORD;
    loginData.cloudflareTurnstileToken = turnstileToken;
    try {
      await fetchLogin({...loginData, tenantId});
      updateDynamicRoutes().then(() => {
        const callback = searchParams.get('callback');
        if (callback) {
          window.location.replace(callback);
        } else {
          navigate('/');
        }
      });
    } finally {
      setLoginButtonLoading(false);
    }
  };

  return (
    <Flex className="container">
      <Flex className={'login-left'} vertical>
        <Flex className={'logo-container'} align={'center'}>
          <img
            width={42}
            height={42}
            src={logo} alt="logo"
            style={{mixBlendMode: 'multiply'}}
          />
          <p>Sass Platform</p>
        </Flex>
        <Icon
          className={'login-svg'}
          component={LoginSvg}
        />
      </Flex>

      <Flex className={'login-right'} vertical>
        <Flex justify={'flex-end'} align={'center'} className={'login-tools'}>
          {LanguageSwitcherButton}
          <Button
            type={'text'}
            onClick={() => {
              const newTheme = theme === 'light' ? 'dark' : 'light';
              changeTheme(newTheme);
              setThemeIcon(newTheme === 'light' ? <MoonOutlined/> : <SunOutlined/>);
            }}>{themeIcon}</Button>
        </Flex>
        <Flex flex={8} justify={'space-between'} align={'center'} vertical>
          <div className={'login-form-container'}>
            <div className={'login-title'}>
              <h1>{t('Login.title')}</h1>
              <span>{t('Login.tips')}</span>
            </div>
            <Select
              value={tenantId}
              prefix={<IconFont type={'i-zuhuguanli'}/>}
              className={'tenant-choose-container'}
              onSelect={(value: string) => setTenantId(value)}
              placeholder={t('Login.chooseTenantPlaceholder')}
              options={tenantList?.map(tenantInfo => {
                return {
                  value: tenantInfo.id,
                  label: <Space className={'tenant-choose'}>
                    <Avatar
                      icon={null}
                      src={preview(tenantInfo.icon)}/>{tenantInfo.tenantName}
                  </Space>
                };
              })}/>
            {weLinkQr ?
              <WeLinkLogin redirectType={'login'}/>
              :
              <Form
                name="login"
                initialValues={{
                  "identify": "admin",
                  "credentials": "admin",
                }}
                onFinish={onFinish}
              >
                <Form.Item
                  name="identify"
                  rules={[{required: true, message: t('Login.usernameEmptyMessage')}]}
                >
                  <Input prefix={<IconFont type={'i-zhanghao'}/>}
                         placeholder={t('Login.usernamePlaceholder')}/>
                </Form.Item>
                <Form.Item
                  name="credentials"
                  rules={[{required: true, message: t('Login.passwordEmptyMessage')}]}
                >
                  <Input.Password prefix={<IconFont type={'i-mima'}/>}

                                  placeholder={t('Login.passwordPlaceholder')}/>
                </Form.Item>
                {import.meta.env.VITE_CLOUDFLARE_SITE_KEY &&
                  <Form.Item className={'cloudflare-turnstile-container'}>
                    <label>
                      <span>{t('Login.cloudflareTurnstileVerify')}</span>
                    </label>
                    <Turnstile
                      ref={turnstileRef}
                      options={{
                        theme: theme as 'light' | 'dark' | 'auto',
                        size: 'flexible',
                      }}
                      siteKey={import.meta.env.VITE_CLOUDFLARE_SITE_KEY}
                      onSuccess={(token: string) => setTurnstileToken(token)}
                    />
                  </Form.Item>
                }

                <Form.Item className={'login-button-container'}>
                  <Button
                    disabled={import.meta.env.VITE_CLOUDFLARE_SITE_KEY && !turnstileToken}
                    block
                    type="primary" htmlType="submit" loading={loginButtonLoading}>
                    {t('Login.loginButton')}
                  </Button>
                </Form.Item>
              </Form>
            }
            <Divider className='other-login-divider'>
              <p>{t('Login.otherLogin')}</p>
            </Divider>
            <div className='other-login-methods'>
              {weLinkQr ? <Button icon={<IconFont type="i-zhanghao"/>}
                                  color="default"
                                  variant="link"
                                  className='other-login-method'
                                  onClick={() => {
                                    setWeLinkQr(false);
                                  }}>
                  <p>{t('Login.usernamePasswordLogin')}</p>
                </Button> :
                <Button icon={<IconFont type="i-WeLink"/>}
                        color="default"
                        variant="link"
                        className='other-login-method'
                        onClick={() => {
                          setWeLinkQr(true);
                        }}>

                  <p>{t('Login.weLinkLogin')}</p>
                </Button>
              }
            </div>
          </div>
          <footer className={'foot-copyright'}>
            <p>Copyright © 2024-{new Date().getFullYear()} <a
              href="https://github.com/fuhouyu">fuhouyu</a>.
            </p>
          </footer>
        </Flex>
      </Flex>
    </Flex>
  );
}


export default Login;
