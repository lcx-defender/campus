# 环境准备

## 补齐配置文件

```yml
spring:
  data:
    redis:
      host: redis服务器IP
      port: 6379
      database: 1
      password: Redis密码，没有可不加
      timeout: 10s
      lettuce:
        pool:
          enabled: true
          max-active: 8
          min-idle: 0
          max-idle: 8
          max-wait: -1ms
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql服务器IP:3306/campus?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true
    username: mysql的用户名，一般是root
    password: mysql的密码
dromara:
  x-file-storage: #文件存储配置
    default-platform: aliyun-oss-1 #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    #对应平台的配置写在这里，注意缩进要对齐
    aliyun-oss:
      - platform: aliyun-oss-1 # 存储平台标识
        enable-storage: true  # 启用存储
        access-key: 自行替换
        secret-key: 自行替换
        end-point: 自行替换
        bucket-name: 自行替换
        domain: https://自行替换/ # 访问域名，注意“/”结尾，例如：https://abc.oss-cn-shanghai.aliyuncs.com/
        base-path: upload/ # 基础路径
```