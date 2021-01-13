## ios_super_sign

###### 超级签名 免签封装 安卓打包 企业签名 自助分发多合一系统

### 主要功能

1. 基本操作

   - 修改密码
   - 用户信息
   - 共有池
   - 退出

2. 超级签名

   - 应用列表

     - 应用信息

     - 安卓合并
     - 简介编辑
     - 轮播图编辑
     - 分发地址复制
     - 下载码启动
     - 下载码购买地址
     - 删除

   - 上传应用

     - 拖动ipa上传

   - 下载密码

     - 下载码生成
     - 下载码信息显示
     - 删除

3. 证书管理

   - 证书列表
     - 证书信息
     - 剩余设备数
     - 删除
   - 上传证书
     - p8模式证书上传

4. 用户管理

   - 用户列表
     - 用户信息
     - 添加共有池

5. 下载管理

   - 下载记录
   - 对应设备签名的证书下载

6. 免签封装

   - 打包支持自定义信息
     - 应用名称
     - 打包网址
     - 描述文件名称
     - 描述文件机构
     - 描述文件描述信息
     - 描述文件统一信息
     - 应用图标
     - 启动图(仅支持安卓)
     - 苹果图标是否可删除
     - 安卓动态网址
     - 安卓包名
     - 版本
     - 自定义描述文件绿标证书
   - 打包记录
     - 分发地址
     - 分发源码下载地址

7. 企业签名

   - 开始签名
     - 开始签名
     - 编辑签名证书所需共有池
     - 删除
     - 证书备注修改
   - 签名记录
     - 签名记录信息
     - 签名后的ipa包下载
   - 上传证书

8. 自助分发

   - 开始分发
   - 分发记录
     - 分发记录显示
     - 安卓合并更新
     - 苹果更新
     - 简介编辑
     - 分发地址复制
     - 删除

### 架构

---

- 后端 spring boot
- 前端 vue
- 数据库 mysql
- redis
- 其他 redis python openssl androidsdk python unzip java8 java11 zsign

### 部署

---



**准备**

​		*由于该项目环境制作十分复杂,所以使用docker来部署,使用到的都是编译后的jar包和静态文件*

 		1.centos7 服务器必须是干净的 配置的话没什么要求太低会卡 推荐阿里云 阿里云可以使用内网oss分发很   快 如果使用其他服务器只能使用七牛云分发或者自己服务器的带宽
 		2.域名一个 并申请ssl证书 拿到tomcat证书也就是jks后缀 必须是ca认证的 解析到服务器上
		 3.下载发布包

**安装**

 1. 解压发布包,修改sign/public/js/app.63d3a6a1.js 搜索a.defaults.baseURL 修改成你的域名

 2. 修改application-sign.xml 的domain为你的域名,key-password为你的ssl证书密码 其他选项请自行根据注释修改

 3. 修改你的jks文件名,改名为cert.jks,并复制到sign/mode目录下

 4. 将sign目录复制到根目录 application-sign.yml 和jar包复制到/opt目录

 5. 下面请执行以下命令

    - systemctl stop firewalld.service

    - systemctl disable firewalld.service

    - yum update -y  && chmod -R 777 /sign && yum install docker -y && systemctl start docker

    - docker run -v /opt:/opt  -v /var/lib/mysql/:/var/lib/mysql/  -v /sign:/sign -p 80:80 -p 3306:3306 -p 443:443 -tdi --privileged    --name sign -d  --restart always 2524931333/centos7xjm:expect  init -t (这里使用的是阿里云docker镜像需要先登录 具体怎么登录请百度)
    - docker exec -it sign /bin/bash
    - sh /root/mysqlinit.sh
    - systemctl start mysqld
    - mysql -uroot -pMysql666..
    - create database ios_super_sign;
    - use ios_super_sign;
    - set names utf8mb4;
    - source /sign/mode/ios_super_sign.sql;
    - insert into user() values(null,super,super,now(),1,0);
    - quit;
    - screen -S sign
    - cd  /opt
    - java -jar -Djava.security.egd=file:/dev/./urandom ios-super-sign-0.0.1-SNAPSHOT.jar
    - 启动后请按两次ctrl+a+d 退出screen和退出docker

    

启动后访问域名即可管理员账号密码均为super

### 说明

---

本项目前后端一体,所以没有好看的ui,有需要的可以自行克隆二开,开源不易如果对你有帮助的话请点个start吧！

演示地址：https://iosign.wlznsb.cn/iosign 账号密码均为admin

