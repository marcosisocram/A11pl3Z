package com.marcosisocram.a11pl3z.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DecisionMaker {

    public static ServiceChoice makeDecision(boolean defaultWorking, int defaultTimeResponse, boolean fallbackWorking, int fallbackTimeResponse) {
        if (!defaultWorking) {
            // 2. Verificar se o serviço FALLBACK também está funcionando
            if (!fallbackWorking) {
                // Ambos funcionando: Escolher o com menor tempo de resposta
                if (defaultTimeResponse <= fallbackTimeResponse) {
                    return ServiceChoice.DEFAULT;
                } else {
                    return ServiceChoice.FALLBACK;
                }
            } else {
                // DEFAULT funcionando, FALLBACK não: Usar DEFAULT
                return ServiceChoice.DEFAULT;
            }
        } else {
            // DEFAULT não está funcionando
            // 3. Verificar se o FALLBACK está funcionando
            if (!fallbackWorking) {
                // FALLBACK funcionando, DEFAULT não: Usar FALLBACK
                return ServiceChoice.FALLBACK;
            } else {
                // 4. Nenhum dos serviços está funcionando
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
