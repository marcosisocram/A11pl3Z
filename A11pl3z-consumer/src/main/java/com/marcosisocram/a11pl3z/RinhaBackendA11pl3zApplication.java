package com.marcosisocram.a11pl3z;

import com.marcosisocram.a11pl3z.config.MyRuntimeHints;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ImportRuntimeHints( { MyRuntimeHints.class } )
@EnableRabbit
@EnableScheduling
public class RinhaBackendA11pl3zApplication {

    public static void main( String[] args ) {
        SpringApplication.run( RinhaBackendA11pl3zApplication.class, args );
    }

}
