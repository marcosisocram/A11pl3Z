package com.marcosisocram.a11pl3z.repository;

import com.marcosisocram.a11pl3z.client.DefaultService;
import com.marcosisocram.a11pl3z.client.FallBackService;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentResponse;
import com.marcosisocram.a11pl3z.dto.Totals;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.generated.public_.tables.records.PaymentsRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessorRepository {
    private static final boolean PROCESSED_BY_DEFAULT = true;
    private static final boolean PROCESSED_BY_FALLBACK = false;

    private final DefaultService paymentProcessorDefaultClient;
    private final FallBackService paymentProcessorFallbackClient;

    private final PaymentRepository paymentRepository;


//    @Retry( name = "processPayment", fallbackMethod = "processPaymentfallback" )
    @CircuitBreaker( name = "processPayment", fallbackMethod = "processPaymentFallback" )
    public void processPayment( Payment payment ) {

        final ResponseEntity< PaymentResponse > processed = paymentProcessorDefaultClient.process( payment );

        if ( ! processed.getStatusCode( ).is2xxSuccessful( ) ) {
            throw new RuntimeException( "Deu merda" );
        }

        paymentRepository.save( payment, PROCESSED_BY_DEFAULT );

        log.atInfo( ).log( "Processado {} pelo default", payment.getCorrelationId( ) );

    }

//    @CircuitBreaker( name = "paymentProcessorFallback", fallbackMethod = "processPaymentFallbackAsync" )
    private void processPaymentFallback( Payment payment, Throwable e ) {

        final ResponseEntity< PaymentResponse > processed = paymentProcessorFallbackClient.process( payment );

        if ( ! processed.getStatusCode( ).is2xxSuccessful( ) ) {
            throw new RuntimeException( "Deu merda" );
        }

        paymentRepository.save( payment, PROCESSED_BY_FALLBACK );

        log.atInfo( ).log( "Processado {} pelo fallback", payment.getCorrelationId( ) );
    }

    private void processPaymentFallbackAsync( Payment payment, Throwable e ) {

        log.atInfo().log( "{} caiu no async", payment.getCorrelationId( ) );

//        CompletableFuture.runAsync( ( ) -> {
//
//            while ( true ) {
//                try {
//                    paymentProcessorDefaultClient.process( payment );
//                    paymentRepository.save( payment, PROCESSED_BY_DEFAULT );
//
//                    log.atInfo().log( "{} foi processado async", payment.getCorrelationId( ) );
//                    break;
//                } catch ( Throwable ex ) {
//                    log.atInfo( ).log( "Error processing Payment: {}", payment.getCorrelationId( ), ex );
//                }
//            }
//        } );
    }

    private Totals mapTo( Record3< Boolean, BigDecimal, Integer > record3 ) {
        return new Totals(record3.value1(), record3.value3( ), record3.value2( ).doubleValue( ) );
    }

    public List< Totals > getSummaryTotals( LocalDateTime from, LocalDateTime to ) {
        Result< Record3< Boolean, BigDecimal, Integer > > summary = paymentRepository.findSummary( from, to );

        return summary.stream( )
                .map( this :: mapTo )
                .toList( );
    }



    public void purge( ) {
        paymentRepository.purge( );
    }
}
