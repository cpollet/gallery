package net.cpollet.gallery;

import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.database.JdbcDatabase;
import net.cpollet.gallery.domain.gallery.PgGallery;
import net.cpollet.gallery.rest.auth.AESEncryptedIdSessions;
import net.cpollet.gallery.rest.auth.InMemorySessions;
import net.cpollet.gallery.rest.undertow.UndertowServer;
import net.cpollet.liquibase.LiquibaseMigration;
import net.cpollet.security.crypt.AESKey;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Base64;

@Slf4j
public final class Application {
    private final String databaseConnectionString;
    private final String databaseUsername;
    private final String databasePassword;
    private final int httpPort;
    private final String httpHost;
    private final String applicationReadyMessage;

    public Application(String databaseConnectionString, String databaseUsername, String databasePassword,
                       int httpPort, String httpHost,
                       String applicationReadyMessage) {
        this.databaseConnectionString = databaseConnectionString;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.httpPort = httpPort;
        this.httpHost = httpHost;
        this.applicationReadyMessage = applicationReadyMessage;
    }

    public Application(String databaseConnectionString, String databaseUsername, String databasePassword,
                       int httpPort, String httpHost) {
        this(
                databaseConnectionString,
                databaseUsername,
                databasePassword,
                httpPort,
                httpHost,
                "ready."
        );
    }

    public Application start() {
        DataSource dataSource = new DriverManagerDataSource(databaseConnectionString, databaseUsername, databasePassword);

        migrateDatabase(dataSource);
        startHttpServer(dataSource);

        log.info(applicationReadyMessage);

        return this;
    }

    private void migrateDatabase(DataSource dataSource) {
        new LiquibaseMigration(dataSource).execute();
    }

    private void startHttpServer(DataSource dataSource) {
        UndertowServer httpServer = new UndertowServer(
                httpHost,
                httpPort,
                new PgGallery(
                        new JdbcDatabase(
                                new NamedParameterJdbcTemplate(dataSource)
                        )
                ),
                new AESEncryptedIdSessions(
                        new AESKey(Base64.getDecoder().decode("c60fogALoeuRGcyxolXCTg==")),
                        new InMemorySessions()
                ),
                new TransactionTemplate(
                        new DataSourceTransactionManager(dataSource)
                )
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Received shutdown signal");
            try {
                httpServer.shutdown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        httpServer.listen();
    }

    public String httpEndpoint() {
        return String.format("http://%s:%d", httpHost, httpPort);
    }
}
