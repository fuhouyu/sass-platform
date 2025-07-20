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

export interface Passkeys {

  /**
   * 通行密钥名称
   */
  passkeyName: string;

  /**
   * 通行密钥id
   */
  passkeyId: string;

  /**
   * 最后使用时间
   */
  lastUseTime: string;

  /**
   * 创建时间
   */
  createdAt: string;
}
