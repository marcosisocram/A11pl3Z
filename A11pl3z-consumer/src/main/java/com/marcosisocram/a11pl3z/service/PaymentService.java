package com.marcosisocram.a11pl3z.service;

import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.exception.PaymentOutException;
import com.marcosisocram.a11pl3z.repository.PaymentProcessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProcessorRepository paymentProcessorRepository;

    public void process( Payment payment ) throws PaymentOutException {

        paymentProcessorRepository.processPayment( payment );

    }
}
