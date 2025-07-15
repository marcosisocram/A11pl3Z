package com.marcosisocram.a11pl3z.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = - 4743277877732906325L;

    private String correlationId;
    private Double amount;
    private LocalDateTime requestedAt;
}
