package org.example.authservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_REQUEST_QUEUE = "user.request.queue";
    public static final String USER_REG_REQUEST_QUEUE = "user_reg.request.queue";
    public static final String USER_RESPONSE_QUEUE = "user.response.queue";

    @Bean
    public Queue requestQueue() {
        return new Queue(USER_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue requestRegQueue() {
        return new Queue(USER_REG_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(USER_RESPONSE_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}