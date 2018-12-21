package net.cpollet.docker;

public interface Container {
    void start() throws InterruptedException;

    void startAsync();

    void waitReady() throws InterruptedException;

    void stop();

    boolean running();

    int exposedPort(int containerPort);
}
