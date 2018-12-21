package net.cpollet.gallery.it;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.junit5.FreePortProvider;
import net.cpollet.junit5.Postgres11;
import net.cpollet.junit5.UnirestMapperConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Postgres11
@UnirestMapperConfiguration
@FreePortProvider
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface IntegrationTest {
}
