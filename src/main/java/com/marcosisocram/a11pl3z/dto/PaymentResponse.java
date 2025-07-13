package com.marcosisocram.a11pl3z.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.io.Serial;
import java.io.Serializable;

//@RegisterReflectionForBinding(PaymentResponse.class)
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
