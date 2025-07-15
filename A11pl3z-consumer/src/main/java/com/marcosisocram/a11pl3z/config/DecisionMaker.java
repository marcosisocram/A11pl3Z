package com.marcosisocram.a11pl3z.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DecisionMaker {

    public static ServiceChoice makeDecision(boolean defaultWorking, int defaultTimeResponse, boolean fallbackWorking, int fallbackTimeResponse) {
        if (!defaultWorking) {
            if (!fallbackWorking) {
                if (defaultTimeResponse <= fallbackTimeResponse) {
                    return ServiceChoice.DEFAULT;
                } else {
                    return ServiceChoice.FALLBACK;
                }
            } else {
                return ServiceChoice.DEFAULT;
            }
        } else {
            if (!fallbackWorking) {
                return ServiceChoice.FALLBACK;
            } else {
                return ServiceChoice.BOTH_FAILING;
            }
        }
    }

    public enum ServiceChoice {
        DEFAULT,
        FALLBACK,
        BOTH_FAILING
    }
}
