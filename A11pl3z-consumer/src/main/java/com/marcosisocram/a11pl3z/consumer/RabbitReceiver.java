package com.marcosisocram.a11pl3z.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class RabbitReceiver {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @RabbitListener(queues = "spring-boot")
    public void receiveMessage(String message) {

        Payment payment = objectMapper.readValue( message, Payment.class );
        log.atDebug().log("Received <{}>", payment);

        paymentService.process( payment );
    }
}
