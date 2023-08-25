package com.example.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 定义demoQueue队列
     *
     * @return
     */
    @Bean
    public Queue demoString() {
        return new Queue("demoQueue");
    }

}