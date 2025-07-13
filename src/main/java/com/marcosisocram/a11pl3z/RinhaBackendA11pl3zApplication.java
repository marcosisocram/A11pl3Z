package com.marcosisocram.a11pl3z;

import com.marcosisocram.a11pl3z.config.MyRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints( { MyRuntimeHints.class } )
public class RinhaBackendA11pl3zApplication {

    public static void main( String[] args ) {
        SpringApplication.run( RinhaBackendA11pl3zApplication.class, args );
    }

}
