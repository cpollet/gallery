package net.cpollet.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
class LogReadinessCheck implements ReadinessCheck {
    private final List<String> needles;

    LogReadinessCheck(List<String> needles) {
        this.needles = new LinkedList<>(needles);
    }

    LogReadinessCheck(String needle) {
        this(Collections.singletonList(needle));
    }

    @Override
    public void await(DockerClient dockerClient, String id) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(needles.size());
        LogContainerResultCallback logCallback = dockerClient.logContainerCmd(id)
                .withFollowStream(true)
                .withStdErr(true)
                .withStdOut(true)
                .withTailAll()
                .exec(new LogContainerResultCallback() {
                    @Override
                    public void onNext(Frame item) {
                        super.onNext(item);
                        if (!needles.isEmpty() && item.toString().contains(needles.get(0))) {
                            log.debug("Read [{}] from [{}]", needles.get(0), id);
                            needles.remove(0);
                            latch.countDown();
                        }
                    }
                });

        latch.await();

        try {
            logCallback.close();
        } catch (IOException e) {
            log.warn("Unable to close log callback: {}", e.getMessage(), e);
        }
    }
}
