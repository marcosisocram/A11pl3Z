package com.marcosisocram.a11pl3z.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummary( @JsonProperty( "default" ) Totals defaults,
                              @JsonProperty( "fallback" ) Totals fallback ) {
}
