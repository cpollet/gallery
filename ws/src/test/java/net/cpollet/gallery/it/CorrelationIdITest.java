package net.cpollet.gallery.it;

import com.mashape.unirest.http.exceptions.UnirestException;
import net.cpollet.gallery.Application;
import net.cpollet.gallery.it.domain.Albums;
import net.cpollet.junit5.FreePortProviderExtension;
import net.cpollet.junit5.Postgres11Extension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@IntegrationTest
class CorrelationIdITest {
    private static Postgres11Extension.Configuration pgConfiguration;
    private static FreePortProviderExtension.Port port;
    private static Albums albums;

    @BeforeAll
    static void beforeAll() {
        Application application = new Application(
                pgConfiguration.jdbcString(),
                pgConfiguration.username(),
                pgConfiguration.password(),
                port.next(),
                "localhost"
        ).start();

        albums = new Albums(application.httpEndpoint())
                .withBasicAuth("user", "password");
    }


    @Test
    void correlationIdIsPreserved() throws UnirestException {
        albums.correlationId("correlation")
                .list()
                .assertHeader("X-Gallery-CorrelationId")
                .assertEquals("correlation");
    }
}
