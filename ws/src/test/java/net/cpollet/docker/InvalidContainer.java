package net.cpollet.docker;

public final class InvalidContainer implements Container {
    private final String containerName;
    private final ContainerException cause;

    public InvalidContainer(String containerName, ContainerException cause) {
        this.containerName = containerName;
        this.cause = cause;
    }

    @Override
    public void start() {
        throw new ContainerException(String.format("Could not start container [%s]", containerName), cause);
    }

    @Override
    public void startAsync() {
        start();
    }

    @Override
    public void waitReady() {
        start();
    }

    @Override
    public void stop() {
        throw new ContainerException(String.format("Could not stop container [%s]", containerName), cause);
    }

    @Override
    public boolean running() {
        throw new ContainerException(String.format("Container [%s] does not exist", containerName), cause);
    }

    @Override
    public int exposedPort(int containerPort) {
        throw new ContainerException(String.format("Could not get host port on container [%s]", containerName), cause);
    }
}
