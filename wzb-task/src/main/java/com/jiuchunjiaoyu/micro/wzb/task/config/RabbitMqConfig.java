package com.jiuchunjiaoyu.micro.wzb.task.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    /**
     * 消费者数量
     */
    @Value("${pay.message.consumer.concurrent:2}")
    private Integer consumerCount;

    /**
     * 每个消费者最大投递数量
     */
    @Value("${pay.message.consumer.prefreshcount:4}")
    private Integer preFreshCount;

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

    @Bean("payMessageTaskContainerFactory")
    public SimpleRabbitListenerContainerFactory payMessageTaskContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(preFreshCount);
        factory.setConcurrentConsumers(consumerCount);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
