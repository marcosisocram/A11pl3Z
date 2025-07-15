package com.marcosisocram.a11pl3z.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.generated.public_.tables.Payments;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final DSLContext dslContext;

    public Result< Record3< Boolean, BigDecimal, Integer > > findSummary(LocalDateTime from, LocalDateTime to) {
        return dslContext
                .select( Payments.PAYMENTS.PROCESSED_AT_DEFAULT, DSL.sum( Payments.PAYMENTS.AMOUNT ), DSL.count( Payments.PAYMENTS.CORRELATION_ID ) )
                .from( Payments.PAYMENTS )
                .where( Payments.PAYMENTS.REQUESTED_AT.between( from, to ) )
                .groupBy( Payments.PAYMENTS.PROCESSED_AT_DEFAULT )
                .orderBy( Payments.PAYMENTS.PROCESSED_AT_DEFAULT.desc( ) )
                .fetch( );
    }

    public void purge( ) {
        dslContext
                .deleteFrom( Payments.PAYMENTS )
                .execute( );
    }
}
