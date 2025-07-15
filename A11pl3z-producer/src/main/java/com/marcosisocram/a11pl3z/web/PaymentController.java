package com.marcosisocram.a11pl3z.web;

import com.marcosisocram.a11pl3z.dto.Payment;
import com.marcosisocram.a11pl3z.dto.PaymentSummary;
import com.marcosisocram.a11pl3z.dto.Totals;
import com.marcosisocram.a11pl3z.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping( "/" )
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @ResponseStatus( HttpStatus.OK )
    @PostMapping( "/payments" )
    public void payments( @RequestBody Payment payment ) {
        paymentService.process( payment );
    }

    @ResponseStatus( HttpStatus.OK )
    @PostMapping( "/purge-payments" )
    public void purgePayments( ) {
        log.atInfo( ).log( "purge-payment" );
        paymentService.purge( );
    }

    @GetMapping( "/payments-summary" )
    public ResponseEntity< PaymentSummary > paymentsSummary( @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("to") LocalDateTime to ) {
        log.atDebug( ).log( "payments-summary from {} to {}", from, to );
        return ResponseEntity.ok( paymentService.getPayments(  from, to ) );
    }
}
