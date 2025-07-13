package com.marcosisocram.a11pl3z.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CustomSpringEvent {

    private String message;

    public CustomSpringEvent(String message) {
        this.message = message;
    }
}
