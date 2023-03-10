## Java

> ### 笔记点
>> #### 参考文档地址 <br>
>> 关键词：** <br>
>
>> 内容
>

> ### java相关学习示例
>> #### https://github.com/powerLeePlus/java-samples <br>
>> 这里有很多示例，可以学习

> ### Zookeeper集群“脑裂”问题处理
>> #### https://blog.51cto.com/mingongge/3063433 <br> 
>> 关键词：Zookeeper容错、奇数部署、过半机制、假死、Leader、follower、Quorums(法定人数)、epoch(标号) <br>
> 
>> 脑裂：由于假死会发起新的Leader选举，选举出一个新的Leader，但旧的Leader网络又通了，导致出现了两个Leader ，有的客户端连接到老的Leader，而有的客户端则连接到新的leader。<br>
ZooKeeper默认采用了Quorums这种方式来防止"脑裂"现象。<br/>
假设某个Leader假死，其余的followers选举出了一个新的Leader。这时，旧的Leader复活并且仍然认为自己是Leader，这个时候它向其他followers发出写请求也是会被拒绝的。
因为每当新Leader产生时，会生成一个epoch标号(标识当前属于那个Leader的统治时期)，这个epoch是递增的，followers如果确认了新的Leader存在，知道其epoch，就会拒绝epoch小于现任Leader epoch的所有请求。
那有没有follower不知道新的Leader存在呢？有可能，但肯定不是大多数，否则新Leader无法产生。Zookeeper的写也遵循quorum机制，因此，得不到大多数支持的写是无效的，旧Leader即使各种认为自己是leader，依然没有什么作用。
>

> ### RabbitMq
>> #### https://github.com/powerLeePlus/java-samples/tree/master/rabbitmq   <br>
>> 关键词：交换机、路由、消息、持久化、Ack机制、消费者确认：basicAck、消费者拒绝：basicNack、生产者确认的两种模式：事务与confirm机制<br>
>
>> 当你声明了一个名为”hello_queue”的队列，RabbitMQ会自动将其绑定到默认交换机上，绑定（binding）的路由键名称也是为”hello_queue”,消费者通过绑定的路由进行消费。<br>
> 如果要设置重回队列的话，要设置最大处理次数，例如为3，记录消费者处理失败的次数，当处理失败次数小于3调用Nack重回队列；如果达到了最大重试次数，则调用Ack删除消息，同时持久化该消息，后期采用定时任务或者手工处理。
> - Fanout exchange（扇型交换机）- Publish/Subscribe（发布订阅） - 一对多消费  
> - Direct exchange（直连交换机）- 够对消息按照一定的规则进行转发，消息会被推送至binding key和消息routing key完全匹配的队列，即使用特定的 binding key发送消息 - 一对一消费
> - Topic exchange（主题交换机）- 发送到topic交换机的消息不能具有任意的  routing_key —— 它必须是由点分隔的单词列表   
> \*（星号）可以替代一个单词， ＃（hash）可以替换零个或多个单词, 主题类型的转发器非常强大，可以实现其他类型的转发器。  
当一个队列与绑定键#绑定，将会收到所有的消息，类似fanout类型转发器。    
当绑定键中不包含任何#与\*时，类似direct类型转发器。
> - Dead Letter Exchange（DLX-死信交换机）  
> 死信消息有如下几种情况：1、消息被拒绝(Basic.Reject/Basic.Nack) ，井且设置requeue(重回队列) 参数为false ; 2、消息过期 ; 3、队列达到最大长度
> 
 



 