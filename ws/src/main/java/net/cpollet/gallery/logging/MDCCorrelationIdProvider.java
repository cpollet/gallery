package net.cpollet.gallery.logging;

import org.slf4j.MDC;

import java.util.UUID;

public final class MDCCorrelationIdProvider implements CorrelationIdProvider {
    private static final String CORRELATION_ID_KEY = "correlationId";

    @Override
    public String generate() {
        String correlationId = UUID.randomUUID().toString();

        MDC.put(CORRELATION_ID_KEY, correlationId);

        return correlationId;
    }
}
