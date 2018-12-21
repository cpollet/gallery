package net.cpollet.gallery.it;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.Application;
import net.cpollet.gallery.domain.logins.Password;
import net.cpollet.gallery.it.domain.Albums;
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

@Slf4j
@Postgres11
@UnirestMapperConfiguration
@FreePortProvider
class AlbumsITest {
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

        DataSource dataSource = new DriverManagerDataSource(pgConfiguration.jdbcString(), pgConfiguration.username(), pgConfiguration.password());
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        template.update("INSERT INTO logins (username, password) VALUES (:username, :password)", ImmutableMap.of(
                "username", "user",
                "password", new Password("password".toCharArray()).toString()
        ));

        albums = new Albums(application.httpEndpoint())
                .withBasicAuth("user", "password");
    }

    @Test
    // we don't want to create constants on purpose
    @SuppressWarnings("squid:S1192")
    void test() throws UnirestException {
        albums.assertCountIs(0)
                .create("Animals", "...")
                .assertCountIs(1)
                .filter("published", "true")
                .assertCountIs(0);

        albums.assertExists(1L)
                .hasProperty("name", "Animals");

        albums.assertDoesNotExist(2L)
                .create("Beers", "...")
                .assertCountIs(2)
                .assertExists(2L)
                .hasProperty("name", "Beers");

        albums.sort("name", "desc")
                .assertNotEmpty()
                .hasProperty("[0].name", "Beers")
                .hasProperty("[1].name", "Animals");

        albums.sort("name", "asc")
                .assertNotEmpty()
                .hasProperty("[0].name", "Animals")
                .hasProperty("[1].name", "Beers");
    }
}
