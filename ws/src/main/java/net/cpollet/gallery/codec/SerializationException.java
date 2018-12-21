package net.cpollet.gallery.codec;

public final class SerializationException extends RuntimeException {
    SerializationException(Exception e) {
        super(e);
    }
}
