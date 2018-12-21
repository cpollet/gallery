package net.cpollet.gallery.codec;

public interface Deserializer {
    <T> T deserialize(String string, Class<T> target);
}
