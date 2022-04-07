# tact-id
一个给分布式服务生成全局snowflakeID的轻量化服务, 不需要额外中间件, 开箱即用

有状态服务, 不建议集群部署, 压力实在大可以使用路由做垂直拆分

# module
- 根pom文件所管理的内容为服务端工程, web项目
- client是一个子项目, 更方便使用的一个客户端
  - IdFactory 为ID生成器的工厂类, 这里管理所有的ID生成器, 注意需要将bean提交给spring管理(可以在自己项目里面通过@Import(IdFactory.class)的方式注入)
  - IdGenerator 为ID生成器, 提供了nextId()的一个简单的API生成ID

## 构建
- 环境
  - jdk: oracle java17
    
  - maven 3.6.1

`git clone https://github.com/tactbug/tact-id.git`


`mvn clean -U install`


`java -jar XXX.jar`

> 构建过程会对ID并发生成执行一次单元测试, 可能会等待5-10s左右


## REST 接口

### getBatchId
- 说明: 根据服务名跟聚合名批量获取snowflakeID
- low_level API, 生成的是ID队列, 需要服务自己规划使用方式
- url: /id/batch/{application}/{domain}/{quantity}
- 方法: GET
- 参数
  - application: 服务名
  - domain: 领域名(对象主体名)
  - quantity: 请求ID数量, 最大300,000, 默认10,000
- 返回: Queue<Long> 生成的ID队列 

### IdFactory:getOrBuildGenerator(...)
- client方法, 用来创建或生成新的ID生成器
- 注意此方法如果IdFactory交由bean容器管理的话, ID队列的大小第一次调用的时候就确定了, 使用的时候尽量确保每个业务实体的ID池保证一致

### IdGenerator:nextId()
- client方法, 简单易用的生成下一个ID
