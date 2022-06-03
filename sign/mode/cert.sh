#!/usr/bin/expect -f 
#依次输入 jks路径  jks密码 p12输出路径 个人证书输出路径 ca证书输出路径 秘钥输出路径 

set jksPath [lindex $argv 0]  
set password [lindex $argv 1]
set p12Path [lindex $argv 2]
set myCertPath [lindex $argv 3]
set caCertPath [lindex $argv 4]
set keyPath [lindex $argv 5]

spawn keytool -importkeystore -srckeystore $jksPath -srcstoretype JKS -destkeystore $p12Path -deststoretype PKCS12 -srcstorepass $password -deststorepass $password 
expect "Enter Import Password:*" {send "$password\r"}


spawn openssl pkcs12 -in $p12Path -nokeys -clcerts -out $myCertPath
expect "Enter Import Password:*" {send "$password\r"}

spawn openssl pkcs12 -in $p12Path -nokeys -cacerts -out $caCertPath
expect "Enter Import Password:*" {send "$password\r"}

spawn openssl pkcs12 -in $p12Path -nocerts -nodes -out $keyPath
expect "Enter Import Password:*" {send "$password\r"}
 
interact
