package com.marcosisocram.a11pl3z.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosisocram.a11pl3z.dto.HealthResponse;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentResponse;
import com.marcosisocram.a11pl3z.exception.PaymentOutException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class FallBackClient {

    @Value( "${services.processor-fallback}" )
    private String processorFallback;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public ResponseEntity< PaymentResponse > process( Payment payment ) {

        try {
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
        } catch ( HttpServerErrorException.InternalServerError e ) {
            throw new PaymentOutException( "Error ao processar com fallback" );
        }
    }

    @SneakyThrows
    public ResponseEntity< HealthResponse > health( ) {

        RestClient restClient = RestClient.builder( )
                .baseUrl( processorFallback )
                .build( );

        return restClient
                .get( )
                .uri( "/payments/service-health" )
                .accept( MediaType.APPLICATION_JSON )
                .retrieve( )
                .toEntity( HealthResponse.class );

    }
}
