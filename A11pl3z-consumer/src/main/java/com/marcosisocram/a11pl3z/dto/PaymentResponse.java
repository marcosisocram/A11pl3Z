package com.marcosisocram.a11pl3z.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 3448443948121682074L;

    private String message;
}
