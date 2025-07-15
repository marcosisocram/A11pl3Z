package com.marcosisocram.a11pl3z.repository;

import com.marcosisocram.a11pl3z.client.DefaultClient;
import com.marcosisocram.a11pl3z.client.FallBackClient;
import com.marcosisocram.a11pl3z.config.DecisionMaker;
import com.marcosisocram.a11pl3z.dto.HealthResponse;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessorRepository {
    private static final boolean PROCESSED_BY_DEFAULT = true;
    private static final boolean PROCESSED_BY_FALLBACK = false;

    private final DefaultClient paymentProcessorDefaultClient;
    private final FallBackClient paymentProcessorFallbackClient;

    private final PaymentRepository paymentRepository;

    private DecisionMaker.ServiceChoice SERVICE = DecisionMaker.ServiceChoice.DEFAULT;

    @Scheduled( fixedRate = 4998 )
    private void process( ) {

        log.atDebug().log( "Iniciando chamando" );
        ResponseEntity< HealthResponse > health = paymentProcessorDefaultClient.health( );
        HealthResponse healthDefault = health.getBody( );
        log.atDebug( ).log( "{}", healthDefault );
        ResponseEntity< HealthResponse > health1 = paymentProcessorFallbackClient.health( );
        HealthResponse healthFallback = health1.getBody( );
        log.atDebug( ).log( "{}", healthFallback );

        assert healthDefault != null;
        assert healthFallback != null;

        SERVICE = DecisionMaker.makeDecision( healthDefault.getFailing( ), healthDefault.getMinResponseTime( ), healthFallback.getFailing( ), healthFallback.getMinResponseTime( ) );
        log.atDebug().log( "{}", SERVICE );
    }

    public void processPayment( Payment payment ) {//recebe consumer

        if ( SERVICE == DecisionMaker.ServiceChoice.BOTH_FAILING ) {
            throw new IllegalStateException( "BOTH FAILING" );
        }

        if ( SERVICE == DecisionMaker.ServiceChoice.DEFAULT ) {
            final ResponseEntity< PaymentResponse > processed = paymentProcessorDefaultClient.process( payment );

            if ( ! processed.getStatusCode( ).is2xxSuccessful( ) ) {
                throw new RuntimeException( "Deu merda" );
            }
            paymentRepository.save( payment, PROCESSED_BY_DEFAULT );
            log.atInfo( ).log( "Processado {} pelo default", payment.getCorrelationId( ) );
        } else {
            final ResponseEntity< PaymentResponse > processed = paymentProcessorFallbackClient.process( payment );

            if ( ! processed.getStatusCode( ).is2xxSuccessful( ) ) {
                throw new RuntimeException( "Deu merda" );
            }

            paymentRepository.save( payment, PROCESSED_BY_FALLBACK );

            log.atInfo( ).log( "Processado {} pelo fallback", payment.getCorrelationId( ) );
        }
    }
}
