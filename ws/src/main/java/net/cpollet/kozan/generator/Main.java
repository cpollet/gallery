package net.cpollet.kozan.generator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public final class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        Generator<Integer> randomIntegerGenerator = new RandomIntegerGenerator(0, 10);
        for (int i = 0; i < 10; i++) {
            log(randomIntegerGenerator, i);
        }

        Generator<Integer> constantGenerator = new ConstantGenerator<>(0);
        for (int i = 0; i < 10; i++) {
            log(constantGenerator, i);
        }

        Generator<Integer> loopingGenerator = new LoopingGenerator<>(1, 2);
        for (int i = 0; i < 10; i++) {
            log(loopingGenerator, i);
        }

        Generator<Integer> sequenceGenerator = new SequenceGenerator<>(0, v -> v + 1);
        for (int i = 0; i < 10; i++) {
            log(sequenceGenerator, i);
        }

        Generator<Integer> integerIncrementGenerator = new IntegerIncrementGenerator(0, 3);
        for (int i = 0; i < 10; i++) {
            log(integerIncrementGenerator, i);
        }

        Generator<Integer> supplierGenerator = new SupplierGenerator<>(() -> LocalDateTime.now().getNano());
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1L);
            log(supplierGenerator, i);
        }
    }

    private static void log(Generator<Integer> randomIntegerGenerator, int i) {
        log.info("{}: {}", i, randomIntegerGenerator.next());
    }
}
