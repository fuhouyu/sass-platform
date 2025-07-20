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

import './index.scss'
import {Button, Descriptions, message, Popconfirm, Space, Table, TableColumnsType} from 'antd';
import {useNavigate} from "react-router-dom";
import {Passkeys} from "@/types/passkeys";
import {useEffect, useState} from "react";
import {passkeyApi} from "@/apis/passkey.ts";
import {DeleteButton} from "@components/Button/commonButton.tsx";
import {useTranslation} from "react-i18next";


export const Passkey = () => {
  const {t} = useTranslation();
  const columns: TableColumnsType<Passkeys> = [
    {
      title: '通行密钥名称',
      dataIndex: 'passkeyName',
      align: 'center',
      key: 'passkeyName',
    },
    {
      title: '通行密钥 ID',
      dataIndex: 'passkeyId',
      align: 'center',
      key: 'passkeyId',
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      align: 'center',
      key: 'createdAt',
    },
    {
      title: '上次使用时间',
      dataIndex: 'lastUseTime',
      align: 'center',
      key: 'lastUseTime',
    },
    {
      title: '操作',
      key: 'action',
      align: 'center',
      render: (_, record: Passkeys) => (
        <Popconfirm
          title={t('Button.delete')}
          description={t('Button.deleteConfirm')}
          okText={t('Common.yes')}
          cancelText={t('Common.no')}
          onConfirm={async () => {
            await passkeyApi.deleteByPasskeyIdApi(record.passkeyId);
            message.success("删除成功");
            setPasskeyListData(await passkeyApi.passkeyListApi());
          }}
        >
          <DeleteButton
          />
        </Popconfirm>
      ),
    },
  ];

  const navigate = useNavigate();
  const [passkeyListData, setPasskeyListData] = useState<Passkeys[]>([]);

  useEffect(() => {
    passkeyApi.passkeyListApi().then(res => setPasskeyListData(res));
  }, []);
  return (
    <>
      <Descriptions
        title={
          <Space direction="vertical" size={0}>
            <h1 style={{marginBottom: 0}}>通行密钥</h1>
            <p className={'passkey-describe'}>
              通行密钥是一种基于 FIDO 标准的认证凭证，允许您使用设备支持的指纹识别、人脸识别和安全密钥等认证方式进行登录和多因素认证。
            </p>
          </Space>
        }
        column={1}
        bordered={false}
      />


      <Button type="primary"
              onClick={() => {
                navigate('/passkeys/add');
              }}
      >创建通行密钥</Button>
      <Table<Passkeys>
        style={{
          marginTop: '1rem'
        }}
        columns={columns}
        dataSource={passkeyListData}
        pagination={false}
        bordered
      />
    </>
  );
}
