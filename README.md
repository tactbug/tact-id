# tact-id
一个给分布式服务生成全局snowflakeID的服务
不建议集群部署, 可以考虑主备运行, 压力实在大可以通过gateway将指定domain路由到指定服务

## 构建
- 环境
  - jdk: oracle java17
    
  - maven 3.6.1

`git clone https://github.com/tactbug/tact-id.git`


`mvn clean -U install`


`java -jar XXX.jar`

> 构建过程会对ID并发生成执行一次单元测试, 可能会等待5-10s左右


## REST 接口

### getSnowFlakeId
- 说明: 根据服务名跟聚合名批量获取snowflakeID
- url: /id/snowflake/batch/{application}/{domain}/{quantity}
- 方法: GET
- 参数
  - application: 服务名
  - domain: 领域名(对象主体名)
  - quantity: 请求ID数量, 最大300,000, 最小10,000
- 返回: Queue\<Long> 生成的ID队列 

