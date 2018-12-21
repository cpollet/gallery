package net.cpollet.kozan.maybe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public interface Maybe<T> {
    boolean valid();

    List<String> errors();

    T value();

    T or(T fallback);

    T or(Supplier<T> fallback);

    final class Error<T> implements Maybe<T> {
        private final List<String> errors;

        public Error(List<String> errors) {
            this.errors = errors;
        }

        public Error(String... errors) {
            this(Arrays.asList(errors));
        }

        @Override
        public boolean valid() {
            return false;
        }

        @Override
        public List<String> errors() {
            return errors;
        }

        @Override
        public T value() {
            throw new IllegalStateException("No value to return, call valid() before or use or()");
        }

        @Override
        public T or(T fallback) {
            return fallback;
        }

        @Override
        public T or(Supplier<T> fallback) {
            return fallback.get();
        }
    }

    final class Success<T> implements Maybe<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public boolean valid() {
            return true;
        }

        @Override
        public List<String> errors() {
            return Collections.emptyList();
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public T or(T fallback) {
            return value;
        }

        @Override
        public T or(Supplier<T> fallback) {
            return value;
        }
    }
}
