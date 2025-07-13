package com.marcosisocram.a11pl3z.service;

import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentSummary;
import com.marcosisocram.a11pl3z.dto.Totals;
import com.marcosisocram.a11pl3z.event.CustomSpringEvent;
import com.marcosisocram.a11pl3z.repository.PaymentProcessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProcessorRepository paymentProcessorRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void process( Payment payment ) {
        final LocalDateTime now = LocalDateTime.now( );
        payment = new Payment( payment.getCorrelationId( ), payment.getAmount( ), now );

        log.atInfo().log( "Processing Payment: {}", payment );

        CustomSpringEvent customSpringEvent = new CustomSpringEvent("processing");
        applicationEventPublisher.publishEvent(customSpringEvent);

        try {
            paymentProcessorRepository.processPayment( payment );
        } catch ( Throwable e ) {
            log.atInfo().log( "{}", e.getClass() );
            log.atInfo().log( "Error processing Payment: {}", payment );
            log.atInfo().log( "Error: {}", e.getMessage() );
            if(log.isDebugEnabled()) {
                log.atError().log( "Error processing Payment: {}", payment, e );
            }
        }
    }

    @SneakyThrows
    public PaymentSummary getPayments( LocalDateTime from, LocalDateTime to ) {

//        CompletableFuture< List< Payment > > listCompletableFutureDefault = CompletableFuture.supplyAsync( ( ) -> paymentProcessorRepository.getPayFromDefault( from, to ) );
//
//        CompletableFuture< List< Payment > > listCompletableFutureFallback = CompletableFuture.supplyAsync( ( ) -> paymentProcessorRepository.getPayFromFallback( from, to ) );
//
//        CompletableFuture.allOf( listCompletableFutureDefault, listCompletableFutureFallback ).join();
//
//        List< Payment > payFromDefault = listCompletableFutureDefault.get( );
//        List< Payment > payFromFallback = listCompletableFutureFallback.get( );
//
//        double summedDefaults = payFromDefault.stream( ).mapToDouble( Payment :: getAmount ).sum( );
//        double summedFallback = payFromFallback.stream( ).mapToDouble( Payment :: getAmount ).sum( );

        List< Totals > summaryTotals = paymentProcessorRepository.getSummaryTotals( from, to );

        Totals byDefault = summaryTotals.stream( ).filter( Totals :: byDefault ).findFirst( ).orElseGet( () -> new Totals( false, 0, 0d ) );

        Totals byFallback = summaryTotals.stream( ).filter( totals -> !totals.byDefault( ) ).findFirst( ).orElseGet(() -> new Totals( false, 0, 0d ) );

        return new PaymentSummary( byDefault, byFallback );

    }

    public void purge( ) {
        paymentProcessorRepository.purge( );
    }
}
