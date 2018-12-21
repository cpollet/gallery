package net.cpollet.gallery.rest.core;

import java.util.Collections;
import java.util.List;

public final class ActionException extends RuntimeException {
    public static final ActionException PAYLOAD_NOT_DESERIALIZABLE = new ActionException("Unable to deserialize input", 400);
    public static final ActionException NOT_FOUND = new ActionException("Not Found", 404);
    public static final ActionException METHOD_NOT_ALLOWED = new ActionException("Method Not Allowed", 405);
    public static final ActionException NOT_ACCEPTABLE = new ActionException("Not Acceptable", 406);
    public static final RuntimeException UNAUTHORIZED = new ActionException("Unauthorized", 401);

    private final int httpStatus;
    private final String code;
    private final List<String> details;

    public ActionException(String message, String code, int httpStatus, List<String> details) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public ActionException(String message, int httpStatus) {
        this(message, String.format("0%d", httpStatus), httpStatus, Collections.emptyList());
    }

    public ActionException(String message, String code, int httpStatus) {
        this(
                message,
                code,
                httpStatus,
                Collections.emptyList()
        );
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public List<String> getDetails() {
        return details;
    }

    public ActionException withDetails(List<String> details) {
        return new ActionException(getMessage(), code, httpStatus, details);
    }
}
