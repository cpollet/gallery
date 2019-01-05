package net.cpollet.kozan.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public final class Main {
    public static void main(String[] args) {
        String json = new Car("Skoda", "Octavia").print(new JsonMedia());
        log.info("{}", json);
    }

    static class Car implements Printable {
        private final String brand;
        private final String model;

        Car(String brand, String model) {
            this.brand = brand;
            this.model = model;
        }

        @Override
        public <T> T print(Media<T> media) {
            return media
                    .with("brand", () -> brand)
                    .with("model", () -> model)
                    .output();
        }
    }

    static class JsonMedia implements Media<String> {
        private final Map<String, Object> map;

        JsonMedia() {
            map = ImmutableMap.of();
        }

        private JsonMedia(Map<String, Object> map, String key, Object value) {
            this.map = ImmutableMap.<String, Object>builder()
                    .putAll(map)
                    .put(key, value)
                    .build();
        }

        @Override
        public Media<String> with(String key, Supplier<Object> valueSupplier) {
            return new JsonMedia(map, key, valueSupplier.get());
        }

        @SuppressWarnings("squid:S00112") // we dont care, this is an example
        @Override
        public String output() {
            try {
                return new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
