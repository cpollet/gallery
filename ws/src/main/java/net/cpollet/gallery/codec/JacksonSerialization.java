package net.cpollet.gallery.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JacksonSerialization implements Serializer, Deserializer {
    @Override
    public String serialize(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(String string, Class<T> target) {
        try {
            return new ObjectMapper().readValue(string, target);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
