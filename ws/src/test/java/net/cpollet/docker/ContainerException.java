package net.cpollet.docker;

public final class ContainerException extends RuntimeException {
    public ContainerException(String message, Exception e) {
        super(message, e);
    }

    public ContainerException(String message) {
        super(message);
    }
}
