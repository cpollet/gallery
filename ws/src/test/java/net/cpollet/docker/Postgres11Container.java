package net.cpollet.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.kozan.lazy.Lazy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
public final class Postgres11Container implements Container {
    private static final String IMAGE_NAME = "postgres:11";
    private final String databaseName;
    private final String username;
    private final String password;
    private final String containerName;
    private final DockerClient dockerClient;
    private final Lazy<Container> container;

    public Postgres11Container(String databaseName, String username, String password, String containerName, DockerClient dockerClient) {
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        this.containerName = containerName;
        this.dockerClient = dockerClient;
        this.container = new Lazy<>(this::createContainer);
    }

    public Postgres11Container(String databaseName, String username, String password) {
        this(
                databaseName,
                username,
                password,
                "postgres-" + UUID.randomUUID().toString(),
                DockerClientBuilder.getInstance().build()
        );
    }

    public Postgres11Container(String databaseName) {
        this(databaseName, "postgres", "password");
    }

    private Container createContainer() {
        try {
            downloadImage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new InvalidContainer(containerName, new ContainerException("Interrupted while downloading image"));
        }

        try (CreateContainerCmd cmd = dockerClient.createContainerCmd(IMAGE_NAME)
                .withName(containerName)
                .withNetworkMode("bridge")
                .withPublishAllPorts(true)
                .withEnv("POSTGRES_PASSWORD=" + password)
                .withEnv("POSTGRES_USER=" + username)) {

            return new GenericContainer(
                    dockerClient,
                    cmd.exec().getId(),
                    containerName,
                    new LogReadinessCheck(Arrays.asList(
                            "listening on IPv4 address \"0.0.0.0\", port 5432",
                            "database system is ready to accept connections"
                    ))
            );
        }
    }

    private void downloadImage() throws InterruptedException {
        log.info("Downloading [{}] for [{}]", IMAGE_NAME, containerName);
        CountDownLatch latch = new CountDownLatch(1);
        dockerClient.pullImageCmd(IMAGE_NAME).exec(new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                super.onNext(item);
                log.trace("{}", item.getStatus());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                latch.countDown();
            }
        });
        latch.await();
    }

    @Override
    public void start() {
        try {
            container().start();
            createDatabase();
        } catch (Exception e) {
            throw new ContainerException(String.format("Unable to start container [%s]", containerName), e);
        }
    }

    @Override
    public void startAsync() {
        container().startAsync();
    }

    @Override
    public void waitReady() throws InterruptedException {
        container().waitReady();
    }

    private Container container() {
        return container.value();
    }

    private void createDatabase() {
        try {
            try (Connection connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://localhost:%d/", container().exposedPort(5432)), username(), password())
            ) {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("create database " + databaseName);
                }
            }
        } catch (SQLException e) {
            throw new ContainerException(String.format("Unable to create database [%s] in container [%s]", databaseName, containerName), e);
        }
    }

    @Override
    public void stop() {
        container().stop();
    }

    @Override
    public boolean running() {
        return container().running();
    }

    @Override
    public int exposedPort(int containerPort) {
        return container().exposedPort(containerPort);
    }

    public String jdbcString() {
        return String.format("jdbc:postgresql://localhost:%d/%s", container().exposedPort(5432), databaseName);
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
