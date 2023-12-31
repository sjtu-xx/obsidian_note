# 任务队列（Redis）

<aside>
💡 任务队列用于管理后台工作，通常这些后台工作必须在 HTTP请求-响应循环 之外执行。

</aside>

## **消息队列**

消息队列，一般我们会简称它为MQ(Message Queue)。他是由两个单词组成，我们应该对队列(Queue)很熟悉吧。队列是一种先进先出的数据结构。再配合上消息，消息队列可以简单理解为：把要传输的数据放在队列中。使用较多的消息队列有**ActiveMQ，RabbitMQ，ZeroMQ，Kafka，MetaMQ，RocketMQ**。

### **场景**

消息队列中间件是分布式系统中重要的组件，主要解决应用耦合，异步消息，流量削锋等问题。这里举一个消息队列的使用场景：日志处理。

日志处理是指将消息队列用在日志处理中，比如Kafka的应用，解决大量日志传输的问题。架构简化如下：

[https://segmentfault.com/img/remote/1460000037713405](https://segmentfault.com/img/remote/1460000037713405)

- 日志采集客户端，负责日志数据采集，定时写入`Kfaka`队列。
- `Kfaka`消息队列，负责日志数据的接收，存储和转发。
- 日志处理应用，订阅并消费kafka队列中日志数据。

## **任务队列**

既然消息队列称为MQ，那么任务队列我们就可以叫其TQ(Task Message)。任务队列可以简单理解为：把要执行的任务放在队列中。使用较多的任务队列有`machiney`、`Celery`、`goWorker`、`YTask`。我写了一篇`[machinery`入门教程](https://link.segmentfault.com/?enc=BuhRhv0FXKOPD2g3sP%2FYrw%3D%3D.c1Bhj4ZbxT7kEuU0Zo1rdApfVyj4%2FLtg4asTNQ3YnGYFNunkyybtNVQAt1HZ6t5g6pXzDljmrQn1BVem87Qbsw%3D%3D)，并且翻译了[一篇`machinery`中文文档](https://link.segmentfault.com/?enc=J69bgqM%2BLVvBN%2BVANM3klg%3D%3D.ZanNHLdgChBOSFoboOvBSWIPQ%2FQItM3xaYgc5ZTvJiVKeV7qfvHatTSugfj%2B7dI9tH8fo2UtYCHoqFkPfhiAUA%3D%3D)，有需要的公众号自取。具体任务队列的细节就不讲了。这不是本文的主题，下面我们看一看任务队列的使用场景。

### **场景**

任务队列是用来执行一个耗时任务。大家可能都使用过马爸爸的花呗，每当我们还款时，就会增加自己的芝麻信用分。这就可以用到任务队列来计算用户的积分和等级了。架构简化如下：

[https://segmentfault.com/img/remote/1460000037713407](https://segmentfault.com/img/remote/1460000037713407)

- 用户还款，当用户还款成功时，发送一个计算用户积分计算的任务到任务队列。
- 任务队列，可以是`mq`，也可是`redis`，用来存储任务。
- 任务执行者，任务的执行者，监听任务队列，当任务队列中有任务时，便会执行。

## **区别**

消息队列和任务队列，我觉得最大的不同就是理念的不同：任务队列传递的是"任务"，消息队列传递的是"消息"。任务队列可以说是消息队列的二次开发。

两者区别：

- 消息队列更侧重于消息的吞吐、处理，具有有处理海量信息的能力。另外利用消息队列的生产者和消费者的概念，也可以实现任务队列的功能，但是还需要进行额外的开发处理。
- 任务队列则提供了执行任务所需的功能，比如任务的重试，结果的返回，任务状态记录等。虽然也有并发的处理能力，但一般不适用于高吞吐量快速消费的场景。其实任务队列和远程函数调用很像，不过和rpc调用不同，他的调用不是网络请求的方式，而是通过利用消息队列传递任务信息。

## Redis实现任务队列

借助**BRPOP**命令，可以实现一旦有新任务加入队列就通知消费者

BRPOP命令接收两个参数，第一个是键名，第二个是超时时间，单位是秒。当超过了此时间仍然没有获得新元素的话就会返回nil。**如果超时时间设为“0”，表示不限制等待的时间**，如果没有新元素加入列表就会永远阻塞下去。

BRPOP和RPOP命令相似，唯一区别是：任务列表中没有元素时BRPOP命令会一直阻塞住连接，直到有新元素加入。上面的伪代码可以优化为：

Redis伪代码

```bash
loop
    # 如果任务队列中没有新任务，BRPOP命令会一直阻塞，不会执行execute()
    $task = BRPOP queue, 0
    # 返回值是一个数组，数组的第二个元素是我们需要的任务
    execute($task[1])
```

队列有的时候需要优先级。比如：系统需要发送确认邮件和通知邮件两种任务同时存在时，应该优先执行确认邮件。具体场景如下，订阅一个名人的博客的用户有10万人，当该博客发布一篇新文章后，博客就会向任务队列中添加10万个发送通知邮件的任务。如果每一封邮件需要10ms，那么全部完成这10万个任务就需要：100 000*10/1000=1000秒（将近20分钟）。加入这期间有新用户想订阅该博客，当提交完自己的邮箱并看到网页提示查收确认邮件时，该用户并不知道向自己发送的确认邮件的任务被加入到已经有10万个任务的队列中，需要为此等待近20分钟。

BRPOP命令可以同时接受多个键，其完整的命令格式为：BRPOP key[key...]timeout,如：BRPOP queue1 queue2 0.着意味着同时检测多个键，如果其中有一个键有元素，则从该键中弹出元素；如果多个键都有元素，则按照从左到右的顺序取第一个键中的第一个元素。

借此特性可以实现区分优先级的任务队列。我们分别使用queue:confirmation.email和queue.notification.email两个键存储发送确认邮件和发送通知邮件两种任务，然后将消费者的伪代码修改为：

```bash
loop
    $task = 
    BRPOP  queue:confirmationl.email,
                queue:notification.email,
                0
    execute($task[1])
```