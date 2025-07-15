package com.marcosisocram.a11pl3z.repository;

import com.marcosisocram.a11pl3z.dto.Totals;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessorRepository {

    private final PaymentRepository paymentRepository;


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
