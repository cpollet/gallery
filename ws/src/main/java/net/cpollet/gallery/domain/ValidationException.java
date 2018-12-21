package net.cpollet.gallery.domain;

public final class ValidationException extends RuntimeException {
    private ValidationException(String message) {
        super(message);
    }

    public static ValidationException isNull(String target) {
        return new ValidationException(String.format("[%s] cannot be <null>", target));
    }

    public static ValidationException isEmpty(String target) {
        return new ValidationException(String.format("[%s] cannot be <empty>", target));
    }

    public static ValidationException isLongerThan(String target, int length) {
        return new ValidationException(String.format("[%s] is longer than <%d> chars", target, length));
    }

    public static ValidationException isNotSupported(String target, String name) {
        return new ValidationException(String.format("[%s] does not support <%s>", target, name));
    }

    public static ValidationException isInvalid(String target, String string) {
        return new ValidationException(String.format("[%s] cannot parse <%s>", target, string));
    }
}
