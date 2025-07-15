package com.marcosisocram.a11pl3z.config;

import com.marcosisocram.a11pl3z.dto.HealthResponse;
import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentResponse;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints( RuntimeHints hints, ClassLoader classLoader ) {
        // Register serialization
        hints.serialization().registerType( PaymentResponse.class);
        hints.serialization().registerType( HealthResponse.class);
        hints.serialization().registerType( Payment.class);
    }
}
