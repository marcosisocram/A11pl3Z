package com.marcosisocram.a11pl3z.config;

import com.marcosisocram.a11pl3z.consumer.RabbitReceiver;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {

    public static final String topicExchangeName = "spring-boot-exchange";

    public static final String queueName = "spring-boot";

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("rinha.backend.#");
    }

//    @Bean
//    public StatefulRetryOperationsInterceptor interceptor() {
//        return RetryInterceptorBuilder.stateful()
//                .maxAttempts(5)
//                .backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval
//                .build();
//    }

//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }

//    @Bean
//    public MessageListenerAdapter listenerAdapter( RabbitReceiver rabbitReceiver) {
//        return new MessageListenerAdapter(rabbitReceiver, "receiveMessage");
//    }
}
