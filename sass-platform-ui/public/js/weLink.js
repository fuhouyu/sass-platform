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
!function(e,n){function t(e){let o,i=e.baseUrl?e.baseUrl:"https://login.welink.huaweicloud.com",r=n.createElement("iframe");t=i+"/sso-proxy-front/public/qrcode/0.0.1/qrcode.html?redirect_uri="+encodeURIComponent(e.redirect_uri)+"&client_id="+e.client_id,t+="&response_type="+(e.response_type?e.response_type:"code"),t+="&scope="+(e.scope?e.scope:"snsapi_login"),t+=e.state?"&state="+e.state:"",t+=e.style?"&style="+encodeURIComponent(e.style):"",t+="&self_redirect="+(e.self_redirect?e.self_redirect:"false"),t+="&lang="+(e.lang?e.lang:"cn"),t+=e.nameCN?"&nameCN="+encodeURIComponent(e.nameCN):"",t+=e.nameEN?"&nameEN="+encodeURIComponent(e.nameEN):"",t+=e.isHideName?"&isHideName=true":"",r.src=t,r.frameBorder="0",r.scrolling="no",r.allowTransparency="true",r.height=e.height?e.height+"px":"400px",r.width=e.width?e.width+"px":"365px",o=n.getElementById(e.id),o.innerHTML="",o.appendChild(r)}e.wlQrcodeLogin=t}(window,document);