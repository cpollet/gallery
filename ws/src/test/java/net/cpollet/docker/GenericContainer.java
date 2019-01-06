package net.cpollet.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GenericContainer implements Container {
    private static final Logger log = LoggerFactory.getLogger(GenericContainer.class);

    private final String id;
    private final DockerClient dockerClient;
    private final String containerName;
    private final ReadinessCheck readinessCheck;

    GenericContainer(DockerClient dockerClient, String id, String containerName, ReadinessCheck readinessCheck) {
        log.debug("ContainerID: {}", id);
        log.debug("container name: {}", containerName);

        this.dockerClient = dockerClient;
        this.id = id;
        this.containerName = containerName;
        this.readinessCheck = readinessCheck;
    }

    GenericContainer(DockerClient dockerClient, String id, String containerName) {
        this(
                dockerClient,
                id,
                containerName,
                ReadinessCheck.NOWAIT
        );
    }

    @Override
    public void start() throws InterruptedException {
        startAsync();
        waitReady();
    }

    @Override
    public void startAsync() {
        log.info("Starting container [{}]", containerName);

        if (running()) {
            throw new IllegalStateException(String.format("Container [%s] already running", containerName));
        }

        dockerClient.startContainerCmd(id).exec();
    }

    @Override
    public void waitReady() throws InterruptedException {
        readinessCheck.await(dockerClient, id);
    }

    @Override
    public void stop() {
        log.info("Stopping container [{}]", containerName);

        if (!running()) {
            throw new IllegalStateException(String.format("Container [%s] not running", containerName));
        }

        dockerClient.stopContainerCmd(id).exec();
        dockerClient.removeContainerCmd(id).exec();
    }

    @Override
    public boolean running() {
        if (dockerClient.listContainersCmd()
                .withShowAll(true)
                .exec()
                .stream()
                .noneMatch(c -> c.getId().equals(id))) {
            throw new IllegalStateException(String.format("Container [%s] does not exist", containerName));
        }

        return Boolean.TRUE.equals(dockerClient.inspectContainerCmd(id)
                .exec()
                .getState()
                .getRunning());
    }

    @Override
    public int exposedPort(int containerPort) {
        if (!running()) {
            throw new IllegalStateException(String.format("Container [%s] not running", containerName));
        }

        return Integer.valueOf(
                dockerClient.inspectContainerCmd(id)
                        .exec()
                        .getNetworkSettings()
                        .getPorts()
                        .getBindings()
                        .get(new ExposedPort(containerPort))[0]
                        .getHostPortSpec()
        );
    }
}
