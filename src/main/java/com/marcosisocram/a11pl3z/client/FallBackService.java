package com.marcosisocram.a11pl3z.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class FallBackService {

    @Value( "${services.processor-fallback}" )
    private String processorFallback;

    private final ObjectMapper objectMapper;

    @SneakyThrows
//    @CircuitBreaker( name = "paymentsProcessorFallback" )
    public ResponseEntity< PaymentResponse > process( Payment payment ) {
        log.atInfo().log( "FallBackService process");

        RestClient restClient = RestClient.builder( )
                .baseUrl( processorFallback )
                .build( );

        return restClient
                .post( )
                .uri( "/payments" )
                .contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON )
                .body( objectMapper.writeValueAsString( payment ) )
                .retrieve( )
                .toEntity( PaymentResponse.class );
    }
}
