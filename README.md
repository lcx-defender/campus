# 环境准备

## 补齐配置文件

```yml
spring:
  data:
    redis:
      host: redis主机IP
      port: 6379(默认端口)
      database: 1(选用的数据库)
      password: redis密码
      timeout: 10s
      lettuce:
        pool:
          enabled: true
          max-active: 8
          min-idle: 0
          max-idle: 8
          max-wait: -1ms
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: 数据库用户名，默认root
    password: 自己数据库用户对应的密码
    url: jdbc:mysql://mysql数据库所在主机IP:3306/campus?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    druid:
      initialSize: 5
      minIdle: 5
      maxActive: 20
      maxWait: 60000
      timeBetweenEvictionRunsMillis: 60000
      minEvictableIdleTimeMillis: 300000
      validationQuery: SELECT 1 FROM DUAL
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      poolPreparedStatements: true
      maxPoolPreparedStatementPerConnectionSize: 20
      connectTimeout: 30000
      socketTimeout: 60000



dromara:
  x-file-storage: #文件存储配置
    default-platform: aliyun-oss-1 #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    #对应平台的配置写在这里，注意缩进要对齐
    aliyun-oss:
      - platform: aliyun-oss-1 # 存储平台标识
        enable-storage: true  # 启用存储
        access-key: 自己的 # 阿里云accessKeyId
        secret-key: 自己的 # 阿里云accessKeySecret
        end-point: oss-cn-shanghai.aliyuncs.com # 阿里云存储区域节点
        bucket-name: 自定义 # 阿里云存储空间名称
        domain:  # 访问域名，注意“/”结尾，例如：https://abc.oss-cn-shanghai.aliyuncs.com/
        base-path: upload/ # 基础路径

# token配置
token:
  # 令牌自定义标识
  header: Authorization
  # 令牌密钥，根据需要自己修改
  secret: abcdefghijklmnopqrstuvwxyz
  # 令牌有效期300分钟
  expireTime: 300
```