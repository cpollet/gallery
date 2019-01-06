package net.cpollet.gallery.it;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.cpollet.gallery.Application;
import net.cpollet.gallery.domain.logins.Password;
import net.cpollet.gallery.it.domain.Albums;
import net.cpollet.gallery.it.domain.Sessions;
import net.cpollet.junit5.FreePortProvider;
import net.cpollet.junit5.FreePortProviderExtension;
import net.cpollet.junit5.Postgres11;
import net.cpollet.junit5.Postgres11Extension;
import net.cpollet.junit5.UnirestMapperConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Postgres11
@UnirestMapperConfiguration
@FreePortProvider
class AuthITest {
    private static Postgres11Extension.Configuration pgConfiguration;
    private static FreePortProviderExtension.Port port;
    private static Albums albums;
    private static Sessions sessions;

    @BeforeAll
    static void beforeAll() {
        Application application = new Application(
                pgConfiguration.jdbcString(),
                pgConfiguration.username(),
                pgConfiguration.password(),
                port.next(),
                "localhost"
        ).start();

        DataSource dataSource = new DriverManagerDataSource(pgConfiguration.jdbcString(), pgConfiguration.username(), pgConfiguration.password());
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        template.update("INSERT INTO logins (username, password) VALUES (:username, :password)", ImmutableMap.of(
                "username", "user",
                "password", new Password("password".toCharArray()).toString()
        ));

        albums = new Albums(application.httpEndpoint());
        sessions = new Sessions(application.httpEndpoint());
    }

    @Test
    void cookie() throws UnirestException {
        String sessionCookie = sessions.create("user", "password");

        albums.withCookieAuth(sessionCookie)
                .assertCountIs(0);
    }

    @Test
    void basic() throws UnirestException {
        albums.withBasicAuth("user", "password")
                .assertCountIs(0);
    }

    @Test
    void no() throws UnirestException {
        albums.assertCountIsUnauthorized();
    }
}
