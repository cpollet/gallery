package net.cpollet.gallery.rest.core;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public final class ErrorResponse {
    private final int httpStatus;
    private final String message;
    private final String code;
    private final String correlationId;
    private final List<String> details;

    public ErrorResponse(int httpStatus, String message, String code, String correlationId, List<String> details) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
        this.correlationId = correlationId;
        this.details = details;
    }

    public ErrorResponse(int httpStatus, String message, String correlationId) {
        this(
                httpStatus,
                message,
                String.format("0%d", httpStatus),
                correlationId,
                Collections.emptyList()
        );
    }
}
