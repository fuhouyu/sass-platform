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

export default {
  root: true,
  // 继承某些已有的规则
  extends: [
    'stylelint-config-standard', // css 标准配置
    'stylelint-config-standard-scss', // scss 标准配置
    'stylelint-config-recess-order', // CSS 属性排序配置
  ],
  plugins: ['stylelint-order'],
  rules: {
    'no-descending-specificity': null,
    'no-empty-source': null,
    'selector-class-pattern': null,
    'property-no-unknown': null,
    'value-no-vendor-prefix': null,
    'property-no-vendor-prefix': null,
    'selector-pseudo-class-no-unknown': [
      true,
      {
        ignorePseudoClasses: ['global', 'export'],
      },
    ],
  },
  ignoreFiles: [
    '**/*.js',
    '**/*.jsx',
    '**/*.tsx',
    '**/*.ts',
    '**/*.json',
    '**/*.md',
    '**/*.yaml',
    '**/*.cjs',
  ],
};
