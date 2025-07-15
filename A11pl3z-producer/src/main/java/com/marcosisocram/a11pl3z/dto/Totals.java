package com.marcosisocram.a11pl3z.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Totals( @JsonIgnore Boolean byDefault, Integer totalRequests, Double totalAmount ) {
}
