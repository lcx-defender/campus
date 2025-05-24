# 环境准备

## 补齐配置文件

```yml
# application-dev.yml
spring:
  data:
    redis:
      host: Redis服务器IP
      port: 6379
      database: Redis数据库编号(默认为0)
      password: Redis密码
      timeout: 10s # 连接超时时间
      lettuce:
        pool:
          enabled: true
          max-active: 8
          min-idle: 0
          max-idle: 8
          max-wait: -1ms
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource 
    driver-class-name: com.mysql.cj.jdbc.Driver # MySQL驱动
    username: root # 数据库用户名
    password: yourself_password # 数据库密码
    url: jdbc:mysql://MySQL服务器IP:3306/campus?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    druid: # Druid连接池配置
      initialSize: 2
      minIdle: 2
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
  # spring-ai配置
  ai:
    openai:
      api-key: # 连接所用 API密钥
      base-url: https://dashscope.aliyuncs.com/compatible-mode # 连接模型所用 API地址
      chat:
        options:
          model: qwen-max-latest # 聊天模型名称
      # 向量模型
      embedding:
        options:
          model: text-embedding-v3 # 选用的向量模型
          dimensions: 1024 # 向量维度
    ollama:
      base-url: http://localhost:11434 # 配置的本地ollama服务地址(可自行修改)
      chat:
        model: deepseek-r1:1.5b # 聊天模型名称

dromara:
  x-file-storage: #文件存储配置
    default-platform: aliyun-oss-1 #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    #对应平台的配置写在这里，注意缩进要对齐
    aliyun-oss:
      - platform: aliyun-oss-1 # 存储平台标识
        enable-storage: true  # 启用存储
        access-key: # 阿里云OSS的AccessKeyId
        secret-key: # 阿里云OSS的AccessKeySecret
        end-point: # 阿里云OSS的Endpoint
        bucket-name: # 阿里云OSS的BucketName
        domain: https://your_bucket-name.your_end-point/ # 访问域名，注意“/”结尾，例如：https://abc.oss-cn-shanghai.aliyuncs.com/
        base-path: upload/ # 基础路径

# token配置
token:
  # 令牌自定义标识
  header: Authorization
  # 令牌密钥
  secret: # 令牌加密盐
  # 令牌有效期5小时(单位配置在JwtTokenServiceImpl)
  expireTime: 5
```