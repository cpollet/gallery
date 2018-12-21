package net.cpollet.gallery.rest.core;

import net.cpollet.gallery.rest.api.request.ValidatablePayload;

public final class ValidatePayload implements Action {
    private final Action wrapped;
    private final ValidatablePayload payload;

    public ValidatePayload(Action wrapped, ValidatablePayload payload) {
        this.wrapped = wrapped;
        this.payload = payload;
    }

    @Override
    public Response execute() {
        if (!payload.isValid()) {
            throw ActionException.NOT_ACCEPTABLE.withDetails(payload.errors());
        }
        return wrapped.execute();
    }
}
