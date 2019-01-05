package net.cpollet.junit5;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.Arrays;

public final class FreePortProviderExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalStateException("Not able to get test class"));

        Arrays.stream(testClass.getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(Port.class))
                .findFirst()
                .ifPresent(f -> set(f, testClass, (Port) () -> {
                    try (ServerSocket socket = new ServerSocket(0)) {
                        return socket.getLocalPort();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    private void set(Field f, Class destination, Object value) {
        boolean accessible = f.canAccess(null);

        try {
            f.setAccessible(true);
            f.set(destination, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(String.format("Unable to configure field [%s]", f.getName()));
        } finally {
            f.setAccessible(accessible);
        }
    }

    public interface Port {
        int next();
    }
}
