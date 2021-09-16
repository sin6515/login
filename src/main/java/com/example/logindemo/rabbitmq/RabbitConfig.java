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
        return new Queue("registerEmployeeQueue", true);
    }

    @Bean
    public Queue manualBookQueue() {
        return new Queue("secondQueue", true);
    }
}
