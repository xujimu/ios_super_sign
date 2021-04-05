环境java 11 java 8 unzip zip centos7 openssl androidsdk  mysql5.7 python
使用方法:sign移动到linux根目录,/sign/mode/static/mode/android/temp 
执行里面的shell.sh
/sign/mode/cert 更换成你自己的apache的ssl证书,文件名要一样
/sign/mode/zsign 权限设置777
/sign/public 里面的js网址设置成自己的
cert.jsk换成你的tomcat ssl证书
更改yml文件的一些参数
新建数据库ios_super_sign,导入sql文件

更换域名,替换cert.jks,清空mode  cert证书
systemctl stop firewalld.service
systemctl disable firewalld.service
yum update -y  && chmod -R 777 /sign && yum install docker -y && systemctl start docker
systemctl status docker
//如果要在docker外使用数据库要注意映射
docker run -v /opt:/opt  -v /sign:/sign  -p 8080:8080 -tdi --privileged    --name sign -d  --restart always 2524931333/centos7xjm:expect  init -t
docker exec -it sign /bin/bash
sh /root/mysqlinit.sh
systemctl start mysqld
mysql -uroot -pMysql666..
create database ios_super_sign;
use ios_super_sign;
set names utf8mb4;
source /sign/mode/ios_super_sign.sql;
insert into user() values(null,super,super,now(),1,0);
quit;
screen -S sign
cd  /opt
java -jar -Djava.security.egd=file:/dev/./urandom ios-super-sign-0.0.1-SNAPSHOT.jar
