package com.example.logindemo.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hrh13
 * @date 2021/9/15
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue defaultBookQueue() {
        // 第一个是 QUEUE 的名字,第二个是消息是否需要持久化处理
        return new Queue("firstQueue", true);
    }
    @Bean
    public Queue manualBookQueue() {
        // 第一个是 QUEUE 的名字,第二个是消息是否需要持久化处理
        return new Queue("secondQueue", true);
    }

}
