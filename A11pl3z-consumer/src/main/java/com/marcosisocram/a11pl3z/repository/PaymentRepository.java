package com.marcosisocram.a11pl3z.repository;

import com.marcosisocram.a11pl3z.dto.Payment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.public_.tables.Payments;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final DSLContext dslContext;

    public void save( Payment payment, Boolean byDefault ) {

        dslContext
                .insertInto( Payments.PAYMENTS )
                .values( payment.getCorrelationId( ), payment.getAmount(), payment.getRequestedAt( ), byDefault )
                .execute( );

    }
}
