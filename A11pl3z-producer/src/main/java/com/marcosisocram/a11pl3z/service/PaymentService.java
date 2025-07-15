package com.marcosisocram.a11pl3z.service;

import com.marcosisocram.a11pl3z.client.RabbitPublisher;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentSummary;
import com.marcosisocram.a11pl3z.dto.Totals;
import com.marcosisocram.a11pl3z.repository.PaymentProcessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProcessorRepository paymentProcessorRepository;

    private final RabbitPublisher rabbitPublisher;

    public void process( Payment payment ) {
        final LocalDateTime now = LocalDateTime.now( );

        payment.setRequestedAt( now );

        rabbitPublisher.sendMessage( payment );

        log.atInfo().log( "Processed Payment: {}", payment.getCorrelationId( ) );
    }

    @SneakyThrows
    public PaymentSummary getPayments( LocalDateTime from, LocalDateTime to ) {

        List< Totals > summaryTotals = paymentProcessorRepository.getSummaryTotals( from, to );

        Totals byDefault = summaryTotals.stream( ).filter( Totals :: byDefault ).findFirst( ).orElseGet( () -> new Totals( false, 0, 0d ) );

        Totals byFallback = summaryTotals.stream( ).filter( totals -> !totals.byDefault( ) ).findFirst( ).orElseGet(() -> new Totals( false, 0, 0d ) );

        return new PaymentSummary( byDefault, byFallback );

    }

    public void purge( ) {
        paymentProcessorRepository.purge( );
    }
}
