package com.marcosisocram.a11pl3z.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HealthResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = - 4392413525500419319L;

    private Boolean failing;
    private Integer minResponseTime;

}
