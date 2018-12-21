package net.cpollet.junit5;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.docker.Postgres11Container;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
public final class Postgres11Extension implements BeforeAllCallback, AfterAllCallback {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
            "net", "cpollet", "junit5", "Postgres11Extension"
    );

    @Override
    public void beforeAll(ExtensionContext context) {
        Postgres11Container postgresContainer = new Postgres11Container("gallery");
        postgresContainer.start();

        context.getStore(NAMESPACE).put(Postgres11Container.class, postgresContainer);

        Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalStateException("Not able to get test class"));

        Arrays.stream(testClass.getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(Configuration.class))
                .findFirst()
                .ifPresent(f -> set(f, testClass, new Configuration() {
                    @Override
                    public String jdbcString() {
                        return postgresContainer.jdbcString();
                    }

                    @Override
                    public String username() {
                        return postgresContainer.username();
                    }

                    @Override
                    public String password() {
                        return postgresContainer.password();
                    }
                }));
    }

    private void set(Field f, Class destination, Object value) {
        boolean accessible = f.isAccessible();

        try {
            f.setAccessible(true);
            f.set(destination, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(String.format("Unable to configure field [%s]", f.getName()));
        } finally {
            f.setAccessible(accessible);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Postgres11Container postgresContainer = context.getStore(NAMESPACE).get(Postgres11Container.class, Postgres11Container.class);
        context.getStore(NAMESPACE).remove(Postgres11Container.class);

        if ("true".equals(System.getProperty("keepDatabase"))) {
            log.info("You may connect to the postgres with {}:{} on {}",
                    postgresContainer.username(),
                    postgresContainer.password(),
                    postgresContainer.jdbcString()
            );
        } else {
            log.info("Stopping and deleting database. Set system property keepDatabase to true to avoid destroying database");
            postgresContainer.stop();
        }
    }

    public interface Configuration {
        String jdbcString();

        String username();

        String password();
    }
}
