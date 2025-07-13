package com.marcosisocram.a11pl3z.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class CustomSpringEventListener{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @EventListener
    public void onApplicationEvent(CustomSpringEvent event) {
        log.atInfo().log("Received spring custom event - {}", event.getMessage());
        int i = atomicInteger.incrementAndGet( );

        if(i < 3) {
            log.atInfo().log("throw spring custom event - {}", event.getMessage());
            throw new RuntimeException( "" );
        }

        log.atInfo().log("Process spring custom event - {}", event.getMessage());
    }
}
