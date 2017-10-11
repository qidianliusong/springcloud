package com.jiuchunjiaoyu.micro.aggregate.wzb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue payMessage() {
        return new Queue("wzb.pay.message");
    }

    @Bean
    public TopicExchange wzbExchange() {
        return new TopicExchange("wzb.topic");
    }

    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(payMessage()).to(wzbExchange()).with("wzb.pay.message");
    }

}
