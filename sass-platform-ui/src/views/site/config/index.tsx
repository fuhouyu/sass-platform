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

import {FC, useEffect, useState} from "react";
import {Button, Form, Input, message} from "antd";
import {siteConfigApi} from "@/apis/siteConfig.ts";
import {ISiteConfig} from "@/types/site-config";
import {AnyObject} from "antd/es/_util/type";
import {S3Upload} from "@/components";
import {useResourceAction} from "@/hooks/useResourceAction";
import {UploadFileStatus} from "antd/lib/upload/interface";

const SiteConfig: FC = () => {
  const [form] = Form.useForm();
  const {preview} = useResourceAction();

  const [iconFiles, setIconFiles] = useState<{
    uid: string,
    name: string,
    status?: UploadFileStatus,
    url?: string
  }[] | undefined>(undefined);


  useEffect(() => {
    // 获取初始配置
    siteConfigApi.getSiteConfig().then((res) => {
      form.setFieldsValue(res);
    });
  }, [form]);


  const handleSubmit = async (values: any) => {
    // await saveSiteConfig(values);
    message.success("保存成功", values);
  };

  return (
    <div style={{margin: "0 auto", padding: 24, background: "#fff", borderRadius: 8}}>
      <Form<ISiteConfig>
        form={form}
        onFinish={handleSubmit}
        labelCol={{span: 2,}}
        clearOnDestroy={true}
        autoComplete="off"
      >
        <Form.Item label="网站名称" name="siteName"
                   rules={[{required: true, message: "请输入网站名称"}]}>
          <Input/>
        </Form.Item>
        <Form.Item label="网站描述" name="siteDescription">
          <Input.TextArea rows={5}/>
        </Form.Item>

        <Form.Item
          label={'logo'}
          validateTrigger="onBlur"
          colon={false}
          required={true}
          hasFeedback
          name='logo'
          valuePropName={'fileList'}
          getValueProps={(resourceId) => {
            return {
              uid: resourceId,
              name: 'logo.png',
              status: 'done',
              url: preview(resourceId),
            };
          }}
          validateFirst={true}
          rules={[{
            required: true,
            type: "object",
            message: '请选择Logo',
            validator: async (_, resourceId: AnyObject) => {
              return resourceId === null || resourceId === undefined;
            }
          }]}
        >
          <S3Upload
            onRemove={_ => setIconFiles(undefined)}
            prefix={'application-icon'}
            isPublic
            fileList={iconFiles}
            accept={'image/*'}
            maxCount={1}
            onUploadSuccess={async (resourceId) => {
              setIconFiles([{
                uid: resourceId,
                name: 'icon.png',
                status: 'done',
                url: preview(resourceId),
              }]);
            }}
            listType="picture-card">
            {iconFiles && null}
          </S3Upload>
        </Form.Item>

        <Form.Item label="联系邮箱" name="contactEmail">
          <Input/>
        </Form.Item>

        <Form.Item label="联系电话" name="contactPhone">
          <Input/>
        </Form.Item>

        <Form.Item label="联系地址" name="contactAddress">
          <Input/>
        </Form.Item>

        <Form.Item label="ICP备案号" name="icpNumber">
          <Input/>
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">保存设置</Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default SiteConfig;

