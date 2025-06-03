# 简介

Sass Platform 后台管理系统

JDK 21

React 18

## 本地运行

### 安装依赖

```shell
git clone https://github.com/fuhouyu/base-framework.git
cd base-framework
mvn clean install -Dmaven.javadoc.skip=true -Dmaven.source.skip=true -DskipTests
```

### 后端服务

```shell
git clone -b develop https://github.com/fuhouyu/sass-platform.git
cd sass-platform
mvn clean package -Dmaven.javadoc.skip=true -Dmaven.source.skip=true -DskipTests -Plocal
java -jar sass-platform-admin/target/sass-platform-admin-1.0.0-SNAPSHOT.jar 
```

### 前端服务

```shell
cd sass-platform-ui
npm i
npm run dev
```