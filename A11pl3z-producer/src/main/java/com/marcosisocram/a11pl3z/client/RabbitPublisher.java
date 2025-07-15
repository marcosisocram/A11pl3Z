package com.marcosisocram.a11pl3z.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosisocram.a11pl3z.config.RabbitMQConfig;
import com.marcosisocram.a11pl3z.dto.Payment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendMessage( Payment payment ) {
        rabbitTemplate.convertAndSend( RabbitMQConfig.topicExchangeName, "rinha.backend.process", objectMapper.writeValueAsString( payment ) );
    }
}
