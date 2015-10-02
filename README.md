Godzilla

====================

Godzilla是一个基于svn、maven的项目配置自动化管理工具。它以最终简洁方便开发人员或测试人员管理项目为主旨，在此基础上构建起来。<br>
Features:<br>
- 支持svn分支合并主干，提交主干
- 支持maven打war包与下载war包
- 支持基于maven的部署
- 支持pom配置项后台管理

## 使用说明 ##
1. 找相关人员 注册帐号，并赋予项目权限（目前可使用项目为apollo,cupid,fso_newmanager,fso-lark,godzilla,hera,message-center,nuggets-server,venus,zeus-server，其他项目请找 管理员设置）

2. 查看客户端服务器是否以配置godzilla帐号，是否安装jdk，svn，maven，是否创建godzilla工作目录/home/godzilla/gzl（查看是否具有client、rpc、work子目录）

3. 启动客户端: 
 
  <pre>
  
  ```java
  java -jar /home/godzilla/gzl/rpc/godzilla-rpc-provider.jar  >/home/godzilla/gzl/rpc/log.txt 2>&1 &
  ```
  </pre>

4. 初始化项目（注意TEST环境下zookeeper使用地址为:10.100.142.78:2181）


## 功能说明 ##
1. 部署流程：清除work目录->检出主干代码->合并各个分支 if not found confilict->mvn deploy -Pdev

2. 合并代码：清除work目录->检出主干代码->合并各个分支 if not found confilict-> success

3. 提交主干：清除work目录->检出主干代码->合并各个分支 if not found confilict-> 提交主干 ->删除分支
