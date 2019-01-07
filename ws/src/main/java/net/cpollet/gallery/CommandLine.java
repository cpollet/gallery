package net.cpollet.gallery;

import com.beust.jcommander.JCommander;
import net.cpollet.kozan.lazy.Lazy;

final class CommandLine {
    private final Lazy<Arguments> arguments;

    CommandLine(String[] args) {
        this.arguments = new Lazy<>(() -> {
            Arguments a = new Arguments();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);
            return a;
        });
    }

    String databaseHost() {
        return arguments.value().getDatabaseHost();
    }
}
