package com.example.demo.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 自动配置
 * 1、RabbitAutoConfiguration
 * 2、有自动配置了连接工厂 ConnectionFactory
 * 3、RabbitProperties 封装了 RMQ的所有连接配置信息
 * 4、RabbitTemplate: 给RabbitMQ 发送和接收消息
 * 5、AmqpAdmin: RabbitMQ系统管理组件
 * 6、@EnableRabbit + @RabbitListener 监听消息队列的内容
 */

@EnableRabbit  // 开启基于注解的RabbitMQ模式
@SpringBootApplication
public class RabbitMqTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqTestApplication.class, args);
    }
}
