package com.marcosisocram.a11pl3z.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import java.util.UUID;

@AutoConfigureMockMvc
@SpringBootTest
@EnableWireMock( {
        @ConfigureWireMock(
                name = "processor-default",
                baseUrlProperties = "processor-default.url",
                portProperties = "processor-default.port" ),
        @ConfigureWireMock(
                name = "processor-fallback",
                baseUrlProperties = "processor-fallback.url",
                portProperties = "processor-fallback.port" ),
} )
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectWireMock( "processor-default" )
    private WireMockServer mockProcessorDefault;

    @InjectWireMock( "processor-fallback" )
    private WireMockServer mockProcessorFallback;

    @Test
    @Disabled
    void testeFallback( ) throws Exception {


        mockProcessorDefault.stubFor( WireMock

                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.4" ) ) )
                .willReturn( WireMock.serverError( ) ) );


        mockProcessorFallback.stubFor( WireMock
                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.4" ) ) )
                .willReturn( WireMock.okJson( """
                        { "message": "payment processed successfully"}
                        """ ) ) );

        String uuid = UUID.randomUUID( ).toString( );

        mockMvc.perform( MockMvcRequestBuilders.post( "/payments" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( """
                                {
                                  "correlationId": "%s",
                                  "amount": 42.4
                                }
                                """.formatted( uuid ) ) )
                .andExpect( MockMvcResultMatchers.status( ).isOk( ) );
    }

    @Test
    @Disabled
    void testeCb( ) throws Exception {



        mockProcessorDefault.stubFor( WireMock
                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.5" ) ) )
                .willReturn( WireMock.serverError( ) ) );

        mockProcessorFallback.stubFor( WireMock
                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.5" ) ) )
                .willReturn( WireMock.okJson( """
                        { "message": "payment processed successfully"}
                        """ ) ) );

        for ( int i = 0; i <= 65; i++ ) {

            String uuid = UUID.randomUUID( ).toString( );

            mockMvc.perform( MockMvcRequestBuilders.post( "/payments" )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( """
                                    {
                                      "correlationId": "%s",
                                      "amount": 42.5
                                    }
                                    """.formatted( uuid ) ) )
                    .andExpect( MockMvcResultMatchers.status( ).isOk( ) );
        }
    }

    @Test
    @Disabled
    void testeCbClose( ) throws Exception {

        mockProcessorDefault.stubFor( WireMock
                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.6" ) ) )
                .willReturn( WireMock.aResponse( )
                        .withStatus( 200 )
                        .withBody( """
                                { "message": "payment processed successfully"}
                                """ )
                        .withLogNormalRandomDelay( 100, 0.5d, 150d ) ) );

        mockProcessorFallback.stubFor( WireMock
                .post( "/payments" )
                .withRequestBody( WireMock.matchingJsonPath( "amount", WireMock.equalTo( "42.6" ) ) )
                .willReturn( WireMock.okJson( """
                        { "message": "payment processed successfully"}
                        """ ) ) );

        for ( int i = 0; i <= 100; i++ ) {
            String uuid = UUID.randomUUID( ).toString( );

            mockMvc.perform( MockMvcRequestBuilders.post( "/payments" )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( """
                                    {
                                      "correlationId": "%s",
                                      "amount": 42.6
                                    }
                                    """.formatted( uuid ) ) )
                    .andExpect( MockMvcResultMatchers.status( ).isOk( ) );
        }
    }
}
