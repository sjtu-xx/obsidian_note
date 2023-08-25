首先根据[RabbitMQ安装](RabbitMQ安装.md) 安装好delay message插件
# **项目结构**

![https://img2018.cnblogs.com/blog/1165798/201809/1165798-20180917100642229-1865856711.png](https://img2018.cnblogs.com/blog/1165798/201809/1165798-20180917100642229-1865856711.png)

# 依赖

- Pom.xml
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.6.2</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.example</groupId>
        <artifactId>demo</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>demo</name>
        <description>demo</description>
        <properties>
            <java.version>11</java.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>com.rabbitmq</groupId>
                <artifactId>amqp-client</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-amqp</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
                <scope>runtime</scope>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
            <!-- swagger -->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>2.9.2</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>2.9.2</version>
            </dependency>
        </dependencies>
    
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    
    </project>
    ```
    

# **application.yml**

需要将publisher-confrems设为true，启动确认回调, 将 publisher-returns设为true 确认返回回调

```yaml
spring:
	rabbitmq:
		host: 127.0.0.1
		port: 5672
		username: guest
		password: guest
		publisher-confirms: true #消息发送到交换机确认机制，是否确认回调
		publisher-returns: true # 消息发送到交换机确认机制，是否返回回调
		listener:
			simple:
				acknowledge-mode: manual #需要ack确认
				concurrency: 1  # listener最小的线程数
				max-concurrency: 1 # 制定最大的消费者数量
				retry:
					enabled: true # 是否支持重试
```

# **配置类--RabbitConfig**

第一部分, 定义队列

```java
@Configuration
@EnableRabbit
public class RabbitConfig {
	@Resource
	private RabbitTemplate rabbitTemplate;
	
	/**
		* Queue可以有4个参数： 
		* 队列名
		* durable：是否持久化
		* auto-delete：在队列没有使用时自动删除）
		* excluesive：队列只允许同时有一个连接
		*/
	@Bean
	public Queue helloQueue() {
		// 默认使用direct exchange， 这个exchange为虚拟主机的默认exchange，默认routing key为队列的默认名称。
		return new Queue("queue-test"); 
	}
}
```

第二部分，设置一些消息处理策略

```java
@Bean
public RabbitTemplate rabbitTemplate() {
  // 消息发送失败，返回队列中，yml中需要配置publisher-returns：true
	rabbitTemplate.setMandatory(true);
	
	// 消息返回，yml需要配置pulisher-return: true
	rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey))-> {
	  String correlationId = message.getMessageProperties().getCorrelationIdString();
		log.debug("消息：{}发送失败，应答码：{}原因：{}交换机：{}路由键{}", correlationId, replyCode, replyText, exchange, routingKey);
	}
	
	// 消息确认，yml需要配置publisher-confirm:true
	rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
		if (ack) {
			// 消息发送到exchange成功
		} else {
			log.debug("消息发送到exchange失败，原因：{}", cause);
		}
	});
}
```

# **生产者**

```java
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 给hello队列发送消息
     */
    public void send() {
        for (int i =0; i< 100; i++) {
            String msg = "hello, 序号: " + i;
            System.out.println("Producer, " + msg);
            rabbitTemplate.convertAndSend("queue-test", msg);
        }
    }
}
```

# **消费者**

```java
@Component
public class Comsumer {
    private Logger log = LoggerFactory.getLogger(Comsumer.class);

    @RabbitListener(queues = "queue-test")
    public void process(Message message, Channel channel) throws IOException {
        // 采用手动应答模式, 手动确认应答更为安全稳定
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        log.info("receive: " + new String(message.getBody()));
    }
}
```

# **测试类**

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    private Producer producer;

    @Test
    public void contextLoads() {
        producer.send();
    }
}
```