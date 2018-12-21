package net.cpollet.docker;

import com.github.dockerjava.api.DockerClient;

public interface ReadinessCheck {
    ReadinessCheck NOWAIT = (dockerClient, id) -> {
    };

    void await(DockerClient dockerClient, String id) throws InterruptedException;
}
